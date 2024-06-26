package kuchat.server.domain.member.repository;

import kuchat.server.domain.enums.Platform;
import kuchat.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPlatformAndAttributeName(Platform platform, String nameAttributeKey);

    Optional<Member> findAllByStudentId(String studentId);

    Optional<Member> findByEmail(String email);
}
