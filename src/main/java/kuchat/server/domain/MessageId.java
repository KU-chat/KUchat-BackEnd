package kuchat.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class MessageId implements Serializable {

    private Long roomId;        // @MapsId("roomId")로 매핑

    @Column(name = "message_id")
    private Long messageId;
}
