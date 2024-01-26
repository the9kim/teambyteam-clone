package web.teambyteam.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTeamPlaceRepository extends JpaRepository<MemberTeamPlace, Long> {

    Optional<MemberTeamPlace> findByMemberIdAndTeamPlaceId(Long memberId, Long TeamPlaceId);

}
