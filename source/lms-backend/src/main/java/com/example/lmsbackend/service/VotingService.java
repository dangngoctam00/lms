package com.example.lmsbackend.service;


import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.classmodel.UserVotingChoiceEntity;
import com.example.lmsbackend.domain.classmodel.VotingChoiceEntity;
import com.example.lmsbackend.domain.classmodel.VotingEntity;
import com.example.lmsbackend.dto.classes.VotingChoiceCount;
import com.example.lmsbackend.dto.classes.VotingChoiceDto;
import com.example.lmsbackend.dto.classes.VotingDto;
import com.example.lmsbackend.enums.ActionType;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.UnauthorizedException;
import com.example.lmsbackend.exceptions.aclass.ClassNotFoundException;
import com.example.lmsbackend.exceptions.aclass.*;
import com.example.lmsbackend.exceptions.course.ChapterNotFoundException;
import com.example.lmsbackend.mapper.VotingMapper;
import com.example.lmsbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class VotingService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final VotingRepository votingRepository;
    private final UserVotingChoiceRepository userVotingChoiceRepository;
    private final VotingChoiceRepository votingChoiceRepository;
    private final VotingMapper votingMapper;
    private final ClassService classService;
    private final ChapterClassRepository chapterClassRepository;
    private final ClassRepository classRepository;

    @PersistenceContext
    private final EntityManager entityManager;


    @Transactional
    public VotingDto choseVotingChoice(Long voteId, Long choiceId) {
        var username = userService.getCurrentUsername();
        var user = userRepository.findFetchVotingChoicesUsername(username)
                .orElseThrow(() -> new UnauthorizedException());
        var voting = votingRepository.findFetchUserVotingChoicesById(voteId)
                .orElseThrow(() -> new VotingNotFoundException(voteId));

        var votingChoice = voting.getChoices()
                .stream()
                .filter(choice -> choice.getId().equals(choiceId))
                .findAny()
                .orElseThrow(() -> new VotingChoiceNotFoundException(choiceId));

        var isAlreadyVoted = votingChoice.getChosenBy()
                .stream()
                .map(UserVotingChoiceEntity::getUser)
                .map(UserEntity::getUsername)
                .anyMatch(u -> u.equals(username));
        if (isAlreadyVoted) {
            entityManager.flush();
            entityManager.clear();
            entityManager.createQuery("DELETE FROM UserVotingChoiceEntity u WHERE u.id.userId = (:userId) and u.id.votingChoiceId = (:votingChoiceId)")
                    .setParameter("userId", user.getId())
                    .setParameter("votingChoiceId", choiceId)
                    .executeUpdate();
        } else {
            var userVotingChoice = new UserVotingChoiceEntity();
            userVotingChoice.setVotingChoice(votingChoice);
            userVotingChoice.setUser(user);
        }

        return getVoting(voteId);
    }

    @Transactional
