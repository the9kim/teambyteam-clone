package web.teambyteam.teamplace.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.teambyteam.member.domain.MemberTeamPlace;

import java.util.List;

public interface TeamPlaceRepository extends JpaRepository<TeamPlace, Long> {

    @Query("SELECT mt FROM MemberTeamPlace mt " +
            "join mt.member m " +
            "join mt.teamPlace t "+
            "WHERE t.id=:id")
    List<MemberTeamPlace> findMemberTeamPlacesById(@Param("id") Long teamPlaceId);
}
