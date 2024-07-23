package com.bjcareer.stockservice.timeDeal.service;

import com.bjcareer.stockservice.timeDeal.domain.Coupon;
import com.bjcareer.stockservice.timeDeal.domain.TimeDealEvent;
import com.bjcareer.stockservice.timeDeal.repository.CouponRepository;
import com.bjcareer.stockservice.timeDeal.repository.EventRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeDealService {
    private final CouponRepository couponRepository;
    private final EventRepository timeDealEventRepository;

    @Transactional
    public TimeDealEvent createTimeDealEvent(int publishedCouponNum){
        log.debug("타임딜 서비스 시작");

        TimeDealEvent timeDealEvent = new TimeDealEvent(publishedCouponNum);
        Long saveId = timeDealEventRepository.save(timeDealEvent);
        log.debug("저장된 ID는 {}", saveId);

        return timeDealEvent;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Coupon generateCouponToUser(Long eventId, Double discountRate){
        TimeDealEvent timeDealEvent = timeDealEventRepository.findById(eventId);
        log.debug("사용자에게 전달된 쿠폰의 개수는 = {} ", timeDealEvent.getDeliveredCouponNum());

        if(timeDealEvent.getPublishedCouponNum() <= timeDealEvent.getDeliveredCouponNum()){
            log.debug("더 이상 발급 불가");
            throw new IllegalStateException("더 이상 발급하지 못함");
        }

        timeDealEvent.updateDeliveredCouponNum();

        Coupon coupon = new Coupon(discountRate, timeDealEvent);
        String saveId = couponRepository.save(coupon);
        log.debug("발급된 쿠폰 ID는 {}", saveId);

        return coupon;
    }
}
