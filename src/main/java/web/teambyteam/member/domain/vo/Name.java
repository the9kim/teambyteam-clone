package web.teambyteam.member.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.teambyteam.member.exception.MemberException;

import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class Name {

    public static final int MAX_LENGTH = 20;
    @Column(name = "name", nullable = false, length = MAX_LENGTH)
    private String value;

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

    public void changeName(String name) {
        this.value = name;
    }

}

