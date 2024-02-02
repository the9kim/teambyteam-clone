package web.teambyteam.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.teambyteam.member.application.MemberService;
import web.teambyteam.member.application.dto.MyInfoResponse;
import web.teambyteam.member.application.dto.MyInfoUpdateRequest;
import web.teambyteam.member.application.dto.ParticipatingTeamsResponse;
import web.teambyteam.member.application.dto.SignUpRequest;

import java.net.URI;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {

        Long memberId = memberService.signUp(request);

        URI location = URI.create("/api/me/" + memberId);

        return ResponseEntity.created(location).build();
    }


    @GetMapping("/{memberId}")
    public ResponseEntity<MyInfoResponse> getMyInfo(@PathVariable Long memberId) {

        MyInfoResponse response = memberService.getMyInfo(memberId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> updateMyIfo(@PathVariable Long memberId,
                                            @RequestBody MyInfoUpdateRequest request) {

        memberService.updateMyInfo(memberId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {

        memberService.cancelMembership(memberId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/team-places/{memberId}")
    public ResponseEntity<ParticipatingTeamsResponse> findTeamPlaces(@PathVariable Long memberId) {
        ParticipatingTeamsResponse response = memberService.findParticipatingTeams(memberId);
        return ResponseEntity.ok(response);
    }
}
