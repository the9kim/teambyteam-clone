package web.teambyteam;

import io.restassured.internal.common.assertion.Assertion;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    @Test
    void name() {
        Hello hello = new Hello();
        Assertions.assertThat(hello.name()).isEqualTo("roy");
    }

    @Test
    void age() {
        Hello hello = new Hello();
        Assertions.assertThat(hello.age()).isEqualTo(36);
    }

    @Test
    void phone() {
        Hello hello = new Hello();
        Assertions.assertThat(hello.phone()).isEqualTo("010-1234-5678");
    }

    @Test
    void address() {
        Hello hello = new Hello();
        Assertions.assertThat(hello.address()).isEqualTo("changwon");
    }

    @Test
    void fullName() {
        Hello hello = new Hello();
        Assertions.assertThat(hello.fullName()).isEqualTo("roy kim");
    }

    @RequestMapping("job")
    String job() {
        return "programming";
    }


}
