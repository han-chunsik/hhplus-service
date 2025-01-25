package kr.hhplus.be.server.reservation;

import kr.hhplus.be.server.reservation.application.ReservationTemporary;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@Slf4j
public class ReservationIntegrationOneSeatTest {
    @Autowired
    private ReservationTemporary reservationTemporary;

    @Nested
    @DisplayName("동시성 제어 통합 테스트")
    class OneSeatConcurrencyTest {
        @Test
        @DisplayName("하나의 좌석에 5명이 좌석 예약 요청 1개의 예약이 성공한다.")
        public void 같은_좌석_예약() throws Exception {
            //Given
            Long seatId = 51L;
            int threadCount = 5;

            AtomicLong successfulRequestCount = new AtomicLong(0);
            AtomicLong failedRequestCount = new AtomicLong(0);

            CountDownLatch doneSignal = new CountDownLatch(threadCount);
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

            //When
            for (int i = 101; i < 101 + threadCount; i++) {
                long finalI = i;
                executorService.execute(() -> {
                    try {
                        reservationTemporary.reserveTemporary(seatId, finalI);
                        successfulRequestCount.incrementAndGet();
                    } catch (Exception e) {
                        failedRequestCount.incrementAndGet();
                    } finally {
                        doneSignal.countDown();
                    }
                });
            }
            doneSignal.await();
            executorService.shutdown();
            assertEquals(1, successfulRequestCount.get());
            assertEquals(4, failedRequestCount.get());
        }
    }

}
