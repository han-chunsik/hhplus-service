package kr.hhplus.be.server.app.queue.interfaces.req;

import lombok.Builder;

@Builder
public record IssueQueueTokenRequest(
    Long userId
) {
}
