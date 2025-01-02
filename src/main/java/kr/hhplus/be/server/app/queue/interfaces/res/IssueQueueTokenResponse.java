package kr.hhplus.be.server.app.queue.interfaces.res;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IssueQueueTokenResponse(
    Long id,
    Long userId,
    String token,
    String tokenStatus,
    LocalDateTime createdAt
) {
}
