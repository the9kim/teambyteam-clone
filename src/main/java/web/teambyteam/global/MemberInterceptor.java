package web.teambyteam.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import web.teambyteam.member.domain.Member;
import web.teambyteam.member.domain.MemberRepository;
import web.teambyteam.member.domain.vo.Email;
import web.teambyteam.member.exception.MemberException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
public class MemberInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getRequestURI().equals("/api/me") && request.getMethod().equals("POST")) {
            return true;
        }


        String authHeader = request.getHeader("authorization");

        String[] credentials = null;

        if (authHeader != null && authHeader.startsWith("Basic ")) {

            try {
                String base64Credentials = authHeader.substring(6);
                byte[] decodeBytes = Base64.getDecoder().decode(base64Credentials);
                String decodedAuth = new String(decodeBytes, StandardCharsets.UTF_8);
                credentials = decodedAuth.split(":");
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
        }

        if (credentials != null && credentials.length == 2) {
            String userEmail = credentials[0];
            String password = credentials[1];

            Member member = memberRepository.findByEmail(new Email(userEmail))
                    .orElseThrow(() -> new MemberException.NotFoundException(userEmail));

            if (!member.getPassword().getValue().equals(password)) {
                throw new MemberException.WrongPasswordException();
            }

            AuthMember authMember = new AuthMember(member.getEmail().getValue());
            request.setAttribute("authMember", authMember);

            return true;
        }

        throw new IllegalArgumentException("등록된 회원이 아닙니다.");
    }
}
