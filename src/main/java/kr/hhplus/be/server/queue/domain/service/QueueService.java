package kr.hhplus.be.server.queue.domain.service;

import kr.hhplus.be.server.queue.domain.dto.QueueTokenPositionResult;
import kr.hhplus.be.server.queue.domain.dto.QueueTokenResult;
import kr.hhplus.be.server.queue.domain.entity.QueueToken;
import kr.hhplus.be.server.queue.domain.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueTokenRepository queueTokenRepository;

    @Transactional
    public QueueTokenResult createToken(Long userId) {
        // 1. 기존 대기열 토큰 존재 여부 확인
        QueueToken queueToken = queueTokenRepository.findFirstByUserIdAndStatusNotWithLock(userId, QueueToken.Status.EXPIRED);
        // 2. 기존 토큰 만료
        if (queueToken != null) {
            queueToken.changeStatus(QueueToken.Status.EXPIRED);
            queueTokenRepository.save(queueToken);
        }
        // 3. 토큰 발급
        String newToken = UUID.randomUUID().toString();

        QueueToken newQueueToken = new QueueToken(
                null,
                userId,
                newToken,
                QueueToken.Status.WAITING,
                LocalDateTime.now(),
                null
        );

        // 4. 토큰 저장
        queueTokenRepository.save(newQueueToken);

        return QueueTokenResult.builder()
                .userId(userId)
                .token(newToken)
                .build();
    }

    @Transactional(readOnly = true)
    public QueueTokenPositionResult getTokenPosition(String token) {
        // 1. 토큰 유효성 검증
        QueueToken queueToken = Optional.ofNullable(queueTokenRepository.findFirstByToken(token))
                .orElseThrow(() -> new RuntimeException("유효하지 않은 토큰입니다."));

        if (queueToken.getStatus() == QueueToken.Status.EXPIRED) {
            throw new RuntimeException("만료된 토큰입니다.");
        }

        boolean isAvailable = false;
        Long position = null;
        if (queueToken.getStatus() == QueueToken.Status.WAITING) {
            List<QueueToken> queueTokens = queueTokenRepository.findQueueTokenEntitiesByStatus(QueueToken.Status.WAITING);
            position = (long) queueTokens.stream()
                    .map(QueueToken::getToken)
                    .toList()
                    .indexOf(token) + 1;
        } else {
            isAvailable = true;
        }
        return QueueTokenPositionResult.builder()
                .token(token)
                .position(position)
                .isAvailable(isAvailable)
                .build();
    }
}
