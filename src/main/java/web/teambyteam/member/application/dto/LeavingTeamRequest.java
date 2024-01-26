package web.teambyteam.member.application.dto;

public record LeavingTeamRequest(
        Long memberId,
        Long teamPlaceId
) {
}
