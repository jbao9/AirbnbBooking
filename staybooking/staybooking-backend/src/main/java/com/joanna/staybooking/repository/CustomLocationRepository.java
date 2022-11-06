package com.joanna.staybooking.repository;

import java.util.List;

public interface CustomLocationRepository {
    List<Long> searchByDistance(double lat, double lon, String distance); //searchByDistance需要自己实现，因为elastic search没有这个实现
}
