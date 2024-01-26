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

}
