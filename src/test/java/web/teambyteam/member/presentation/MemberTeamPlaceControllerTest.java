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
import org.springframework.test.context.jdbc.Sql;
import web.teambyteam.fixtures.FixtureBuilder;
import web.teambyteam.fixtures.MemberFixtures;
import web.teambyteam.fixtures.TeamPlaceFixtures;
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

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH)
                .post("/api/member-team-place/{teamPlaceId}", teamPlace.getId())
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });
    }

    @Test
    void failToParticipateTeamPlaceDueToDuplication() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        builder.buildMemberTeamPlace(member, teamPlace);

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH)
                .post("/api/member-team-place/{teamPlaceId}", teamPlace.getId())
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            softly.assertThat(response.body().asString()).isEqualTo(String.format("해당 팀플레이스에 이미 가입한 회원입니다. - log info { member_id : %d, team_place_id : %d}", member.getId(), teamPlace.getId()));
        });

    }

    @Test
    void leaveTeamPlace() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        MemberTeamPlace memberTeamPlace = builder.buildMemberTeamPlace(member, teamPlace);

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH)
                .delete("/api/member-team-place/{teamPlaceId}", teamPlace.getId())
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }

    @Test
    void shouldFailToLeaveNonExistTeamPlace() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH)
                .delete("/api/member-team-place/{teamPlaceId}", teamPlace.getId())
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).isEqualTo("존재하지 않는 멤버의 팀 공간입니다.");
        });

    }

}
