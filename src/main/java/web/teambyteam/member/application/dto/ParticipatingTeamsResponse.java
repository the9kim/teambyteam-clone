package web.teambyteam.member.application.dto;

import web.teambyteam.member.domain.MemberTeamPlace;

import java.util.List;

public record ParticipatingTeamsResponse(List<ParticipatingTeamResponse> teamPlaces) {

    public static ParticipatingTeamsResponse of(List<MemberTeamPlace> memberTeamPlaces) {

        return new ParticipatingTeamsResponse(memberTeamPlaces.stream()
                .map(ParticipatingTeamResponse::of)
                .toList());
    }

}
