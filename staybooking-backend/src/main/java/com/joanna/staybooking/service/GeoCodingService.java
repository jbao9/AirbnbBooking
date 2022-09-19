package com.joanna.staybooking.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.joanna.staybooking.exception.GeoCodingException;
import com.joanna.staybooking.exception.InvalidStayAddressException;
import com.joanna.staybooking.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoCodingService {
    private GeoApiContext context;

    @Autowired
    public GeoCodingService(GeoApiContext context) {
        this.context = context;
    }

    public Location getLatLng(Long id, String address) throws GeoCodingException {
        try {
            GeocodingResult result = GeocodingApi.geocode(context, address).await()[0]; //[0]把第0个元素拿出来
            if (result.partialMatch) { //.partialMatch 只match到部分地址 //如果第一个都是partial match,后面肯定都是partial match
                throw new InvalidStayAddressException("Failed to find stay address");
            }
            return new Location(id, new GeoPoint(result.geometry.location.lat, result.geometry.location.lng));
        } catch (IOException | ApiException | InterruptedException e) {
            e.printStackTrace();
            throw new GeoCodingException("Failed to encode stay address");
        }
    }
}
