package com.joanna.staybooking.service;

import com.joanna.staybooking.model.Stay;
import com.joanna.staybooking.repository.LocationRepository;
import com.joanna.staybooking.repository.StayRepository;
import com.joanna.staybooking.repository.StayReservationDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {
    private StayRepository stayRepository;
    private StayReservationDateRepository stayReservationDateRepository;
    private LocationRepository locationRepository;

    @Autowired
    public SearchService(StayRepository stayRepository, StayReservationDateRepository stayReservationDateRepository, LocationRepository locationRepository) {
        this.stayRepository = stayRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
        this.locationRepository = locationRepository;
    }

    public List<Stay> search(int guestNumber, LocalDate checkinDate, LocalDate checkoutDate, double lat, double lon, String distance) {

        List<Long> stayIds = locationRepository.searchByDistance(lat, lon, distance); //从elastic search 按照经纬度搜索
        if (stayIds == null || stayIds.isEmpty()) {
            return new ArrayList<>();
        }
                                                                                                    // -1的意思是 checkout那天 其他人可以checkin
        Set<Long> reservedStayIds = stayReservationDateRepository.findByIdInAndDateBetween(stayIds, checkinDate, checkoutDate.minusDays(1)); //找到在搜索时间区间内已经被预定过的房子的集合

        List<Long> filteredStayIds = new ArrayList<>();
        for (Long stayId : stayIds) {   //把已经被预定过的房源，筛除掉
            if (!reservedStayIds.contains(stayId)) {
                filteredStayIds.add(stayId);
            }
        }
        return stayRepository.findByIdInAndGuestNumberGreaterThanEqual(filteredStayIds, guestNumber);
    }
}
