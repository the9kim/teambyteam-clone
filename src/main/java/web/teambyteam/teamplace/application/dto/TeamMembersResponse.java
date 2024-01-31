package web.teambyteam.teamplace.application.dto;

import web.teambyteam.member.domain.Member;

import java.util.List;

public record TeamMembersResponse(
        List<TeamMemberResponse> members
) {
    public static TeamMembersResponse of(List<Member> members, Long memberId) {
        List<TeamMemberResponse> teamMembers = members.stream()
                .map(member -> {
                    if (member.getId() == memberId) {
                        return TeamMemberResponse.createMySelf(member);
                    } else {
                        return TeamMemberResponse.of(member);
                    }
                })
                .toList();

        return new TeamMembersResponse(teamMembers);
    }

}
