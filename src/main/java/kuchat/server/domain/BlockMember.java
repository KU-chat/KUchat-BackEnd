package kuchat.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "block_member")
@Getter
@NoArgsConstructor
public class BlockMember extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
