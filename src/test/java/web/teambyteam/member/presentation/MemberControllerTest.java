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
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @Autowired
    MemberController memberController;

    @Autowired
    MemberRepository memberRepository;

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
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.header("location")).isEqualTo("/api/me/1");
    }

    /**
     * There is need to insert member first
     */
    @Test
    void getMyInfo() {

        // given
        Member savedMember = memberRepository.save(new Member("roy", "roy@gmail.com", "image"));

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .get("/api/me/" + savedMember.getId())
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

}
