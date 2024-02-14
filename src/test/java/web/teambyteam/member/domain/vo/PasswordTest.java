package web.teambyteam.member.domain.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import web.teambyteam.member.exception.MemberException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678", "abcdefgh", "가나다라", "!@#$%^&*",
            "1234567890111213", "abcdefghijklmnop", "가나다라마바사아", "!@#$%^&*!@#$%^&*",
            "1234abcd가나!@#$"})
    void createPassword(String s) {
        // given & when & then
        assertDoesNotThrow(
                () -> new Password(s)
        );

    }

    @Test
    void createPassword_withNullValue_shouldFail() {
        // given
        String wrongPassword = null;

        // when & then
        Assertions.assertThatThrownBy(() ->
                        new Password(wrongPassword))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("비밀번호 값은 null일 수 없습니다.");
    }

    @Test
    void createPassword_OverLength_shouldFail() {
        // given
        String wrongPassword = "1".repeat(17);

        // when & then
        Assertions.assertThatThrownBy(() ->
                        new Password(wrongPassword))
                .isInstanceOf(MemberException.InvalidPasswordException.class)
                .hasMessage("비밀번호는 최소 1자, 최대 16자로 구성된 문자, 숫자, 특수 기호만 사용할 수 있습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345678()", "abcdefgh[]", "가나다라{}", "가나다라_-+",
            "12345678|", "abcdefgh'", "가나다라\"", "가나다라\\",
            "12345678<>", "abcdefgh?", "가나다라/", "가나다라.",
    })
    void createPassword_WithWrongPattern_shouldFail(String s) {
        // given & when & then
        Assertions.assertThatThrownBy(() ->
                new Password(s));

    }

}
