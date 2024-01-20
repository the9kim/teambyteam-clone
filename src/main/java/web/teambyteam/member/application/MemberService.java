package web.teambyteam.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.member.application.dto.MyInfoResponse;
import web.teambyteam.member.application.dto.MyInfoUpdateRequest;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.application.dto.SignUpResponse;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.exception.MemberException;

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

    public MyInfoResponse getMyInfo(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException.NotFoundException(memberId));

        MyInfoResponse response = MyInfoResponse.of(member);

        return response;
    }

    /**
     * Why isn't the update query being sent when I call this method?
     */
    public void updateMyInfo(Long memberId, MyInfoUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException.NotFoundException(memberId));

        member.updateName(request.name());
    }

}
