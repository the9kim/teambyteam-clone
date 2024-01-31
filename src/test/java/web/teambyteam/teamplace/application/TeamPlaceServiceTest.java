package web.teambyteam.teamplace.application;

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
import web.teambyteam.teamplace.application.dto.TeamCreationRequest;
import web.teambyteam.teamplace.application.dto.TeamMemberResponse;
import web.teambyteam.teamplace.application.dto.TeamMembersRequest;
import web.teambyteam.teamplace.application.dto.TeamMembersResponse;
import web.teambyteam.teamplace.domain.TeamPlace;
import web.teambyteam.teamplace.domain.TeamPlaceRepository;
import web.teambyteam.teamplace.exception.TeamPlaceException;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql("/h2-truncate.sql")
class TeamPlaceServiceTest {

    @Autowired
    TeamPlaceService teamPlaceService;

    @Autowired
    TeamPlaceRepository teamPlaceRepository;

    @Autowired
    FixtureBuilder builder;


    @Test
    void createTeam() {
        // given
        String name = "팀바팀";
        TeamCreationRequest request = new TeamCreationRequest(name);

        // when
        Long teamPlaceId = teamPlaceService.createTeamPlace(request);

        TeamPlace teamPlace = teamPlaceRepository.findById(teamPlaceId)
                .orElseThrow(() -> new TeamPlaceException.NameLengthException(20, request.name().length()));

        // then
        Assertions.assertThat(teamPlace.getName().getValue()).isEqualTo(name);
    }

    @Test
    void findTeamMembers() {
        // given
        Member member1 = builder.buildMember(MemberFixtures.member1());
        Member member2 = builder.buildMember(MemberFixtures.member2());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        builder.buildMemberTeamPlace(member1, teamPlace);
        builder.buildMemberTeamPlace(member2, teamPlace);

        TeamMembersResponse expected = new TeamMembersResponse(
                List.of(
                        new TeamMemberResponse(member1.getId(), member1.getName().getValue(), member1.getProfileImageUrl().getValue(), true),
                        new TeamMemberResponse(member2.getId(), member2.getName().getValue(), member2.getProfileImageUrl().getValue(), false)
                )
        );

        // when
        TeamMembersResponse response = teamPlaceService.findTeamMembers(new TeamMembersRequest(teamPlace.getId(), member1.getId()));

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).usingRecursiveComparison().isEqualTo(expected);
        });
    }
}
