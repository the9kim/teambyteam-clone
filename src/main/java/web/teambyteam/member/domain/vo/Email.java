package web.teambyteam.member.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class Email {

    private static final String EMAIL_FORMAT = "^[\\w.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,8}$";

    @Column(name = "email", nullable = false, unique = true)
    private String value;

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
