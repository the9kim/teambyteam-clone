package web.teambyteam.member.presentation;

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
import web.teambyteam.member.application.dto.LeavingTeamRequest;
import web.teambyteam.member.application.dto.ParticipateRequest;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberTeamPlace;
import web.teambyteam.teamplace.domain.TeamPlace;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/h2-truncate.sql")
class MemberTeamPlaceControllerTest {

    @Autowired
    FixtureBuilder builder;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void participateTeamPlace() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        ParticipateRequest request = new ParticipateRequest(member.getId(), teamPlace.getId());

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post("/api/member-team-place")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });
    }

    @Test
    void leaveTeamPlace() {
        // given
        MemberTeamPlace memberTeamPlace = builder.buildMemberTeamPlace(MemberFixtures.member1(), TeamPlaceFixtures.teamPlace1());
        LeavingTeamRequest request = new LeavingTeamRequest(memberTeamPlace.getMember().getId(), memberTeamPlace.getTeamPlace().getId());

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .delete("/api/member-team-place")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }

}
