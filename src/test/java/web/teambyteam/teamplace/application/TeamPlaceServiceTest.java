package web.teambyteam.teamplace.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.fixtures.FixtureBuilder;
import web.teambyteam.teamplace.domain.TeamPlace;
import web.teambyteam.teamplace.domain.TeamPlaceRepository;
import web.teambyteam.teamplace.dto.TeamCreationRequest;
import web.teambyteam.teamplace.exception.TeamPlaceException;

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
}
