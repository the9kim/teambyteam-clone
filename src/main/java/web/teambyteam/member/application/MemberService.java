package web.teambyteam.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.application.dto.SignUpResponse;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        Member member = new Member(signUpRequest.name(), signUpRequest.email(), signUpRequest.profileImageUrl());

        Member savedMember = memberRepository.save(member);

        return new SignUpResponse(savedMember.getId());
    }
}
