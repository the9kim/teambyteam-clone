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
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberTeamPlace;
import web.teambyteam.member.domain.MemberTeamPlaceRepository;
import web.teambyteam.member.exception.MemberTeamPlaceException;
import web.teambyteam.teamplace.domain.TeamPlace;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("/h2-truncate.sql")
class MemberTeamPlaceServiceTest {

    @Autowired
    MemberTeamPlaceService memberTeamPlaceService;

    @Autowired
    MemberTeamPlaceRepository memberTeamPlaceRepository;

    @Autowired
    FixtureBuilder builder;

    @Test
    void participateTeamPlace() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());

        // when
        Long memberTeamPlaceId = memberTeamPlaceService.participateTeam(MemberFixtures.member1Request(), teamPlace.getId());

        MemberTeamPlace memberTeamPlace = memberTeamPlaceRepository.findById(memberTeamPlaceId).get();

        // then
        SoftAssertions.assertSoftly(softly ->
                softly.assertThat(member.getMemberTeamPlaces()).contains(memberTeamPlace));
    }

    @Test
    void failToParticipateTeamWithDuplication() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        builder.buildMemberTeamPlace(member, teamPlace);

        // when & then
        Assertions.assertThatThrownBy(() -> memberTeamPlaceService.participateTeam(MemberFixtures.member1Request(), teamPlace.getId()))
                .isInstanceOf(MemberTeamPlaceException.RegisterDuplicationException.class)
                .hasMessage(String.format("해당 팀플레이스에 이미 가입한 회원입니다. - log info { member_id : %d, team_place_id : %d}", member.getId(), teamPlace.getId()));
    }

    @Test
    void leaveTeamPlace() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        MemberTeamPlace memberTeamPlace = builder.buildMemberTeamPlace(member, teamPlace);

        // when
        memberTeamPlaceService.leaveTeam(MemberFixtures.member1Request(), teamPlace.getId());

        System.out.println(memberTeamPlace.getId());

        // then
        Assertions.assertThatThrownBy(
                () -> memberTeamPlaceRepository.findById(memberTeamPlace.getId())
                        .orElseThrow(() -> new MemberTeamPlaceException.NotFoundException())
        ).isInstanceOf(MemberTeamPlaceException.NotFoundException.class);
    }

    @Test
    void leaveNonExistMemberTeamPlace_shouldFail() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());

        // when & then
        Assertions.assertThatThrownBy(() -> memberTeamPlaceService.leaveTeam(MemberFixtures.member1Request(), teamPlace.getId()))
                .isInstanceOf(MemberTeamPlaceException.NotFoundException.class)
                .hasMessage("존재하지 않는 멤버의 팀 공간입니다.");
    }
}
