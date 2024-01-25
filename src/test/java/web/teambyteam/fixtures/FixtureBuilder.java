package web.teambyteam.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.teamplace.domain.TeamPlace;
import web.teambyteam.teamplace.domain.TeamPlaceRepository;

@Component
public class FixtureBuilder {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamPlaceRepository teamPlaceRepository;

    public Member buildMember(Member member) {
        return memberRepository.save(member);
    }


    public TeamPlace buildTeamPlace(TeamPlace teamPlace) {
        return teamPlaceRepository.save(teamPlace);
    }

}
