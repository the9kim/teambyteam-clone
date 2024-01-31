package web.teambyteam.teamplace.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.teambyteam.teamplace.application.TeamPlaceService;
import web.teambyteam.teamplace.application.dto.TeamCreationRequest;
import web.teambyteam.teamplace.application.dto.TeamMembersRequest;
import web.teambyteam.teamplace.application.dto.TeamMembersResponse;

import java.net.URI;

@RestController
@RequestMapping("/api/team-places")
@RequiredArgsConstructor
public class TeamPlaceController {

    private final TeamPlaceService teamPlaceService;

    @PostMapping
    public ResponseEntity<Void> createTeamPlace(@RequestBody TeamCreationRequest request) {

        Long teamPlaceId = teamPlaceService.createTeamPlace(request);

        URI location = URI.create("/api/team-places/" + teamPlaceId);

        return ResponseEntity.created(location).build();
    }


    @GetMapping
    public ResponseEntity<TeamMembersResponse> findTeamMembers(@RequestBody TeamMembersRequest request) {

        TeamMembersResponse response = teamPlaceService.findTeamMembers(request);

        return ResponseEntity.ok(response);
    }

}
