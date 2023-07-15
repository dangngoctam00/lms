package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.config.security.aop.caching.PermissionCaching;
import com.example.lmsbackend.criteria.AnnouncementCriteria;
import com.example.lmsbackend.domain.ChatRoomUserEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.compositekey.UserAnnouncementKey;
import com.example.lmsbackend.domain.notification.AnnouncementEntity;
import com.example.lmsbackend.domain.notification.QAnnouncementEntity;
import com.example.lmsbackend.domain.notification.UserAnnouncementEntity;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.AnnouncementScope;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.repository.custom.PagedList;
import com.example.lmsbackend.repository.custom.UserAnnouncementRepositoryCustom;
import com.example.lmsbackend.service.UserService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.lmsbackend.constant.AppConstant.FETCH_GRAPH;
import static com.example.lmsbackend.constant.LimitValue.LIMIT;

@RequiredArgsConstructor
public class UserAnnouncementRepositoryCustomImpl implements UserAnnouncementRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    private final UserService userService;
    private final PermissionCaching permissionCaching;

    @Override
    public PagedList<AnnouncementEntity> getAnnouncementByClass(Long classId, Long receiverId, AnnouncementCriteria criteria) {
        var currentUser = userService.getCurrentUser();
        Long currentUserId = userService.getCurrentUserId();
        var permissions = permissionCaching.getByPermission(currentUserId, PermissionEnum.VIEW_LIST_ANNOUNCEMENT);
        if (CollectionUtils.isNotEmpty(permissions)) {
            var isLimitByTeaching = permissions.stream()
                    .anyMatch(permission -> permission.getIsLimitByTeaching() == LIMIT);
            if (isLimitByTeaching) {
                return new PagedList<>(List.of(), 0, 0, 0);
            }
        }
        var announcement = QAnnouncementEntity.announcementEntity;
        var query = new BlazeJPAQuery<AnnouncementEntity>(entityManager, criteriaBuilderFactory)
                .from(announcement)
                .where(announcement.scope.eq(AnnouncementScope.CLASS))
                .where(announcement.scopeId.eq(classId))
                .orderBy(announcement.id.desc());

        buildSearch(criteria, announcement, query);

        buildLimit(announcement, query, permissions, currentUser);
        var ids = query.select(announcement.id)
                .fetchPage((criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
        var res = new BlazeJPAQuery<AnnouncementEntity>(entityManager, criteriaBuilderFactory)
                .from(announcement)
                .leftJoin(announcement.sender)
                .fetchJoin()
                .leftJoin(announcement.tags)
                .fetchJoin()
                .where(announcement.id.in(ids))
                .orderBy(announcement.sentAt.desc())
                .select(announcement)
                .distinct()
                .fetch();

        return new PagedList<>(res, ids.getTotalSize(), (criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
    }

    private void buildSearch(AnnouncementCriteria criteria, QAnnouncementEntity announcement, BlazeJPAQuery<AnnouncementEntity> query) {
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            query.where(announcement.subject.toLowerCase().containsIgnoreCase(criteria.getKeyword())
                    .or(announcement.content.toLowerCase().containsIgnoreCase(criteria.getKeyword())));
        }
    }

    private void buildLimit(QAnnouncementEntity announcement, BlazeJPAQuery<AnnouncementEntity> query, List<PermissionSecurity> permissions, UserEntity currentUser) {
        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }
        var builder = new BooleanBuilder();
        if (currentUser.getAccountType() == AccountTypeEnum.STAFF) {
            boolean isLimitByOwner = permissions.stream()
                    .anyMatch(permission -> permission.getIsLimitByOwner() == LIMIT);
            if (isLimitByOwner) {
                builder.and(announcement.sender.id.eq(currentUser.getId()));
            }
        }
        builder.and(announcement.toAllMembers.eq(true));
        query.where(builder);
    }

    @Override
    public PagedList<UserAnnouncementEntity> getReceivedAnnouncementByClass(Long classId, Long receiverId, AnnouncementCriteria criteria) {
        var graph = entityManager.getEntityGraph("user-announcement");

        var cb = entityManager.getCriteriaBuilder();
        var userNotificationQuery = cb.createQuery(UserAnnouncementEntity.class);
        var root = userNotificationQuery.from(UserAnnouncementEntity.class);
        var predicates = new ArrayList<Predicate>();
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            predicates.add(cb.like(cb.lower(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.subject)), "%" + criteria.getKeyword().toLowerCase() + "%"));
            predicates.add(cb.like(cb.lower(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.content)), "%" + criteria.getKeyword().toLowerCase() + "%"));
        }

        var countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(UserAnnouncementEntity.class)));
        entityManager.createQuery(countQuery);
        if (CollectionUtils.isNotEmpty(predicates)) {
            countQuery.where(cb.and(
                    cb.or(predicates.toArray(new Predicate[0])),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.scope), AnnouncementScope.CLASS),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.scopeId), classId),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.user).get(UserEntity.Fields.id), receiverId)));

            userNotificationQuery.where(cb.and(
                    cb.or(predicates.toArray(new Predicate[0])),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.scope), AnnouncementScope.CLASS),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.scopeId), classId),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.user).get(UserEntity.Fields.id), receiverId)));
        } else {
            countQuery.where(cb.and(
                    cb.equal(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.scope), AnnouncementScope.CLASS),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.scopeId), classId),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.user).get(UserEntity.Fields.id), receiverId)));

            userNotificationQuery.where(cb.and(
                    cb.equal(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.scope), AnnouncementScope.CLASS),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.scopeId), classId),
                    cb.equal(root.get(UserAnnouncementEntity.Fields.user).get(UserEntity.Fields.id), receiverId)));
        }

        userNotificationQuery.orderBy(cb.desc(root.get(UserAnnouncementEntity.Fields.announcement).get(AnnouncementEntity.Fields.sentAt)),
                cb.desc(root.get(UserAnnouncementEntity.Fields.id).get(UserAnnouncementKey.Fields.receiverId)),
                cb.desc(root.get(UserAnnouncementEntity.Fields.id).get(UserAnnouncementKey.Fields.announcementId)));

        var count = entityManager.createQuery(countQuery).getSingleResult();

        var select = userNotificationQuery.select(root);

        var typedProgramQuery = entityManager.createQuery(select);

        typedProgramQuery.setFirstResult((criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize());
        typedProgramQuery.setMaxResults(criteria.getPagination().getSize());
        typedProgramQuery.setHint(FETCH_GRAPH, graph);
        var result = typedProgramQuery.getResultList();
        return new PagedList<>(result, count, (criteria.getPagination().getPage() - 1) * criteria.getPagination().getSize(), criteria.getPagination().getSize());
    }

    @Override
    public com.blazebit.persistence.PagedList<AnnouncementEntity> getSentAnnouncementByClass(Long classId, Long senderId, AnnouncementCriteria criteria) {
        var page = criteria.getPagination().getPage();
        var size = criteria.getPagination().getSize();
        var announcement = QAnnouncementEntity.announcementEntity;
        var query = new BlazeJPAQuery<ChatRoomUserEntity>(entityManager, criteriaBuilderFactory)
                .from(announcement)
                .where(announcement.sender.id.eq(senderId))
                .where(announcement.isVisibleForSender.eq(true))
                .leftJoin(announcement.sender)
                .fetchJoin();
        if (StringUtils.isNoneBlank(criteria.getKeyword())) {
            query.where(announcement.subject.containsIgnoreCase(criteria.getKeyword())
                    .or(announcement.content.containsIgnoreCase(criteria.getKeyword())));
        }
        return query.select(announcement)
                .orderBy(announcement.sentAt.desc(),
                        announcement.id.desc())
                .fetchPage((page - 1) * size, size);
    }

    @Override
    public Optional<UserAnnouncementEntity> findByAnnouncementAndUser(Long announcementId, Long userId) {
        var result = entityManager.createQuery("SELECT u FROM UserAnnouncementEntity u WHERE u.announcement.id = (:announcementId)" +
                        " AND u.user.id = (:userId)", UserAnnouncementEntity.class)
                .setParameter("announcementId", announcementId)
                .setParameter("userId", userId)
                .setHint(FETCH_GRAPH, entityManager.getEntityGraph("user-announcement"))
                .getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }
}
