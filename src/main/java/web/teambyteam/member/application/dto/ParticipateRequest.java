package web.teambyteam.member.application.dto;

public record ParticipateRequest(
        Long memberId,
        Long teamPlaceId
) {
}
