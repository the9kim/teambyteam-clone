package web.teambyteam.member.application.dto;

import web.teambyteam.member.domain.MemberTeamPlace;
import web.teambyteam.teamplace.domain.vo.Name;

public record ParticipatingTeamResponse(
        Long teamPlaceId,
        Name teamPlaceName
) {
    public static ParticipatingTeamResponse of(MemberTeamPlace memberTeamPlace) {
        return new ParticipatingTeamResponse(
                memberTeamPlace.getTeamPlace().getId(),
                memberTeamPlace.getTeamPlace().getName()
        );
    }
}
