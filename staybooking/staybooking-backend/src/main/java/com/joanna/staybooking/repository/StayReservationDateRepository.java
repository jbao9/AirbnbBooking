package com.joanna.staybooking.repository;

import com.joanna.staybooking.model.StayReservedDate;
import com.joanna.staybooking.model.StayReservedDateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

//要预定时间段内哪些房子已经被预定了，在SearchService 里要删掉它们
@Repository
public interface StayReservationDateRepository  extends JpaRepository<StayReservedDate, StayReservedDateKey> {
    @Query(value = "SELECT srd.id.stay_id FROM StayReservedDate srd WHERE srd.id.stay_id IN ?1 AND srd.id.date BETWEEN ?2 AND ?3 GROUP BY srd.id.stay_id")
            //stay in in 一个参数List<Long> stayIds, AND date BETWEEN startDate AND endDate
    //?1 代表第一个参数  ?2 代表第二个参数
    Set<Long> findByIdInAndDateBetween(List<Long> stayIds, LocalDate startDate, LocalDate endDate);
}       //从elasticsearch返回的id集合里搜索 并且 在某个日期区间里

