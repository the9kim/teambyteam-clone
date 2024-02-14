package web.teambyteam.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.teambyteam.member.exception.MemberException;
import web.teambyteam.member.exception.MemberTeamPlaceException;
import web.teambyteam.teamplace.exception.TeamPlaceException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            MemberException.InvalidPasswordException.class})
    public ResponseEntity<String> badRequestException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = {
            MemberTeamPlaceException.RegisterDuplicationException.class,
            MemberException.WrongPasswordException.class})
    public ResponseEntity<String> unauthorizedException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(value = {
            MemberException.NotFoundException.class,
            TeamPlaceException.NotFoundException.class,
            MemberTeamPlaceException.NotFoundException.class})
    public ResponseEntity<String> notFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(MemberException.DuplicateMemberException.class)
    public ResponseEntity<String> conflictException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(MemberTeamPlaceException.NotTeamMemberException.class)
    public ResponseEntity<String> forbiddenException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