//    @Auth(permission = PermissionEnum.UPDATE_VOTING)
    public VotingDto addVotingChoice(Long resourceId, VotingChoiceDto dto) {
        var voting = votingRepository.findFetchChoicesById(resourceId)
                .orElseThrow(() -> new VotingNotFoundException(resourceId));
        var username = userService.getCurrentUsername();
        if (Boolean.TRUE.equals(voting.getIsAllowedToAddChoice()) || StringUtils.equals(username, voting.getCreatedBy())) {
            var votingChoice = new VotingChoiceEntity();
            votingChoice.setVoting(voting);
            votingChoice.setContent(dto.getContent());
            votingRepository.save(voting);
            return getVoting(resourceId);
        } else {
            throw new VotingIsNotAllowedToAddChoiceException(resourceId);
        }
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_VOTING)
    public VotingDto getVoting(Long resourceId) {
        var voting = votingRepository.findById(resourceId)
                .orElseThrow(() -> new VotingNotFoundException(resourceId));

        var votingChoiceCounts = userVotingChoiceRepository.countNumberOfChosenByVotingChoice();
        var total = votingChoiceCounts.stream()
                .mapToLong(VotingChoiceCount::getCount)
                .sum();

        var dto = votingMapper.mapToVotingDto(voting);

        var votingChoiceDto = voting.getChoices()
                .stream()
                .map(choice -> {
                    var any = votingChoiceCounts.stream()
                            .filter(c -> Objects.equals(c.getChoiceId(), choice.getId()))
                            .findAny();
                    var choiceDto = votingMapper.mapToVotingChoiceDto(choice);
                    choiceDto.setContent(choice.getContent());
                    choiceDto.setId(choice.getId());
                    if (any.isPresent()) {
                        var votingChoice = any.get();
                        choiceDto.setNumbersOfChosen(Math.toIntExact(votingChoice.getCount()));
                        choiceDto.setPercent(choiceDto.getNumbersOfChosen() / (double) total * 100);
                    } else {
                        choiceDto.setNumbersOfChosen(0);
                        choiceDto.setPercent(0.0);
                    }
                    return choiceDto;
                })
                .collect(toList());
        dto.setChoices(votingChoiceDto);
        return dto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_VOTING)
    public VotingDto updateVoting(Long resourceId, VotingDto dto) {
        var voting = votingRepository.findFetchChoicesById(resourceId)
                .orElseThrow(() -> new VotingNotFoundException(resourceId));

        entityManager.detach(voting);
        votingMapper.mapToVotingEntity(voting, dto);

        entityManager.merge(voting);

        entityManager.flush();
        return getVoting(resourceId);
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_VOTING)
    public VotingDto createVoting(Long resourceId, Long chapterId, VotingDto dto) {
        if (!classRepository.existsById(resourceId)) {
            throw new ClassNotFoundException(resourceId);
        }
        var chapter = chapterClassRepository.findFetchActionsById(chapterId)
                .orElseThrow(() -> new ChapterNotFoundException(chapterId));

        var voting = persistVoting(resourceId, dto);
        var sortIndex = classService.buildChapterSortIndex(chapter, voting.getId(), ActionType.VOTING.name());

        dto.setId(voting.getId());
        voting.setOrder(sortIndex.getOrder());
        return votingMapper.mapToVotingDto(voting);
    }

    private VotingEntity persistVoting(Long classId, VotingDto dto) {
        var voting = votingMapper.mapToVotingEntity(dto);
        voting.setClassEntity(entityManager.getReference(ClassEntity.class, classId));
        return votingRepository.save(voting);
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_VOTING)
    public Long deleteVoting(Long resourceId) {
        entityManager.createQuery("DELETE FROM VotingEntity v WHERE v.id = (:id)")
                .setParameter("id", resourceId)
                .executeUpdate();
        return resourceId;
    }

    @Transactional
//    @Auth(permission = PermissionEnum.UPDATE_VOTING)
    public VotingDto updateVotingChoice(Long resourceId, Long choiceId, VotingChoiceDto dto) {
        var currentUser = userService.getCurrentUsername();
        var choice = votingChoiceRepository.findFetchVotingById(choiceId)
                .orElseThrow(() -> new VotingChoiceNotFoundException(choiceId));
        if (StringUtils.equals(choice.getVoting().getCreatedBy(), currentUser) || StringUtils.equals(choice.getCreatedBy(), currentUser)) {
            choice.setContent(dto.getContent());
            entityManager.flush();
            return getVoting(resourceId);
        } else {
            throw new UserIsNotAllowedToUpdateChoiceException(currentUser, choiceId);
        }
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_VOTING)
    public VotingDto deleteVotingChoice(Long resourceId, Long choiceId) {
        var currentUser = userService.getCurrentUsername();
        var choice = votingChoiceRepository.findFetchVotingById(choiceId)
                .orElseThrow(() -> new VotingChoiceNotFoundException(choiceId));
        if (StringUtils.equals(choice.getVoting().getCreatedBy(), currentUser) || StringUtils.equals(choice.getCreatedBy(), currentUser)) {
            entityManager.createQuery("DELETE FROM VotingChoiceEntity v WHERE v.id = (:id)")
                    .setParameter("id", choiceId)
                    .executeUpdate();
            entityManager.flush();
            return getVoting(resourceId);
        } else {
            throw new UserIsNotAllowedToUpdateChoiceException(currentUser, choiceId);
        }
    }
}
