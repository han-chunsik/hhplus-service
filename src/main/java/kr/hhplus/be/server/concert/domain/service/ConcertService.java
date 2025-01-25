package kr.hhplus.be.server.concert.domain.service;

import kr.hhplus.be.server.common.aop.lock.DistributedLock;
import kr.hhplus.be.server.concert.domain.dto.ConcertScheduleResult;
import kr.hhplus.be.server.concert.domain.dto.ConcertSeatResult;
import kr.hhplus.be.server.concert.domain.model.ConcertSchedule;
import kr.hhplus.be.server.concert.domain.model.Seat;
import kr.hhplus.be.server.concert.domain.repository.ConcertRepository;
import kr.hhplus.be.server.concert.domain.repository.ConcertScheduleRepository;
import kr.hhplus.be.server.concert.domain.repository.ConcertSeatRepository;
import kr.hhplus.be.server.concert.exception.ConcertErrorCode;
import kr.hhplus.be.server.concert.exception.ConcertException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ConcertRepository concertRepository;

    @Transactional(readOnly = true)
    public List<ConcertScheduleResult> getAvailableScheduleList(Long concertId) {
        concertRepository.findById(concertId)
                .orElseThrow(() -> new ConcertException(ConcertErrorCode.CONCERT_NOT_FOUND, concertId));

        LocalDate today = LocalDate.now();
        List<ConcertSchedule> availableConcertSchedules = concertScheduleRepository
                .findConcertSchedulesByConcertIdAndScheduleDateAfterAndStatus(
                concertId, today, ConcertSchedule.Status.AVAILABLE);

        return availableConcertSchedules.stream()
                .map(ConcertScheduleResult::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ConcertSeatResult> getAvailableSeatList(Long concertScheduleId) {
        ConcertSchedule concertSchedule = concertScheduleRepository
                .findConcertScheduleById(concertScheduleId)
                .orElseThrow(() -> new ConcertException(ConcertErrorCode.SCHEDULE_NOT_FOUND, concertScheduleId));
        concertSchedule.validateScheduleDate();

        List<Seat> availableSeats = concertSeatRepository.findSeatsByConcertScheduleIdAndStatus(
                concertScheduleId, Seat.Status.AVAILABLE);

        return availableSeats.stream()
                .map(ConcertSeatResult::from)
                .collect(Collectors.toList());
    }

    @DistributedLock(key = "'seatId:' + #seatId")
    @Transactional
    public void deactivateSeat(Long seatId) {
        Seat seat = concertSeatRepository.findSeatById(seatId)
                .orElseThrow(() -> new ConcertException(ConcertErrorCode.SEAT_NOT_FOUND, seatId));
        seat.validateSetNotAvailableSeat();
        concertSeatRepository.save(seat);
    }


    @DistributedLock(key = "'seatId:' + #seatId")
    @Transactional
    public void activateSeat(Long seatId) {
        Seat seat = concertSeatRepository.findSeatById(seatId)
                .orElseThrow(() -> new ConcertException(ConcertErrorCode.SEAT_NOT_FOUND, seatId));
        seat.setSeatAvailable();
        concertSeatRepository.save(seat);
    }

    @Transactional
    public Long getSeatPrice(Long seatId) {
        Seat seat = concertSeatRepository.findSeatById(seatId)
                .orElseThrow(() -> new ConcertException(ConcertErrorCode.SEAT_NOT_FOUND, seatId));
        return seat.getPrice();
    }

    @DistributedLock(key = "'seatId:' + #seatId")
    @Transactional
    public void activateSeatList(List<Long> seatIdList) {
        seatIdList.stream()
            .map(seatId -> concertSeatRepository.findSeatById(seatId).orElse(null))
                .filter(Objects::nonNull)
                .forEach(seat -> {seat.setSeatAvailable();
                concertSeatRepository.save(seat);
            });
    }
}
