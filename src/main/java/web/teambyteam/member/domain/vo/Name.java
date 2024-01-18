package web.teambyteam.member.domain.vo;

import lombok.Getter;
import web.teambyteam.member.exception.MemberException;

import java.util.Objects;

@Getter
public class Name {

    public static final int MAX_LENGTH = 20;
    private final String value;

    public Name(String value) {
        validateNull(value);
        String trimmedValue = value.trim();
        validate(trimmedValue);
        this.value = trimmedValue;
    }

    public void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new NullPointerException();
        }
    }

    private void validate(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new MemberException.NameLengthException(MAX_LENGTH, value);
        }
    }


}
