package com.example.lmsbackend.domain;

import com.example.lmsbackend.domain.classmodel.UserVotingChoiceEntity;
import com.example.lmsbackend.domain.notification.UserAnnouncementEntity;
import com.example.lmsbackend.enums.AccountTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "users")
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.userId", cascade = CascadeType.ALL)
    private List<UserRoleEntity> userRoleEntities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserPermissionEntity> userPermissionEntities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
    private List<BranchEntity> branches;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ChatRoomUserEntity> chats;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
    private Set<ChatRoomEntity> chatRooms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserAnnouncementEntity> notifications = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserVotingChoiceEntity> votingChoices = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(avatar, that.avatar) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, firstName, lastName, email, phone, avatar, createdAt);
    }

    // constructor for fetching auth information
    public UserEntity(Long id, String username, String password, String firstName, String lastName, String email, String phone, String avatar, AccountTypeEnum accountType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.accountType = accountType;
    }
}
