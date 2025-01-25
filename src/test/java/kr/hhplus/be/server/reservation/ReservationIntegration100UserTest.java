package kr.hhplus.be.server.reservation;

import kr.hhplus.be.server.reservation.application.ReservationTemporary;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@Slf4j
public class ReservationIntegration100UserTest {
    @Autowired
    private ReservationTemporary reservationTemporary;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("동시성 제어 통합 테스트")
    class UserConcurrencyTest {
        @Test
        @DisplayName("100명의 사용자가 좌석 예약 요청 시 50개 예약이 성공한다.(한 좌석에 2명 씩 요청)")
        public void 좌석만큼_예약() throws Exception {
            //Given
            int threadCount = 100;
            List<Long> userIds = IntStream.range(1, 101).mapToLong(i -> (long) i).boxed().toList();
            List<Long> seatIds = IntStream.range(1, 51).mapToLong(i -> (long) i).boxed().toList();

            AtomicLong successfulRequestCount = new AtomicLong(0);
            AtomicLong failedRequestCount = new AtomicLong(0);

            CountDownLatch doneSignal = new CountDownLatch(threadCount);
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

            //When
            for (int i = 0; i < threadCount; i++) {
                int finalI = i;
                executorService.execute(() -> {
                    try {
                        int seatIndex = finalI / 2;
                        if (seatIndex < seatIds.size() && finalI < userIds.size()) {
                            reservationTemporary.reserveTemporary(seatIds.get(seatIndex), userIds.get(finalI));
                        }
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
            assertEquals(50, successfulRequestCount.get());
            assertEquals(50, failedRequestCount.get());
        }
    }

}
