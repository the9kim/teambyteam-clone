package web.teambyteam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web.teambyteam.member.exception.MemberException;
import web.teambyteam.member.exception.MemberTeamPlaceException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberTeamPlaceException.RegisterDuplicationException.class)
    public ResponseEntity<String> handlerException(MemberTeamPlaceException.RegisterDuplicationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(MemberException.NotFoundException.class)
    public ResponseEntity<String> notFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
