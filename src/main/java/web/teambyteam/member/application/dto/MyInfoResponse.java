package web.teambyteam.member.application.dto;

import web.teambyteam.member.domain.Member;

public record MyInfoResponse(
        String name,
        String email,
        String profileImageUrl

) {

    public MyInfoResponse(String name, String email, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static MyInfoResponse of(Member member) {
        return new MyInfoResponse(
                member.getName().getValue(),
                member.getEmail().getValue(),
                member.getProfileImageUrl().getValue()
        );
    }
}
