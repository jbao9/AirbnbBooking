package com.joanna.staybooking.controller;

import com.joanna.staybooking.model.Reservation;
import com.joanna.staybooking.model.Stay;
import com.joanna.staybooking.model.User;
import com.joanna.staybooking.service.ReservationService;
import com.joanna.staybooking.service.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

//@RestController：Controller + Responsebody
@RestController   //Controller : annotate 成 MVC的controller, 可以接收前端穿过来的对象
public class StayController {
    private StayService stayService;
    private ReservationService reservationService;


    @Autowired
    public StayController(StayService stayService, ReservationService reservationService) {
        this.stayService = stayService;
        this.reservationService = reservationService;
    }

    //Implement the methods for all stay management APIs

    @GetMapping(value = "/stays")
    public List<Stay> listStays(Principal principal) {  //get stay by user
        return stayService.listByUser(principal.getName());
    }

    //principal 意思是 当前用户
    @GetMapping(value = "/stays/{stayId}")
    public Stay getStay(@PathVariable Long stayId, Principal principal) {  //get stay by id
        return stayService.findByIdAndHost(stayId, principal.getName());
    }

    @GetMapping(value = "/stays/reservations/{stayId}")
    public List<Reservation> listReservations(@PathVariable Long stayId) {
        return reservationService.listByStay(stayId);
    }

    @PostMapping("/stays")
    public void addStay(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("guest_number") int guestNumber,
            @RequestParam("images") MultipartFile[] images,
            Principal principal) {

        Stay stay = new Stay.Builder()
                .setName(name)
                .setAddress(address)
                .setDescription(description)
                .setGuestNumber(guestNumber)
                .setHost(new User.Builder().setUsername(principal.getName()).build())
                .build();
        stayService.add(stay, images);
    }

    @DeleteMapping("/stays/{stayId}")
    public void deleteStay(@PathVariable Long stayId, Principal principal) {
        stayService.delete(stayId, principal.getName());
    }

}
//局限性我们的Stay没有update的功能