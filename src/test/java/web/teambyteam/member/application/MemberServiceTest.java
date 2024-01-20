package web.teambyteam.member.application;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.member.application.dto.MyInfoResponse;
import web.teambyteam.member.application.dto.MyInfoUpdateRequest;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.application.dto.SignUpResponse;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.exception.MemberException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("/h2-truncate.sql")
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

        Member savedMember = memberRepository.findById(response.memberId())
                .orElseThrow(() -> new MemberException.NotFoundException(response.memberId()));


        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedMember.getName().getValue()).isEqualTo("roy");
            softly.assertThat(savedMember.getEmail().getValue()).isEqualTo("roy@gmail.com");
            softly.assertThat(savedMember.getProfileImageUrl().getValue()).isEqualTo("image");
        });
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

    @Test
    void updateMyInfo() {
        // given
        Member savedMember = memberRepository.save(new Member("roy", "roy@gmail.com", "image"));
        MyInfoUpdateRequest request = new MyInfoUpdateRequest("koy");

        // when
        memberService.updateMyInfo(savedMember.getId(), request);

        MyInfoResponse myInfo = memberService.getMyInfo(savedMember.getId());

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(myInfo.name()).isEqualTo("koy");
        });
    }

    @Test
    void deleteMember() {

        // given
        Member savedMember = memberRepository.save(new Member("roy", "roy@gmail.com", "image"));

        // when
        memberService.cancelMembership(savedMember.getId());

        // then

        Assertions.assertThatThrownBy(
                () -> memberRepository.findById(savedMember.getId())
                        .orElseThrow(() -> new MemberException.NotFoundException(savedMember.getId()))
        ).isInstanceOf(MemberException.NotFoundException.class);
    }

}
