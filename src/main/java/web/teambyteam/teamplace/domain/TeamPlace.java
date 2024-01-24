package web.teambyteam.teamplace.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.teambyteam.teamplace.domain.vo.Name;


@Entity
@NoArgsConstructor
@Getter
public class TeamPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_place_id")
    private Long id;

    @Embedded
    private Name name;

}
