package web.teambyteam.member.domain.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"abc@email.com"})
    void succeedWithEmailFormat(String s) {

        // given & when
        assertDoesNotThrow(() -> new Email(s));
    }


    @ParameterizedTest
    @ValueSource(strings = {"abcd.com", "abcd@abc", "abcd@abc.123456789", "%^&abc@email.com", "abc@a_b.com"})
    void throwExceptionWithWrongEmailFormat(String s) {

        // given & when
        Assertions.assertThatThrownBy(() -> new Email(s))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
