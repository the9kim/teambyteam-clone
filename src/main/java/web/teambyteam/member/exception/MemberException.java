package web.teambyteam.member.exception;

public class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }

    public static class NameLengthException extends MemberException {
        public NameLengthException(int allowedLength, String inputName) {
            super(String.format(
                    "멤버 이름의 길이가 최대 이름 길이를 초과했습니다. - request info { allowed_length : %d, input_value_length : %d }",
                    allowedLength,
                    inputName.length()
            ));
        }
    }


}