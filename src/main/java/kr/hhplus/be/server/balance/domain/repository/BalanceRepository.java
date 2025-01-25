package kr.hhplus.be.server.balance.domain.repository;

import kr.hhplus.be.server.balance.domain.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Balance b WHERE b.userId = :userId")
    Optional<Balance> findFirstByUserIdWithLock(@Param("userId") Long userId);

    Optional<Balance> findFirstByUserId(Long userId);
}
