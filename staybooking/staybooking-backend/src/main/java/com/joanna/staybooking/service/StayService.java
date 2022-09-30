package com.joanna.staybooking.service;

import com.joanna.staybooking.exception.StayDeleteException;
import com.joanna.staybooking.exception.StayNotExistException;
import com.joanna.staybooking.model.*;
import com.joanna.staybooking.repository.LocationRepository;
import com.joanna.staybooking.repository.ReservationRepository;
import com.joanna.staybooking.repository.StayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service    //激活 inversion of control, spring帮助创建对象，可以支持@Autowired
public class StayService {
    private StayRepository stayRepository;
    private LocationRepository locationRepository;
    private ReservationRepository reservationRepository;
    private ImageStorageService imageStorageService;
    private GeoCodingService geoCodingService;

    @Autowired
    public StayService(StayRepository stayRepository, LocationRepository locationRepository,
                       ReservationRepository reservationRepository,
                       ImageStorageService imageStorageService, GeoCodingService geoCodingService) {
        this.stayRepository = stayRepository;
        this.locationRepository = locationRepository;
        this.reservationRepository = reservationRepository;
        this.imageStorageService = imageStorageService;
        this.geoCodingService = geoCodingService;
    }


    //Implement the methods for stay save, delete by id, list by the user and get by id
    public List<Stay> listByUser(String username) {
        ////从stayRepository读取数据         //创建User 对象
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    public Stay findByIdAndHost(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        return stay;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Stay stay, MultipartFile[] images) {
        //upload files, return urls        //stream(images).parallel() 并行处理array里对应的元素
        List<String> mediaLinks = Arrays.stream(images).parallel().map(image -> imageStorageService.save(image)).collect(Collectors.toList());
        //下面时比较native的写法，没有并行上传的功能，属于串行操作
//        List<String> imageUrls = new ArrayList();
//        for (MultipartFile image: images) {
//            String url = imageStorageService.save(image);
//            imageUrls.add(url);
//        }

        //create stayimage objects, save to db
        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }

        stay.setImages(stayImages);
        stayRepository.save(stay);

        Location location = geoCodingService.getLatLng(stay.getId(), stay.getAddress());
        locationRepository.save(location);  //存进elastic search
    }


    //原子操纵，当一个操作涉及到多个table,确保成功都成功，失败都失败  //删除stay, 它相关的日期、image等都要被删掉
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long stayId, String username) throws StayNotExistException {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        List<Reservation> reservations = reservationRepository.findByStayAndCheckoutDateAfter(stay, LocalDate.now());
        if (reservations != null && reservations.size() > 0) {
            throw new StayDeleteException("Cannot delete stay with active reservation");
        }
        stayRepository.deleteById(stayId);
    }
}
