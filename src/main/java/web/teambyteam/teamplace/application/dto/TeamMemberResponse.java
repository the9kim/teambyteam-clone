package web.teambyteam.teamplace.application.dto;

import web.teambyteam.member.domain.Member;

public record TeamMemberResponse(
        Long memberId,
        String name,
        String profileImageUrl,
        boolean isMe
) {
    public static TeamMemberResponse of(Member member) {
        return new TeamMemberResponse(
                member.getId(),
                member.getName().getValue(),
                member.getProfileImageUrl().getValue(),
                false
        );
    }

    public static TeamMemberResponse createMySelf(Member member) {
        return new TeamMemberResponse(
                member.getId(),
                member.getName().getValue(),
                member.getProfileImageUrl().getValue(),
                true
        );
    }
}
