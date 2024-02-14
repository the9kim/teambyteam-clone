package web.teambyteam.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.teambyteam.global.AuthMember;
import web.teambyteam.global.AuthPrincipal;
import web.teambyteam.member.application.MemberTeamPlaceService;

import java.net.URI;

@RestController
@RequestMapping("/api/member-team-place")
@RequiredArgsConstructor
public class MemberTeamPlaceController {

    private final MemberTeamPlaceService memberTeamPlaceService;

    @PostMapping("/{teamPlaceId}")
    public ResponseEntity<Void> participateTeam(
            @AuthPrincipal AuthMember member,
            @PathVariable Long teamPlaceId) {
        long memberTeamPlaceId = memberTeamPlaceService.participateTeam(member, teamPlaceId);

        URI location = URI.create("/api/member-team-place/" + memberTeamPlaceId);

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{teamPlaceId}")
    public ResponseEntity<Void> leaveTeam(
            @AuthPrincipal AuthMember member,
            @PathVariable Long teamPlaceId) {
        memberTeamPlaceService.leaveTeam(member, teamPlaceId);

        return ResponseEntity.noContent().build();
    }
}
