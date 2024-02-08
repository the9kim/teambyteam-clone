package web.teambyteam.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.global.AuthMember;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.domain.MemberTeamPlace;
import web.teambyteam.member.domain.MemberTeamPlaceRepository;
import web.teambyteam.member.domain.vo.Email;
import web.teambyteam.member.exception.MemberException;
import web.teambyteam.member.exception.MemberTeamPlaceException;
import web.teambyteam.teamplace.domain.TeamPlace;
import web.teambyteam.teamplace.domain.TeamPlaceRepository;
import web.teambyteam.teamplace.exception.TeamPlaceException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberTeamPlaceService {

    @Autowired
    private final MemberTeamPlaceRepository memberTeamPlaceRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final TeamPlaceRepository teamPlaceRepository;


    public Long participateTeam(AuthMember authMember, Long teamPlaceId) {

        Member member = memberRepository.findByEmail(new Email(authMember.email()))
                .orElseThrow(() -> new MemberException.NotFoundException(authMember.email()));
        TeamPlace teamPlace = teamPlaceRepository.findById(teamPlaceId)
                .orElseThrow(() -> new TeamPlaceException.NotFoundException(teamPlaceId));

        if (memberTeamPlaceRepository.existsByMemberIdAndTeamPlaceId(member.getId(), teamPlaceId)) {
            throw new MemberTeamPlaceException.RegisterDuplicationException(member.getId(), teamPlaceId);
        }

        MemberTeamPlace savedMemberTeamPlace = memberTeamPlaceRepository.save(new MemberTeamPlace(member, teamPlace));

        member.participateTeam(savedMemberTeamPlace);

        return savedMemberTeamPlace.getId();
    }

    public void leaveTeam(AuthMember authMember, Long teamPlaceId) {
        Member member = memberRepository.findByEmail(new Email(authMember.email()))
                .orElseThrow(() -> new MemberException.NotFoundException(authMember.email()));

        MemberTeamPlace memberTeamPlace = memberTeamPlaceRepository.findByMemberIdAndTeamPlaceId(member.getId(), teamPlaceId)
                .orElseThrow(() -> new MemberTeamPlaceException.NotFoundException());
        Member leavingMember = memberTeamPlace.getMember();
        leavingMember.leaveTeamPlace(memberTeamPlace);

        memberTeamPlaceRepository.deleteById(memberTeamPlace.getId());
    }
}
