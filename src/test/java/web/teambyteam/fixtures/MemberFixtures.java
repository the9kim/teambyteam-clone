package web.teambyteam.fixtures;

import web.teambyteam.global.AuthMember;
import web.teambyteam.member.domain.Member;

public class MemberFixtures {


    public static final String MEMBER1_NAME = "roy";
    public static final String MEMBER2_NAME = "hoy";

    public static final String NON_EXIST_MEMBER_EMAIL = "nonExistMember@gmail.com";
    public static final String MEMBER1_EMAIL = "roy@gmail.com";
    public static final String MEMBER2_EMAIL = "hoy@gmail.com";

    public static final String MEMBER1_PROFILE_IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Pharaoh.svg/440px-Pharaoh.svg.png";
    public static final String MEMBER2_PROFILE_IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Pharaoh.svg/440px-Pharaoh.svg.png";


    public static final String NON_EXIST_MEMBER_BASIC_AUTH = "Basic bm9uRXhpc3RNZW1iZXJAZ21haWwuY29tOjEyMzQ=";
    public static final String MEMBER1_BASIC_AUTH = "Basic cm95QGdtYWlsLmNvbToxMjM0";
    public static final String MEMBER2_BASIC_AUTH = "Basic aG95QGdtYWlsLmNvbToxMjM0";


    public static AuthMember nonExistMemberRequest() {
        return new AuthMember(NON_EXIST_MEMBER_EMAIL);
    }

    public static AuthMember member1Request() {
        return new AuthMember(MEMBER1_EMAIL);
    }

    public static AuthMember member2Request() {
        return new AuthMember(MEMBER2_EMAIL);
    }

    public static Member member1() {
        return new Member(MEMBER1_NAME, MEMBER1_EMAIL, MEMBER1_PROFILE_IMAGE_URL);
    }

    public static Member member2() {
        return new Member(MEMBER2_NAME, MEMBER2_EMAIL, MEMBER2_PROFILE_IMAGE_URL);
    }

}
