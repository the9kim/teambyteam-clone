package web.teambyteam.teamplace.presentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import web.teambyteam.fixtures.FixtureBuilder;
import web.teambyteam.fixtures.MemberFixtures;
import web.teambyteam.fixtures.TeamPlaceFixtures;
import web.teambyteam.member.domain.Member;
import web.teambyteam.teamplace.application.dto.TeamCreationRequest;
import web.teambyteam.teamplace.application.dto.TeamMembersRequest;
import web.teambyteam.teamplace.domain.TeamPlace;
import web.teambyteam.teamplace.domain.TeamPlaceRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/h2-truncate.sql")
class TeamPlaceControllerTest {

    @Autowired
    TeamPlaceRepository teamPlaceRepository;

    @Autowired
    FixtureBuilder builder;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void createTeam() {
        // given
        TeamCreationRequest request = new TeamCreationRequest("팀바팀");
        Member member1 = builder.buildMember(MemberFixtures.member1());

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH)
                .body(request)
                .post("/api/team-places")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });
    }

    @Test
    void findTeamMembers() {
        // given
        Member member1 = builder.buildMember(MemberFixtures.member1());
        Member member2 = builder.buildMember(MemberFixtures.member2());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        builder.buildMemberTeamPlace(member1, teamPlace);
        builder.buildMemberTeamPlace(member2, teamPlace);

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH)
                .get("/api/team-places/{teamPlaceId}", teamPlace.getId())
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(response.body().jsonPath().getString("members[0].name")).contains("roy");
                    softly.assertThat(response.body().jsonPath().getString("members[0].isMe")).contains("true");
                    softly.assertThat(response.body().jsonPath().getString("members[1].name")).contains("hoy");
                    softly.assertThat(response.body().jsonPath().getString("members[1].isMe")).contains("false");
                }
        );
    }

    @Test
    void shouldFailToFindMembersOfNonExistTeam() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        long nonExistTeamPlaceId = -1;
        TeamMembersRequest request = new TeamMembersRequest(nonExistTeamPlaceId, member.getId());

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH)
                .get("/api/team-places/{teamPlaceId}", nonExistTeamPlaceId)
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).isEqualTo(String.format(
                    "존재하지 않는 팀플레이스 입니다. - request info { team_place_id : %d }", nonExistTeamPlaceId
            ));
        });
    }

    @Test
    void shouldFailToFindMembersWithRequestOfNonTeamMember() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        builder.buildMemberTeamPlace(member, teamPlace);

        Member nonTeamMember = builder.buildMember(MemberFixtures.member2());
        TeamMembersRequest request = new TeamMembersRequest(teamPlace.getId(), nonTeamMember.getId());

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.MEMBER2_BASIC_AUTH)
                .get("/api/team-places/{teamPlaceId}", teamPlace.getId())
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
            softly.assertThat(response.body().asString()).isEqualTo(String.format("해당 팀플레이스에 소속된 멤버가 아닙니다. - request info {memberId : %d, teamPlaceId : %d]",
                    nonTeamMember.getId(), teamPlace.getId()));
        });
    }
}
