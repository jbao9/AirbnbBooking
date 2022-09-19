package com.joanna.staybooking.service;

import com.joanna.staybooking.exception.ReservationCollisionException;
import com.joanna.staybooking.exception.ReservationNotFoundException;
import com.joanna.staybooking.model.*;
import com.joanna.staybooking.repository.ReservationRepository;
import com.joanna.staybooking.repository.StayReservationDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private StayReservationDateRepository stayReservationDateRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, StayReservationDateRepository stayReservationDateRepository) {
        this.reservationRepository = reservationRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
    }

    public List<Reservation> listByGuest(String username) { //read in db
        return reservationRepository.findByGuest(new User.Builder().setUsername(username).build());
    }


    public List<Reservation> listByStay(Long stayId) { //read in db
        return reservationRepository.findByStay(new Stay.Builder().setId(stayId).build());
    }

    //一个transaction 就是一个原子操作，一个原子操作就是一个不可拆分的单元。从而保证数据库的数据是一致的。
    //一般需要修改多个table的时候，需要用@Transactional
    @Transactional(isolation = Isolation.SERIALIZABLE)  //Isolation.SERIALIZABLE: 隔离之串行执行
    public void add(Reservation reservation) {
        // check collision
        Set<Long> stayIds = stayReservationDateRepository.findByIdInAndDateBetween(
                Arrays.asList(reservation.getStay().getId()),
                reservation.getCheckinDate(),
                reservation.getCheckoutDate().minusDays(1)
        );
        if (!stayIds.isEmpty()) {
            throw new ReservationCollisionException("Duplicate reservation");
        }

        // save reserved date to MySQL
        List<StayReservedDate> reservedDates = new ArrayList<>();
        for (LocalDate date = reservation.getCheckinDate(); date.isBefore(reservation.getCheckoutDate()); date = date.plusDays(1)) {
            reservedDates.add(new StayReservedDate(new StayReservedDateKey(reservation.getStay().getId(), date), reservation.getStay()));
        }
        stayReservationDateRepository.saveAll(reservedDates);

        // save reservation to MySQL
        reservationRepository.save(reservation);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long reservationId, String username) {
        // Is the reservation exist
        Reservation reservation = reservationRepository.findByIdAndGuest(reservationId, new User.Builder().setUsername(username).build()); //返回一个reservation的结果
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation is not available");
        }

        // Delete reservation from MySQL
        reservationRepository.deleteById(reservationId);

        //Delete reserved date from MySQL
        for (LocalDate date = reservation.getCheckinDate(); date.isBefore(reservation.getCheckoutDate()); date = date.plusDays(1)) {
            stayReservationDateRepository.deleteById(new StayReservedDateKey(reservation.getStay().getId(), date));
        }
    }
}
