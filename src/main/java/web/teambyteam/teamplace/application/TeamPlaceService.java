package web.teambyteam.teamplace.application;

import lombok.RequiredArgsConstructor;
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
import web.teambyteam.teamplace.application.dto.TeamCreationRequest;
import web.teambyteam.teamplace.application.dto.TeamMembersResponse;
import web.teambyteam.teamplace.domain.TeamPlace;
import web.teambyteam.teamplace.domain.TeamPlaceRepository;
import web.teambyteam.teamplace.domain.vo.Name;
import web.teambyteam.teamplace.exception.TeamPlaceException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamPlaceService {

    private final TeamPlaceRepository teamPlaceRepository;
    private final MemberRepository memberRepository;
    private final MemberTeamPlaceRepository memberTeamPlaceRepository;


    public Long createTeamPlace(AuthMember authMember, TeamCreationRequest request) {

        Member member = memberRepository.findByEmail(new Email(authMember.email()))
                .orElseThrow(() -> new MemberException.NotFoundException(authMember.email()));

        TeamPlace teamPlace = teamPlaceRepository.save(new TeamPlace(new Name(request.name())));

        MemberTeamPlace savedMemberTeamPlace = memberTeamPlaceRepository.save(new MemberTeamPlace(member, teamPlace));

        member.participateTeam(savedMemberTeamPlace);

        return teamPlace.getId();
    }

    public TeamMembersResponse findTeamMembers(AuthMember authMember, Long teamPlaceId) {

        if (!teamPlaceRepository.existsById(teamPlaceId)) {
            throw new TeamPlaceException.NotFoundException(teamPlaceId);
        }

        Member member = memberRepository.findByEmail(new Email(authMember.email()))
                .orElseThrow(() -> new MemberException.NotFoundException(authMember.email()));

        List<MemberTeamPlace> memberTeamPlaces = teamPlaceRepository.findMemberTeamPlacesById(teamPlaceId);

        memberTeamPlaces.stream()
                .filter(m -> m.getMember().getId() == member.getId())
                .findAny()
                .orElseThrow(() -> new MemberTeamPlaceException.NotTeamMemberException(member.getId(), teamPlaceId));

        List<Member> members = memberTeamPlaces.stream()
                .map(MemberTeamPlace::getMember)
                .collect(Collectors.toList());


        return TeamMembersResponse.of(members, member.getId());
    }

}
