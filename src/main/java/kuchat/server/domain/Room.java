package kuchat.server.domain;

import jakarta.persistence.*;
import kuchat.server.domain.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@NoArgsConstructor
public class Room extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMember = new ArrayList<>();

//    @OneToMany(mappedBy = "room")
//    private List<Message> messages = new ArrayList<>();
}
