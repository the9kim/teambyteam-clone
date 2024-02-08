package web.teambyteam.member.application;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.fixtures.FixtureBuilder;
import web.teambyteam.fixtures.MemberFixtures;
import web.teambyteam.fixtures.TeamPlaceFixtures;
import web.teambyteam.global.AuthMember;
import web.teambyteam.member.application.dto.MyInfoResponse;
import web.teambyteam.member.application.dto.MyInfoUpdateRequest;
import web.teambyteam.member.application.dto.ParticipatingTeamResponse;
import web.teambyteam.member.application.dto.ParticipatingTeamsResponse;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.domain.MemberTeamPlace;
import web.teambyteam.member.exception.MemberException;
import web.teambyteam.teamplace.domain.TeamPlace;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("/h2-truncate.sql")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FixtureBuilder builder;

    @Test
    void signUp() {

        // given
        SignUpRequest request = new SignUpRequest("roy", "roy@gmail.com", "image");

        // when
        Long memberId = memberService.signUp(request);

        Member savedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException.NotFoundException(memberId));


        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedMember.getName().getValue()).isEqualTo("roy");
            softly.assertThat(savedMember.getEmail().getValue()).isEqualTo("roy@gmail.com");
            softly.assertThat(savedMember.getProfileImageUrl().getValue()).isEqualTo("image");
        });
    }

    @Test
    void signUp_withDuplicateEmail_shouldFail() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());

        String duplicateEmail = "roy@gmail.com";
        SignUpRequest request = new SignUpRequest("name", duplicateEmail, "url");

        // when & then
        Assertions.assertThatThrownBy(() ->
                        memberService.signUp(request))
                .isInstanceOf(MemberException.DuplicateMemberException.class)
                .hasMessage("중복된 이메일입니다. - request info {email : %s", request.email());
    }

    /**
     * There is need to insert member first
     */
    @Test
    void getMyInfo() {
        // given
        Member savedMember = builder.buildMember(MemberFixtures.member1());

        // when
        MyInfoResponse response = memberService.getMyInfo(MemberFixtures.member1Request());

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.name()).isEqualTo(savedMember.getName().getValue());
            softly.assertThat(response.email()).isEqualTo(savedMember.getEmail().getValue());
            softly.assertThat(response.profileImageUrl()).isEqualTo(savedMember.getProfileImageUrl().getValue());
        });
    }

    /**
     * How can I define Non-exist Member(id)?
     */
    @Test
    void getNonExistMember_shouldFail() {
        // given
        String nonExistMemberEmail = "nonExistEmail@gmail.com";

        // when & then
        Assertions.assertThatThrownBy(() ->
                        memberService.getMyInfo(MemberFixtures.nonExistMemberRequest()))
                .isInstanceOf(MemberException.NotFoundException.class)
                .hasMessage(String.format(
                        "해당 멤버가 존재하지 않습니다. - request info { member_email : %s}", MemberFixtures.NON_EXIST_MEMBER_EMAIL
                ));
    }

    @Test
    void updateMyInfo() {
        // given
        Member savedMember = builder.buildMember(MemberFixtures.member1());
        MyInfoUpdateRequest request = new MyInfoUpdateRequest("koy");

        // when
        memberService.updateMyInfo(new AuthMember(savedMember.getEmail().getValue()), request);

        MyInfoResponse myInfo = memberService.getMyInfo(new AuthMember(savedMember.getEmail().getValue()));

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(myInfo.name()).isEqualTo("koy");
        });
    }

    @Test
    void updateNonExistMember_shouldFail() {
        // given
        MyInfoUpdateRequest request = new MyInfoUpdateRequest("doy");

        // when & then
        Assertions.assertThatThrownBy(() ->
                        memberService.updateMyInfo(MemberFixtures.nonExistMemberRequest(), request))
                .isInstanceOf(MemberException.NotFoundException.class)
                .hasMessage(String.format(
                        "해당 멤버가 존재하지 않습니다. - request info { member_email : %s}", MemberFixtures.NON_EXIST_MEMBER_EMAIL
                ));
    }

    @Test
    void deleteMember() {
        // given
        Member savedMember = builder.buildMember(MemberFixtures.member1());

        // when
        memberService.cancelMembership(MemberFixtures.member1Request());

        // then

        Assertions.assertThatThrownBy(
                () -> memberRepository.findById(savedMember.getId())
                        .orElseThrow(() -> new MemberException.NotFoundException(savedMember.getId()))
        ).isInstanceOf(MemberException.NotFoundException.class);
    }

    @Test
    void deleteNonExistMember_showFail() {
        // given & when & then
        Assertions.assertThatThrownBy(() ->
                        memberService.cancelMembership(MemberFixtures.nonExistMemberRequest()))
                .isInstanceOf(MemberException.NotFoundException.class)
                .hasMessage(String.format(
                        "해당 멤버가 존재하지 않습니다. - request info { member_email : %s}", MemberFixtures.NON_EXIST_MEMBER_EMAIL
                ));
    }

    @Test
    void findParticipatingTeams() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace1 = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        TeamPlace teamPlace2 = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace2());

        MemberTeamPlace memberTeamPlace1 = builder.buildMemberTeamPlace(member, teamPlace1);
        MemberTeamPlace memberTeamPlace2 = builder.buildMemberTeamPlace(member, teamPlace2);

        ParticipatingTeamsResponse expected = new ParticipatingTeamsResponse(List.of(
                new ParticipatingTeamResponse(memberTeamPlace1.getTeamPlace().getId(), memberTeamPlace1.getTeamPlace().getName()),
                new ParticipatingTeamResponse(memberTeamPlace2.getTeamPlace().getId(), memberTeamPlace2.getTeamPlace().getName())));

        // when
        ParticipatingTeamsResponse response = memberService.findParticipatingTeams(MemberFixtures.member1Request());

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).usingRecursiveComparison().isEqualTo(expected);
        });
    }

    @Test
    void findTeams_ofNonExistMember_shouldFail() {
        // given & when & then
        Assertions.assertThatThrownBy(() ->
                        memberService.findParticipatingTeams(MemberFixtures.nonExistMemberRequest()))
                .isInstanceOf(MemberException.NotFoundException.class)
                .hasMessage(String.format(
                        "해당 멤버가 존재하지 않습니다. - request info { member_email : %s}", MemberFixtures.NON_EXIST_MEMBER_EMAIL
                ));
    }

}
