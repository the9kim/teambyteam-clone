package web.teambyteam.member.domain.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import web.teambyteam.member.exception.MemberException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NameTest {

    @Test
    void succeedInCreatingName() {
        // given
        String value = "roy";

        // when & then
        assertDoesNotThrow(() -> new Name(value));
    }

    @Test
    void throwExceptionWithMoreThan20Char() {
        // given
        int length = 21;
        String value = "a".repeat(length);

        // when & then
        Assertions.assertThatThrownBy(
                        () -> new Name(value))
                .isInstanceOf(MemberException.NameLengthException.class)
                .hasMessage(
                        String.format("멤버 이름의 길이가 최대 이름 길이를 초과했습니다. - request info { allowed_length : %d, input_value_length : %d }"
                                , Name.MAX_LENGTH
                                , length
                        ));
    }

    @Test
    void isTrimmed() {
        // given
        String value = " roy ";
        String expected = "roy";

        // when
        Name name = new Name(value);

        //then
        Assertions.assertThat(name.getValue()).isEqualTo(expected);
    }

    @Test
    void isNull() {
        // given
        String value = null;

        // when & then
        Assertions.assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(NullPointerException.class);
    }
}
