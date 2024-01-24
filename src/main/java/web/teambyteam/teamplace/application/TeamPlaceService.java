package web.teambyteam.teamplace.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.teamplace.domain.TeamPlace;
import web.teambyteam.teamplace.domain.TeamPlaceRepository;
import web.teambyteam.teamplace.domain.vo.Name;
import web.teambyteam.teamplace.dto.TeamCreationRequest;

@Service
@Transactional
@AllArgsConstructor
public class TeamPlaceService {

    private final TeamPlaceRepository teamPlaceRepository;

    public Long createTeamPlace(TeamCreationRequest request) {

        TeamPlace teamPlace = teamPlaceRepository.save(new TeamPlace(new Name(request.name())));

        return teamPlaceRepository.save(teamPlace).getId();
    }

}
