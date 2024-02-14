package web.teambyteam.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import web.teambyteam.member.domain.vo.Email;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(Email email);

    Optional<Member> findByEmail(Email email);

}
