package web.teambyteam.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.teambyteam.global.AuthMember;
import web.teambyteam.global.AuthPrincipal;
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

    @PostMapping("/log-in")
    public ResponseEntity<Void> login(@AuthPrincipal AuthMember member) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MyInfoResponse> getMyInfo(@AuthPrincipal AuthMember member) {

        MyInfoResponse response = memberService.getMyInfo(member);

        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<Void> updateMyIfo(@AuthPrincipal AuthMember member,
                                            @RequestBody MyInfoUpdateRequest request) {

        memberService.updateMyInfo(member, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@AuthPrincipal AuthMember member) {

        memberService.cancelMembership(member);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/team-places")
    public ResponseEntity<ParticipatingTeamsResponse> findTeamPlaces(@AuthPrincipal AuthMember member) {
        ParticipatingTeamsResponse response = memberService.findParticipatingTeams(member);
        return ResponseEntity.ok(response);
    }
}
