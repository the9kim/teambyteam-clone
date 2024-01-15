package web.teambyteam;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    @Test
    void name() {

        Assertions.assertThat("hello").isEqualTo("hello");
    }
}
