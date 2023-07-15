package com.example.lmsbackend.service.announcement;

import com.example.lmsbackend.config.firebase.FirebaseConfiguration;
import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.criteria.AnnouncementCriteria;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.compositekey.UserAnnouncementKey;
import com.example.lmsbackend.domain.notification.AnnouncementEntity;
import com.example.lmsbackend.domain.notification.TagEntity;
import com.example.lmsbackend.domain.notification.UserAnnouncementEntity;
import com.example.lmsbackend.dto.notification.AnnouncementDto;
import com.example.lmsbackend.dto.notification.AnnouncementPagedDto;
import com.example.lmsbackend.dto.notification.CreateAnnouncementRequest;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.AnnouncementScope;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.exceptions.notification.AnnouncementNotFoundException;
import com.example.lmsbackend.mapper.AnnouncementMapper;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.repository.*;
import com.example.lmsbackend.service.UserService;
import com.example.lmsbackend.service.UtilsService;
import com.example.lmsbackend.utils.FileUtils;
import com.example.lmsbackend.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.example.lmsbackend.constant.AppConstant.ANNOUNCEMENT;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementFactory announcementFactory;
    private final AnnouncementRepository announcementRepository;
    private final UserAnnouncementRepository userAnnouncementRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final AnnouncementMapper announcementMapper;
    private final ClassTeacherRepository classTeacherRepository;

    private final SimpMessagingTemplate messageTemplate;
    private final MailUtils mailUtils;
    private final FileUtils fileUtils;
    private final FirebaseConfiguration firebaseConfiguration;
    private final UserService userService;
    private final EntityManager entityManager;
    private final ClassRepository classRepository;
    private final UtilsService utilsService;

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_ANNOUNCEMENT_CLASS)
    public AnnouncementDto createAnnouncement(Long resourceId, CreateAnnouncementRequest dto) {

        var sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(UserNotFoundException::new);
        var className = classRepository.getClassName(resourceId);

        var announcementOperation = announcementFactory.getAnnouncementOperation(AnnouncementScope.CLASS.getScope());
        var announcement = mapAnnouncement(resourceId, dto, sender);
        var receivers = getReceivers(resourceId, dto, announcementOperation, announcement);
        addSenderIfNeeded(sender, receivers);
        var tags = getOrSaveTags(List.of(StringUtils.split(dto.getTags(), ",")));
        updateNotificationTag(announcement, tags);
        updateUserNotification(announcement, receivers);
        announcementRepository.save(announcement);

        var announcementToBeSent = mapToNotification(announcement, sender, tags, false);
        announcementToBeSent.setClassId(resourceId);
        announcementToBeSent.setClassName(className);
        sendAnnouncementViaEmail(dto, receivers, announcementToBeSent, className);
        return announcementToBeSent;
    }

    private void sendAnnouncementViaEmail(CreateAnnouncementRequest dto,
                                          List<UserEntity> receivers,
                                          AnnouncementDto announcementToBeSent,
                                          String className) {
        var tenantName = utilsService.getTenantName();
        if (BooleanUtils.isTrue(dto.getSendMailAsCopy())) {
            var attachment = dto.getAttachment();
            if (StringUtils.isNoneBlank(attachment)) {
                var file = fileUtils.getFileFromFirebase(attachment);
                if (file == null) {
                    return;
                }
                var content = String.format("<p><b>%s</b></p><p>%s</p>", dto.getSubject(), dto.getContent());
                receivers.forEach(receiver -> mailUtils.sendMail(tenantName,
                        receiver.getEmail(),
                        "Thông báo mới từ lớp " + className,
                        content, file));

            } else {
                var map = new HashMap<String, Object>();
                map.put("subject", dto.getSubject());
                map.put("content", dto.getContent());
                receivers.forEach(receiver -> mailUtils.sendMailWithTemplate(tenantName,
                        receiver.getEmail()
                        , "Thông báo mới từ lớp " + className,
                        map,
                        ANNOUNCEMENT));
            }
        }

        receivers.stream()
                .filter(x -> !Objects.equals(x.getId(), announcementToBeSent.getSenderId()))
                .forEach(receiver -> messageTemplate.convertAndSend("topic.user.announcement." + receiver.getId(), announcementToBeSent));
    }

    private AnnouncementEntity mapAnnouncement(Long resourceId, CreateAnnouncementRequest dto, UserEntity sender) {
        var announcement = announcementMapper.mapToNotification(dto);
        announcement.setScope(AnnouncementScope.CLASS);
        announcement.setScopeId(resourceId);
        announcement.setSender(sender);
        return announcement;
    }

    private List<UserEntity> getReceivers(Long resourceId, CreateAnnouncementRequest dto, AnnouncementOperation announcementOperation, AnnouncementEntity announcement) {
        List<UserEntity> receivers;
        if (CollectionUtils.isEmpty(dto.getReceiversId())) {
            receivers = announcementOperation.getReceivers(resourceId);
            announcement.setToAllMembers(true);
        } else {
            receivers = userRepository.findUserEntitiesByIdIn(dto.getReceiversId());
        }
        return receivers;
    }

    private void addSenderIfNeeded(UserEntity sender, List<UserEntity> receivers) {
        if (receivers.stream().noneMatch(r -> Objects.equals(r.getId(), sender.getId()))) {
            receivers.add(sender);
        }
    }

    private List<TagEntity> getOrSaveTags(List<String> tagsName) {
        var tags = tagRepository.findTagEntitiesByNameIn(tagsName);
        var existingTagsName = tags.stream()
                .map(TagEntity::getName)
                .collect(toList());
        var newTags = tagsName.stream()
                .filter(name -> !existingTagsName.contains(name))
                .map(name -> {
                    var newTag = new TagEntity();
                    newTag.setName(name);
                    return newTag;
                })
                .collect(toList());
        tagRepository.saveAll(newTags);
        tags.addAll(newTags);
        return tags;
    }

    private void updateNotificationTag(AnnouncementEntity notification, List<TagEntity> tags) {
        tags.forEach(tag -> tag.getNotifications().add(notification));
        notification.getTags().addAll(tags);
    }

    private void updateUserNotification(AnnouncementEntity notification, List<UserEntity> receivers) {
        receivers.forEach(user -> {
            var userNotification = new UserAnnouncementEntity();
            userNotification.setNotification(notification);
            userNotification.setUser(user);
        });
    }

    private AnnouncementDto mapToNotification(UserAnnouncementEntity userAnnouncementEntity, AnnouncementEntity entity, UserEntity sender, Collection<TagEntity> tags) {
        var notification = mapToNotification(entity, sender, tags, false);
        notification.setSeen(userAnnouncementEntity.getSeen());
        return notification;
    }

    private AnnouncementDto mapToNotification(AnnouncementEntity entity, UserEntity sender, Collection<TagEntity> tags, boolean readOnly) {
        var notification = new AnnouncementDto();
        notification.setId(entity.getId());
        notification.setSubject(entity.getSubject());
        notification.setContent(entity.getContent());
        notification.setAttachment(entity.getAttachment());
        notification.setSentAt(entity.getSentAt());
        notification.setSenderId(sender.getId());
        notification.setSenderName(String.format("%s %s", sender.getLastName(), sender.getFirstName()));
        notification.setImage(sender.getAvatar());
        notification.setTags(tags.stream().map(TagEntity::getName).collect(toList()));
        notification.setReadOnly(readOnly);
        return notification;
    }

    public AnnouncementDto getAnnouncementById(Long id) {
        var byId = announcementRepository.findById(id)
                .orElseThrow(() -> new AnnouncementNotFoundException(id));
        return mapToNotification(byId, byId.getSender(), byId.getTags(), true);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_ANNOUNCEMENT)
    public AnnouncementPagedDto getReceivedAnnouncementByClass(Long resourceId, AnnouncementCriteria criteria) {
        var currentUser = userService.getCurrentUser();
        var receiverId = currentUser.getId();
        var notificationPagedDto = new AnnouncementPagedDto();
        if (currentUser.getAccountType() == AccountTypeEnum.STAFF && !classTeacherRepository.isTeacherInClass(resourceId, receiverId)) {
            var result = userAnnouncementRepository.getAnnouncementByClass(resourceId, receiverId, criteria);
            MapperUtils.mapPagedDto(notificationPagedDto, result);
            notificationPagedDto.setListData(
                    result.getListData()
                            .stream()
                            .map(n -> mapToNotification(n, n.getSender(), n.getTags(), true))
                            .collect(toList()));
        } else {
            var notification = userAnnouncementRepository.getReceivedAnnouncementByClass(resourceId, receiverId, criteria);
            MapperUtils.mapPagedDto(notificationPagedDto, notification);
            notificationPagedDto.setListData(
                    notification.getListData()
                            .stream()
                            .map(n -> mapToNotification(n, n.getAnnouncement(), n.getAnnouncement().getSender(), n.getAnnouncement().getTags()))
                            .collect(toList())
            );
        }
        return notificationPagedDto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_ANNOUNCEMENT)
    public AnnouncementPagedDto getSentAnnouncementByClass(Long classId, AnnouncementCriteria criteria) {
        var senderId = userService.getCurrentUser().getId();
        var announcement = userAnnouncementRepository.getSentAnnouncementByClass(classId, senderId, criteria);
        var announcementPagedDto = new AnnouncementPagedDto();
        MapperUtils.mapPagedDto(announcementPagedDto, announcement);
        announcementPagedDto.setListData(
                announcement
                        .stream()
                        .map(n -> mapToNotification(n, n.getSender(), n.getTags(), true))
                        .collect(toList())
        );
        return announcementPagedDto;
    }

    @Transactional
    public Long deleteReceivedAnnouncement(Long announcementId) {
        var userId = userService.getCurrentUser().getId();
        var id = new UserAnnouncementKey(userId, announcementId);
        userAnnouncementRepository.findById(id)
                .orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
        userAnnouncementRepository.deleteById(id);
        return announcementId;
    }

    @Transactional
    public Long deleteSentAnnouncement(Long announcementId) {
        var senderId = userService.getCurrentUser().getId();
        entityManager.createQuery("UPDATE AnnouncementEntity a SET a.isVisibleForSender = false WHERE a.sender.id = (:senderId)")
                .setParameter("senderId", senderId)
                .executeUpdate();
        return announcementId;
    }

    @Transactional
    public AnnouncementDto seenAnnouncement(Long announcementId) {
        var currentUser = userService.getCurrentUser();
        var announcement = userAnnouncementRepository.findByAnnouncementAndUser(announcementId, currentUser.getId())
                .orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
        announcement.setSeen(true);
        userAnnouncementRepository.save(announcement);
        return mapToNotification(announcement.getAnnouncement(), announcement.getAnnouncement().getSender(), announcement.getAnnouncement().getTags(), false);
    }

    @Transactional
    public AnnouncementDto unseenAnnouncement(Long announcementId) {
        var currentUser = userService.getCurrentUser();
        var announcement = userAnnouncementRepository.findByAnnouncementAndUser(announcementId, currentUser.getId())
                .orElseThrow(() -> new AnnouncementNotFoundException(announcementId));
        announcement.setSeen(false);
        userAnnouncementRepository.save(announcement);
        return mapToNotification(announcement.getAnnouncement(), announcement.getAnnouncement().getSender(), announcement.getAnnouncement().getTags(), false);
    }
}
