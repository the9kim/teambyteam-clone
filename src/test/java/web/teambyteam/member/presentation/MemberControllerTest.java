package web.teambyteam.member.presentation;

import io.restassured.RestAssured;
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
import web.teambyteam.member.application.dto.MyInfoUpdateRequest;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/h2-truncate.sql", executionPhase = BEFORE_TEST_METHOD)
class MemberControllerTest {

    @Autowired
    MemberController memberController;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FixtureBuilder fixturebuilder;

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

    /**
     * There is need to insert member first
     */
    @Test
    void getMyInfo() {

        // given
        Member savedMember = fixturebuilder.buildMember(MemberFixtures.member1());

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .get("/api/me/{memberId}", savedMember.getId())
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
    void updateMyInfo() {

        // given
        Member savedMember = fixturebuilder.buildMember(MemberFixtures.member1());
        MyInfoUpdateRequest request = new MyInfoUpdateRequest("koy");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .patch("/api/me/{memberId}", savedMember.getId())
                .then().log().all()
                .extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void deleteMember() {
        // given
        Member savedMember = fixturebuilder.buildMember(MemberFixtures.member1());

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                .delete("/api/me/{memberId}", savedMember.getId())
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }

}
