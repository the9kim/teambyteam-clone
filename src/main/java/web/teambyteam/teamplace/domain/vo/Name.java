package web.teambyteam.teamplace.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.teambyteam.teamplace.exception.TeamPlaceException;

import java.util.Objects;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class Name {

    private static final int MAX_LENGTH = 20;

    @Column(name = "name", nullable = false)
    private String value;

    public Name(String value) {
        validateNull(value);
        String trimmedValue = value.trim();
        validateLength(trimmedValue);
        this.value = trimmedValue;
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new NullPointerException();
        }
    }

    private void validateLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new TeamPlaceException.NameLengthException(MAX_LENGTH, value.length());
        }
    }

}
