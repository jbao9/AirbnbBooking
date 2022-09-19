package com.joanna.staybooking.repository;

import com.joanna.staybooking.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//extends JpaRepository是因为：会自动生成AuthorityRepository interface implementation (JpaRepository的基本方法)
// https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
@Repository                                //第一个参数代表针对model的，第二个参数代表primary key的type
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //因为完全符合jap repository的naming convention, 所以完全继承了它的 基本方法

}
