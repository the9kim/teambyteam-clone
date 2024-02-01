package web.teambyteam.member.exception;

public class MemberTeamPlaceException extends RuntimeException {

    public MemberTeamPlaceException(String message) {
        super(message);
    }

    public static class NotFoundException extends MemberTeamPlaceException {
        public NotFoundException() {
            super("존재하지 않는 멤버의 팀 공간입니다.");

        }
    }

    public static class RegisterDuplicationException extends MemberTeamPlaceException {
        public RegisterDuplicationException(Long memberId, Long teamPlaceId) {
            super(String.format(
                    "해당 팀플레이스에 이미 가입한 회원입니다. - log info { member_id : %d, team_place_id : %d}", memberId, teamPlaceId));
        }
    }
}
