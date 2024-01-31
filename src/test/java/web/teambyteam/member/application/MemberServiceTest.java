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
import web.teambyteam.member.application.dto.MyInfoResponse;
import web.teambyteam.member.application.dto.MyInfoUpdateRequest;
import web.teambyteam.member.application.dto.ParticipatingTeamResponse;
import web.teambyteam.member.application.dto.ParticipatingTeamsResponse;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.application.dto.SignUpResponse;
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
        Member savedMember = builder.buildMember(MemberFixtures.member1());

        // when
        MyInfoResponse response = memberService.getMyInfo(savedMember.getId());

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
        Member savedMember = builder.buildMember(MemberFixtures.member1());
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
        ParticipatingTeamsResponse response = memberService.findParticipatingTeams(memberTeamPlace2.getMember().getId());

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).usingRecursiveComparison().isEqualTo(expected);
        });
    }

}
