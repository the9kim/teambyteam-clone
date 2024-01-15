package web.teambyteam;

import io.restassured.internal.common.assertion.Assertion;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    @Test
    void name() {

        Assertions.assertThat("hello").isEqualTo("hello");
    }

    @Test
    void age() {
        Assertions.assertThat(1).isEqualTo(1);
    }

    @Test
    void phone() {
        Assertions.assertThat(010).isEqualTo(010);
    }
}
