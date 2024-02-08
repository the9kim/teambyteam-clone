package web.teambyteam.member.presentation;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
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
import web.teambyteam.member.application.dto.MyInfoUpdateRequest;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.domain.MemberTeamPlace;
import web.teambyteam.teamplace.domain.TeamPlace;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/h2-truncate.sql", executionPhase = BEFORE_TEST_METHOD)
class MemberControllerTest {

    @Autowired
    MemberController memberController;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FixtureBuilder builder;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void signUp() {
        // given
        SignUpRequest request = new SignUpRequest(
                "roy", "roy@gamil.com", "abc");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .post("/api/me")
                        .then().log().all()
                        .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });
    }

    @Test
    void shouldFailToSignUpDuplicateMember() {

        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        String duplicateEmail = "roy@gmail.com";
        SignUpRequest request = new SignUpRequest("name", duplicateEmail, "url");

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post("/api/me")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
            softly.assertThat(response.body().asString()).isEqualTo(String.format(
                    "중복된 이메일입니다. - request info {email : %s", duplicateEmail));
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
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .header(new Header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH))
                        .get("/api/me")
                        .then().log().all()
                        .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(response.body().jsonPath().getString("name")).isEqualTo(savedMember.getName().getValue());
            softly.assertThat(response.body().jsonPath().getString("email")).isEqualTo(savedMember.getEmail().getValue());
            softly.assertThat(response.body().jsonPath().getString("profileImageUrl")).isEqualTo(savedMember.getProfileImageUrl().getValue());
        });
    }

    @Test
    void shouldFailToGetNonExistMemberInfo() {
        // given & when

        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.NON_EXIST_MEMBER_BASIC_AUTH)
                .get("/api/me")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).isEqualTo(String.format(
                    "해당 멤버가 존재하지 않습니다. - request info { member_email : %s}", MemberFixtures.NON_EXIST_MEMBER_EMAIL)
            );
        });
    }

    @Test
    void updateMyInfo() {

        // given
        Member member1 = builder.buildMember(MemberFixtures.member1());
        MyInfoUpdateRequest request = new MyInfoUpdateRequest("koy");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH))
                .body(request)
                .patch("/api/me")
                .then().log().all()
                .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldFailToUpdateNonExistMember() {
        // given
        MyInfoUpdateRequest request = new MyInfoUpdateRequest("koy");

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", MemberFixtures.NON_EXIST_MEMBER_BASIC_AUTH)
                .body(request)
                .patch("/api/me")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).isEqualTo(String.format(
                    "해당 멤버가 존재하지 않습니다. - request info { member_email : %s}", MemberFixtures.NON_EXIST_MEMBER_EMAIL
            ));
        });
    }

    @Test
    void deleteMember() {
        // given
        Member member1 = builder.buildMember(MemberFixtures.member1());

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .header(new Header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH))
                        .delete("/api/me")
                        .then().log().all()
                        .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }

    @Test
    void shouldFailToDeleteNonExistMember() {
        // given
        long nonExistMemberId = -1;

        // when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.NON_EXIST_MEMBER_BASIC_AUTH)
                .delete("/api/me")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).isEqualTo(String.format(
                    "해당 멤버가 존재하지 않습니다. - request info { member_email : %s}", MemberFixtures.NON_EXIST_MEMBER_EMAIL
            ));
        });
    }

    @Test
    void findAllTeamPlaces() {
        // given
        Member member = builder.buildMember(MemberFixtures.member1());
        TeamPlace teamPlace1 = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace1());
        TeamPlace teamPlace2 = builder.buildTeamPlace(TeamPlaceFixtures.teamPlace2());

        MemberTeamPlace memberTeamPlace1 = builder.buildMemberTeamPlace(member, teamPlace1);
        MemberTeamPlace memberTeamPlace2 = builder.buildMemberTeamPlace(member, teamPlace2);


        // when
        ExtractableResponse response =
                RestAssured.given().log().all()
                        .header(new Header("authorization", MemberFixtures.MEMBER1_BASIC_AUTH))
                        .get("/api/me/team-places")
                        .then().log().all()
                        .extract();


        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(response.body().jsonPath().getList("teamPlaces.teamPlaceName.value")).contains("teambyteam", "royTeam");
        });
    }

    @Test
    void shouldFailToFindTeamPlacesOfNonExistMember() {
        // given & when
        ExtractableResponse response = RestAssured.given().log().all()
                .header("authorization", MemberFixtures.NON_EXIST_MEMBER_BASIC_AUTH)
                .get("/api/me/team-places")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).isEqualTo(String.format(
                    "해당 멤버가 존재하지 않습니다. - request info { member_email : %s}", MemberFixtures.NON_EXIST_MEMBER_EMAIL
            ));
        });
    }

}
