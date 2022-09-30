package com.joanna.staybooking.repository;

import com.joanna.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository           //service layer的 dependency
public interface  UserRepository extends JpaRepository<User, String> {  //Spring框架自动帮我们implement增删改查的操作
//对User model class 的 String ID type进行增删改查   CrudRepository
//如果除了default 操作，还需要自定义的操作，我们可以在interface里写这个method
}
