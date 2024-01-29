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
import web.teambyteam.member.application.dto.LeavingTeamRequest;
import web.teambyteam.member.application.dto.ParticipateRequest;
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
        ParticipateRequest request = new ParticipateRequest(member.getId(), teamPlace.getId());

        // when
        Long memberTeamPlaceId = memberTeamPlaceService.participateTeam(request);

        MemberTeamPlace memberTeamPlace = memberTeamPlaceRepository.findById(memberTeamPlaceId).get();

        // then
        SoftAssertions.assertSoftly(softly ->
                softly.assertThat(member.getMemberTeamPlaces()).contains(memberTeamPlace));
    }

    @Test
    void leaveTeamPlace() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        MemberTeamPlace memberTeamPlace = builder.buildMemberTeamPlace(member, teamPlace);
        LeavingTeamRequest request = new LeavingTeamRequest(memberTeamPlace.getMember().getId(), memberTeamPlace.getTeamPlace().getId());

        // when
        memberTeamPlaceService.leaveTeam(request);

        System.out.println(memberTeamPlace.getId());

        // then
        Assertions.assertThatThrownBy(
                () -> memberTeamPlaceRepository.findById(memberTeamPlace.getId())
                        .orElseThrow(() -> new MemberTeamPlaceException.NotFoundException())
        ).isInstanceOf(MemberTeamPlaceException.NotFoundException.class);
    }

}
