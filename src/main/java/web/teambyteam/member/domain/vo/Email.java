package web.teambyteam.member.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private static final String EMAIL_FORMAT = "^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,8}$";

    private final String value;

    public Email(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (Objects.isNull(value)) {
            throw new NullPointerException();
        }

        if (!Pattern.matches(EMAIL_FORMAT, value)) {
            throw new IllegalArgumentException();
        }
    }
}
