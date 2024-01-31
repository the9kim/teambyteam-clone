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

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
        TeamMembersRequest request = new TeamMembersRequest(teamPlace.getId(), member1.getId());

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .get("/api/team-places")
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
}
