package com.joanna.staybooking.repository;

import com.joanna.staybooking.model.Location;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends ElasticsearchRepository<Location, Long>, CustomLocationRepository {
    //LocationRepository 是用spring elastic search framework 来实现 增删改查

}
