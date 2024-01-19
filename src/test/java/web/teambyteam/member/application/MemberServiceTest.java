package web.teambyteam.member.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.application.dto.SignUpResponse;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void signUp() {

        // given
        SignUpRequest request = new SignUpRequest("roy", "roy@gmail.com", "image");

        // when
        SignUpResponse response = memberService.signUp(request);

        // then
        Assertions.assertThat(response.memberId()).isEqualTo(1L);

    }
}
