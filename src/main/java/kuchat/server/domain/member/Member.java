package kuchat.server.domain.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import kuchat.server.domain.BaseTime;
import kuchat.server.domain.blockMember.BlockMember;
import kuchat.server.domain.enums.*;
import kuchat.server.domain.friendship.Friendship;
import kuchat.server.domain.member.dto.SignupRequest;
import kuchat.server.domain.roomMember.RoomMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static kuchat.server.domain.enums.SettingLanguage.of;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Email
    @NotEmpty
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "department")
    private String department;

    @Size(min = 9, max = 9)
    @Column(name = "student_id")
    private String studentId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(min = 6, max = 6)
    private String birthday;

    @Column(name = "setting_langugage")
    @Enumerated(EnumType.STRING)
    private SettingLanguage setLanguage;

    @Column(name = "learn_language1")
    @Enumerated(EnumType.STRING)
    private LearnLanguage firstLanguage;

    @Column(name = "learn_language2")
    @Enumerated(EnumType.STRING)
    private LearnLanguage secondLanguage;

    private String hometown;

//    private String fcmToken;

    private String profileImage;

    @OneToMany(mappedBy = "member")
    // member:roomMember = 1:다 -> member는 oneToMany     // 얘는 연관관계 종속됨 (주인은 RoomMember 클래스의 member필드)
    private List<RoomMember> roomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BlockMember> blockMembers = new ArrayList<>();

    @OneToMany(mappedBy = "friend")
    private List<Friendship> friendships = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

//    @OneToMany(mappedBy = "member")
//    private List<Notification> notifications = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Column(nullable = false)
    private String attributeName;       // 플랫폼에서 제공하는 id

    private String refreshToken;

    @Builder
    public Member(String email, Platform platform, String attributeName) {
        this.email = email;
        this.platform = platform;
        this.attributeName = attributeName;
        this.role = Role.GUEST;            // 추가정보 받기 전
    }

    @Builder
    public Member(SignupRequest request){
        this.setLanguage = SettingLanguage.of(request.getSetLanguage());
        this.firstLanguage = LearnLanguage.of(request.getFirstLanguage());
        this.secondLanguage = LearnLanguage.of(request.getSecondLanguage());
        this.hometown = request.getHometown();
        this.name = request.getName();
        this.department = request.getDepartment();
        this.studentId = request.getStudentId();
        this.gender = Gender.of(request.getGender());
        this.birthday = request.getBirthday();
        this.role = Role.STUDENT;           // 추가정보 받은 후
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
