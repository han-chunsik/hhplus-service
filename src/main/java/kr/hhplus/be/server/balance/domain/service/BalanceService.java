package kr.hhplus.be.server.balance.domain.service;

import kr.hhplus.be.server.balance.domain.dto.BalanceChargeResult;
import kr.hhplus.be.server.balance.domain.dto.BalanceResult;
import kr.hhplus.be.server.balance.domain.model.Balance;
import kr.hhplus.be.server.balance.domain.model.BalanceHistory;
import kr.hhplus.be.server.balance.domain.repository.BalanceHistoryRepository;
import kr.hhplus.be.server.balance.domain.repository.BalanceRepository;
import kr.hhplus.be.server.balance.exception.BalanceErrorCode;
import kr.hhplus.be.server.balance.exception.BalanceException;
import kr.hhplus.be.server.common.aop.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;

    @DistributedLock(key = "'balance:' + #userId", lockType = DistributedLock.LockType.FAIR)
    @Transactional
    public BalanceChargeResult chargeBalance(long userId, long amount) {
        // 1. 사용자 잔액 조회
        Balance userBalance = balanceRepository.findFirstByUserIdWithLock(userId).orElseThrow(() -> new BalanceException(BalanceErrorCode.BALANCE_NOT_FOUND, userId));

        // 2. 잔액 충전
        Balance updateUserBalance = userBalance.charge(amount);
        balanceRepository.save(updateUserBalance);

        // 3. 잔액 충전 히스토리 저장
        BalanceHistory balanceHistory = BalanceHistory.createChargeBalanceHistory(userBalance, amount);
        balanceHistoryRepository.save(balanceHistory);

        return BalanceChargeResult.fromWithAmount(updateUserBalance, amount);
    }

    @Transactional(readOnly = true)
    public BalanceResult getBalance(long userId) {
        Balance userBalance = balanceRepository.findFirstByUserId(userId).orElseThrow(() -> new BalanceException(BalanceErrorCode.BALANCE_NOT_FOUND, userId));
        return BalanceResult.from(userBalance);
    }

    @DistributedLock(key = "'balance:' + #userId", lockType = DistributedLock.LockType.FAIR)
    @Transactional
    public BalanceChargeResult useBalance(long userId, long amount) {
        // 1. 사용자 잔액 조회
        Balance userBalance = balanceRepository.findFirstByUserIdWithLock(userId).orElseThrow(() -> new BalanceException(BalanceErrorCode.BALANCE_NOT_FOUND, userId));

        // 2. 잔액 사용
        Balance updateUserBalance = userBalance.use(amount);
        balanceRepository.save(updateUserBalance);

        // 3. 잔액 사용 히스토리 저장
        BalanceHistory balanceHistory = BalanceHistory.createUseBalanceHistory(userBalance, amount);
        balanceHistoryRepository.save(balanceHistory);

        return BalanceChargeResult.fromWithAmount(updateUserBalance, amount);
    }
}
