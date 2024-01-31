package web.teambyteam.fixtures;

import web.teambyteam.teamplace.domain.TeamPlace;

public class TeamPlaceFixtures {

    public static final String TEAM1_NAME = "teambyteam";
    public static final String TEAM2_NAME = "royTeam";

    public static TeamPlace teamPlace1() {
        return new TeamPlace(TEAM1_NAME);
    }

    public static TeamPlace teamPlace2() {
        return new TeamPlace(TEAM2_NAME);
    }
}
