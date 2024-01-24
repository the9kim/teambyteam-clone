package web.teambyteam.teamplace.domain.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import web.teambyteam.teamplace.exception.TeamPlaceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NameTest {

    @Test
    void createTeamPlaceName() {
        // given
        String name = "my team";

        // when
        assertDoesNotThrow(() -> new Name(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  myteam", "myteam  ", "    myteam    "})
    void trimName(String teamWithBlank) {
        // given
        String expected = "myteam";

        // when
        Name name = new Name(teamWithBlank);

        // then
        assertThat(name.getValue()).isEqualTo(expected);
    }

    @Test
    void exceptionWithWrongNameLength() {
        // given
        String wrongName = "a".repeat(21);

        // when & then
        assertThatThrownBy(() -> new Name(wrongName))
                .isInstanceOf(TeamPlaceException.NameLengthException.class)
                .hasMessage(String.format("이름이 최대 길이를 초과했습니다. - request info { allowed_length : 20, input_length :%d }"
                        , wrongName.length())
                );
    }

    @Test
    void exceptionWithNullName() {
        // given
        String wrongName = null;

        // when & then
        assertThatThrownBy(() -> new Name(wrongName))
                .isInstanceOf(NullPointerException.class);
    }
}
