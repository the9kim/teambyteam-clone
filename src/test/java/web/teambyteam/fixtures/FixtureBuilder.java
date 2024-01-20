package web.teambyteam.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;

@Component
public class FixtureBuilder {

    @Autowired
    MemberRepository memberRepository;

    public Member buildMember(Member member) {
        return memberRepository.save(member);
    }

}
