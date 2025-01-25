package kr.hhplus.be.server.common.aop;

import kr.hhplus.be.server.common.aop.lock.DistributedLock;
import kr.hhplus.be.server.common.aop.lock.DistributedLockKeyParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private final RedissonClient redissonClient;
    private final DistributedLockKeyParser lockKeyParser;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(distributedLock)")
    public Object handleDistributedLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockKey = lockKeyParser.parseLockKey(joinPoint, distributedLock.key());
        TimeUnit timeUnit = distributedLock.timeUnit();
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();

        RLock lock = null;
        if (distributedLock.lockType() == DistributedLock.LockType.NORMAL) {
            lock = redissonClient.getLock(lockKey);
        }

        if (distributedLock.lockType() == DistributedLock.LockType.FAIR){
            lock = redissonClient.getFairLock(lockKey);
        }

        boolean isLocked = lock.tryLock(waitTime, leaseTime, timeUnit);
        if (!isLocked) {
            throw new IllegalStateException("현재 작업 중이므로 요청을 처리할 수 없습니다.");
        }

        try {
            // return aopForTransaction.proceed(joinPoint);
            return joinPoint.proceed();
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
