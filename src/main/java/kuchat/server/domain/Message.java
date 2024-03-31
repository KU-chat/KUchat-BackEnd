package kuchat.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "message")
@Getter
@NoArgsConstructor
public class Message extends BaseTime {

    @EmbeddedId     // 복합키
    private MessageId id;

    @MapsId("roomId")
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Message parent;         // 이 메세지가 답장이라면, 어떤 메세지에 대한 답장인지 나타내는 변수

    @Column(name = "sender_id")     // 이걸 Member가 아니라 Long으로 하는게 맞을까? 근데 Member로 하려면 FK가 되어야하니까..
    private Long sender;

    @OneToMany(mappedBy = "parent")
    private List<Message> replies = new ArrayList<>();       // 이 메세지에 대한 답장 메세지들 리스트
}
