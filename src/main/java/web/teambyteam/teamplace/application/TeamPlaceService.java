package web.teambyteam.teamplace.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberTeamPlace;
import web.teambyteam.teamplace.application.dto.TeamMembersRequest;
import web.teambyteam.teamplace.domain.TeamPlace;
import web.teambyteam.teamplace.domain.TeamPlaceRepository;
import web.teambyteam.teamplace.domain.vo.Name;
import web.teambyteam.teamplace.application.dto.TeamCreationRequest;
import web.teambyteam.teamplace.application.dto.TeamMembersResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamPlaceService {

    private final TeamPlaceRepository teamPlaceRepository;

    public Long createTeamPlace(TeamCreationRequest request) {

        TeamPlace teamPlace = teamPlaceRepository.save(new TeamPlace(new Name(request.name())));

        return teamPlaceRepository.save(teamPlace).getId();
    }

    public TeamMembersResponse findTeamMembers(TeamMembersRequest request) {

        List<MemberTeamPlace> memberTeamPlaces = teamPlaceRepository.findMemberTeamPlacesById(request.teamPlaceId());
        List<Member> members = memberTeamPlaces.stream()
                .map(MemberTeamPlace::getMember)
                .collect(Collectors.toList());

        return TeamMembersResponse.of(members, request.memberId());
    }

}
