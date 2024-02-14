package web.teambyteam.member.application.dto;

public record SignUpRequest(
        String name,
        String email,
        String password,
        String profileImageUrl
) {
}
