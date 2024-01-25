package web.teambyteam.fixtures;

import web.teambyteam.teamplace.domain.TeamPlace;

public class TeamPlaceFixtures {

    public static final String TEAM1_NAME = "teambyteam";

    public static TeamPlace teamPlace1() {
        return new TeamPlace(TEAM1_NAME);
    }
}
