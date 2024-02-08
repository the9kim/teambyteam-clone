package web.teambyteam.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.global.AuthMember;
import web.teambyteam.member.application.dto.MyInfoResponse;
import web.teambyteam.member.application.dto.MyInfoUpdateRequest;
import web.teambyteam.member.application.dto.ParticipatingTeamsResponse;
import web.teambyteam.member.application.dto.SignUpRequest;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.domain.vo.Email;
import web.teambyteam.member.exception.MemberException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    public Long signUp(SignUpRequest signUpRequest) {

        Member member = new Member(signUpRequest.name(), signUpRequest.email(), signUpRequest.profileImageUrl());

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberException.DuplicateMemberException(member.getEmail().getValue());
        }

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    public MyInfoResponse getMyInfo(AuthMember authMember) {

        Member member = memberRepository.findByEmail(new Email(authMember.email()))
                .orElseThrow(() -> new MemberException.NotFoundException(authMember.email()));

        MyInfoResponse response = MyInfoResponse.of(member);

        return response;
    }

    /**
     * Why isn't the update query being sent when I call this method?
     */
    public void updateMyInfo(AuthMember authMember, MyInfoUpdateRequest request) {
        Member member = memberRepository.findByEmail(new Email(authMember.email()))
                .orElseThrow(() -> new MemberException.NotFoundException(authMember.email()));

        member.updateName(request.name());
    }

    public void cancelMembership(AuthMember authMember) {
        Member member = memberRepository.findByEmail(new Email(authMember.email()))
                .orElseThrow(() -> new MemberException.NotFoundException(authMember.email()));
        memberRepository.deleteById(member.getId());
    }

    public ParticipatingTeamsResponse findParticipatingTeams(AuthMember authMember) {
        Member member = memberRepository.findByEmail(new Email(authMember.email()))
                .orElseThrow(() -> new MemberException.NotFoundException(authMember.email()));

        return ParticipatingTeamsResponse.of(member.getMemberTeamPlaces());
    }
}
