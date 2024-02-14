package web.teambyteam.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.teambyteam.member.domain.vo.Email;
import web.teambyteam.member.domain.vo.Name;
import web.teambyteam.member.domain.vo.Password;
import web.teambyteam.member.domain.vo.ProfileImageUrl;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private ProfileImageUrl profileImageUrl;

    @OneToMany(mappedBy = "member")
    private List<MemberTeamPlace> memberTeamPlaces;

    public Member(Name name, Email email, Password password, ProfileImageUrl profileImageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.memberTeamPlaces = new ArrayList<>();
    }

    public Member(String name, String email, String password, String profileImageUrl) {
        this(new Name(name), new Email(email), new Password(password), new ProfileImageUrl(profileImageUrl));

    }

    public void updateName(String name) {
        this.name.changeName(name);
    }

    public void participateTeam(MemberTeamPlace memberTeamPlace) {
        this.memberTeamPlaces.add(memberTeamPlace);
    }

    public void leaveTeamPlace(MemberTeamPlace memberTeamPlace) {
        memberTeamPlaces.remove(memberTeamPlace);
    }
    // Do I need to make getNameValue() to follow Demeter's law
}
