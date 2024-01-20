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
import web.teambyteam.member.domain.vo.ProfileImageUrl;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Email email;

    @Embedded
    private ProfileImageUrl profileImageUrl;

    @Column
    @OneToMany
    private List<MemberTeamPlace> memberTeamPlace;

    public Member(Name name, Email email, ProfileImageUrl profileImageUrl) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.memberTeamPlace = new ArrayList<>();
    }

    public Member(String name, String email, String profileImageUrl) {
        this(new Name(name), new Email(email), new ProfileImageUrl(profileImageUrl));

    }

    public void updateName(String name) {
        this.name.changeName(name);
    }
    // Do I need to make getNameValue() to follow Demeter's law
}
