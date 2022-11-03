package com.joanna.staybooking.repository;

import com.joanna.staybooking.model.Location;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;

//根据要住的经纬度，返回房源信息
public class CustomLocationRepositoryImpl implements CustomLocationRepository {
    private final String DEFAULT_DISTANCE = "5000";  //50       //搜索半径  xx公里
    private ElasticsearchOperations elasticsearchOperations;    //这是一个bean

    @Autowired
    public CustomLocationRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public List<Long> searchByDistance(double lat, double lon, String distance) {
        if (distance == null || distance.isEmpty()) {
            distance = DEFAULT_DISTANCE;
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //by default, elastic search only return top 10 result, not pageable
        // below one line of code, enable elastic search to return pageable result (more than 100 result per page).
        // 下面这行让elastic search 返回 100个结果，而不是default的 10个结果
        queryBuilder.withPageable(PageRequest.of(0, 100));
        queryBuilder.withFilter(new GeoDistanceQueryBuilder("geoPoint").point(lat, lon).distance(distance, DistanceUnit.KILOMETERS));

        SearchHits<Location> searchResult = elasticsearchOperations.search(queryBuilder.build(), Location.class);
        List<Long> locationIDs = new ArrayList<>();  //把search到的geoPoint 以一个list返回所有符合条件的stay的id
        for (SearchHit<Location> hit : searchResult.getSearchHits()) {
            locationIDs.add(hit.getContent().getId());
        }
        return locationIDs;   //stayID和locationID是同一个ID

    }

}
