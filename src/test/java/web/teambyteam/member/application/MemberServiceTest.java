package web.teambyteam.member.application;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.member.application.dto.MyInfoResponse;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.application.dto.SignUpResponse;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.exception.MemberException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void signUp() {

        // given
        SignUpRequest request = new SignUpRequest("roy", "roy@gmail.com", "image");

        // when
        SignUpResponse response = memberService.signUp(request);

        // then
        Assertions.assertThat(response.memberId()).isEqualTo(1L);

    }

    /**
     * There is need to insert member first
     */
    @Test
    void getMyInfo() {
        // given
        Member savedMember = memberRepository.save(new Member("roy", "roy@gmail.com", "image"));

        // when
        MyInfoResponse response = memberService.getMyInfo(savedMember.getId());

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.name()).isEqualTo("roy");
            softly.assertThat(response.email()).isEqualTo("roy@gmail.com");
            softly.assertThat(response.profileImageUrl()).isEqualTo("image");
        });
    }

    /**
     * How can I define Non-exist Member(id)?
     */
    @Test
    void throwExceptionWithNonExistMemberId() {
        // given
        long nonExistMemberId = -1;

        // when & then

        Assertions.assertThatThrownBy(() ->
                        memberService.getMyInfo(nonExistMemberId))
                .isInstanceOf(MemberException.NotFoundException.class);
    }

}
