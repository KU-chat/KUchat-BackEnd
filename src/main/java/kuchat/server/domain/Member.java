package kuchat.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import kuchat.server.domain.enums.Gender;
import kuchat.server.domain.enums.LearnLanguage;
import kuchat.server.domain.enums.SettingLanguage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Email      // 건국대 이메일로만 제한하는 방법 없나 : @konkuk.ac.kr로 끝나는 이메일만 받습니다.
    @NotEmpty
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "department", nullable = false)
    private String department;

    @Size(min = 9, max = 9)
    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(min = 6, max = 6)
    private String birthday;

    @Column(name = "setting_langugage", nullable = false)
    @Enumerated(EnumType.STRING)
    private SettingLanguage setLanguage;

    @Column(name = "learn_language1", nullable = false)
    @Enumerated(EnumType.STRING)
    private LearnLanguage firstLanguage;

    @Column(name = "learn_language2")
    @Enumerated(EnumType.STRING)
    private LearnLanguage secondLanguage;

    private String hometown;

//    private String fcmToken;

    private String profileImage;

    @OneToMany(mappedBy = "member")      // member:roomMember = 1:다 -> member는 oneToMany     // 얘는 연관관계 종속됨 (주인은 RoomMember 클래스의 member필드)
    private List<RoomMember> roomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BlockMember> blockMembers = new ArrayList<>();

    @OneToMany(mappedBy = "friend")
    private List<Friendship> friendships = new ArrayList<>();

//    @OneToMany(mappedBy = "member")
//    private List<Notification> notifications = new ArrayList<>();

}
