package web.teambyteam.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.member.application.dto.ParticipateRequest;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.domain.MemberTeamPlace;
import web.teambyteam.member.domain.MemberTeamPlaceRepository;
import web.teambyteam.member.exception.MemberException;
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


    public long participateTeam(ParticipateRequest request) {

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MemberException.NotFoundException(request.memberId()));
        TeamPlace teamPlace = teamPlaceRepository.findById(request.teamPlaceId())
                .orElseThrow(() -> new TeamPlaceException.NotFoundException(request.teamPlaceId()));

        MemberTeamPlace savedMemberTeamPlace = memberTeamPlaceRepository.save(new MemberTeamPlace(member, teamPlace));

        member.participateTeamPlace(savedMemberTeamPlace);

        return savedMemberTeamPlace.getId();
    }
}
