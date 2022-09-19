package com.joanna.staybooking.repository;

import com.joanna.staybooking.model.Stay;
import com.joanna.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {
    //默认是按照primary key搜索，如果不是，需要自己写
    List<Stay> findByHost(User user);  //不能user-id,因为在stay class里定义的是User

    Stay findByIdAndHost(Long id, User host);

    //找房源的 guest # 比 用户输入guest人数多的，返回一个集合
    List<Stay> findByIdInAndGuestNumberGreaterThanEqual(List<Long> ids, int guestNumber);

}
