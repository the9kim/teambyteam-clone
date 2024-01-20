package web.teambyteam.fixtures;

import web.teambyteam.member.domain.Member;

public class MemberFixtures {


    public static final String MEMBER1_NAME = "roy";

    public static final String MEMBER1_EMAIL = "roy@gamil.com";

    public static final String MEMBER1_PROFILE_IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Pharaoh.svg/440px-Pharaoh.svg.png";


    public static Member member1() {
        return new Member(MEMBER1_NAME, MEMBER1_EMAIL, MEMBER1_PROFILE_IMAGE_URL);
    }

}
