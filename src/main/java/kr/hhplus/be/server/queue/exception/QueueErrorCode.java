package kr.hhplus.be.server.queue.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QueueErrorCode {
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다. (요청값: %s)"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 토큰 입니다. (요청값: %s)"),
    USER_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 사용자의 토큰이 존재하지 않습니다. (요청값: %s)");

    private final int code;
    private final String message;

    public String getMessageWithArgs(Object... args) {
        return String.format(message, args);
    }
}
