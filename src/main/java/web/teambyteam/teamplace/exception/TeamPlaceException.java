package web.teambyteam.teamplace.exception;

public class TeamPlaceException extends RuntimeException {

    public TeamPlaceException(String message) {
        super(message);
    }

    public static class NameLengthException extends TeamPlaceException {

        public NameLengthException(int maxLength, int inputLength) {
            super(String.format(
                    "이름이 최대 길이를 초과했습니다. - request info { allowed_length : %d, input_length :%d }"
                    , maxLength
                    , inputLength ));
        }
    }

}
