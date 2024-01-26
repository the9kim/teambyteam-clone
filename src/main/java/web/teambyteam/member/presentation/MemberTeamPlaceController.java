package web.teambyteam.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.teambyteam.member.application.MemberTeamPlaceService;
import web.teambyteam.member.application.dto.LeavingTeamRequest;
import web.teambyteam.member.application.dto.ParticipateRequest;

import java.net.URI;

@RestController
@RequestMapping("/api/member-team-place")
@RequiredArgsConstructor
public class MemberTeamPlaceController {

    private final MemberTeamPlaceService memberTeamPlaceService;

    @PostMapping
    public ResponseEntity<Void> participateTeam(@RequestBody ParticipateRequest request) {
        long memberTeamPlaceId = memberTeamPlaceService.participateTeam(request);

        URI location = URI.create("/api/member-team-place/" + memberTeamPlaceId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> leaveTeam(@RequestBody LeavingTeamRequest request) {
        memberTeamPlaceService.leaveTeam(request);

        return ResponseEntity.noContent().build();
    }
}
