package kuchat.server.domain.member;

import kuchat.server.domain.enums.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPlatformAndEmail(Platform platform, String email);
}
