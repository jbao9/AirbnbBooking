package com.joanna.staybooking.repository;

import com.joanna.staybooking.model.Reservation;
import com.joanna.staybooking.model.Stay;
import com.joanna.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository                                     //让JPA 帮我们实现数据库增删改查
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuest(User guest);

    List<Reservation> findByStay(Stay stay);

    //根据guest 信息，id信息 返回reservation   //如果有reservation, stay就不能被删除
    Reservation findByIdAndGuest(Long id, User guest); // for deletion

    List<Reservation> findByStayAndCheckoutDateAfter(Stay stay, LocalDate date);


}
