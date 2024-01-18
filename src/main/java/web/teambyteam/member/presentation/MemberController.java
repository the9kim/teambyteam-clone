package web.teambyteam.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.teambyteam.member.application.MemberService;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.application.dto.SignUpResponse;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {

        SignUpResponse response = memberService.signUp(request);

        return ResponseEntity.ok(response);
    }
}
