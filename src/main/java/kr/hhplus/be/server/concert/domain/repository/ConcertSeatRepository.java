package kr.hhplus.be.server.concert.domain.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.concert.domain.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertSeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findSeatsByConcertScheduleIdAndStatus(Long concertScheduleId, Seat.Status status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :id")
    Optional<Seat> findSeatByIdWithLock(Long id);
}
