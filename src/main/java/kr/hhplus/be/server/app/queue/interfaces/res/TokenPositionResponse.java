package kr.hhplus.be.server.app.queue.interfaces.res;

public record TokenPositionResponse(
        String token,
        Long position
) {
}
