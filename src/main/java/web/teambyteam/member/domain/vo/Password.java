package web.teambyteam.member.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.teambyteam.member.exception.MemberException;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class Password {

    private static final String PATTERN = "[a-zA-Z0-9가-힣!@#$%^&*]{1,16}";
    private static final int MAX_LENGTH = 16;

    @Column(name = "password", nullable = false, length = MAX_LENGTH)
    private String value;

    public Password(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (Objects.isNull(value)) {
            throw new NullPointerException("비밀번호 값은 null일 수 없습니다.");
        }
        if (!Pattern.matches(PATTERN, value)) {
            throw new MemberException.InvalidPasswordException();
        }
    }
}
