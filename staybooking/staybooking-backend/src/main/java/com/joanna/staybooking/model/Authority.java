package com.joanna.staybooking.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "authority")
public class Authority implements Serializable {
    private static final long serialVersionUID = 1L; //version ID,是为了做版本匹配
    // 数据库存的时候的record的version vs. 将来从数据库取出来之后要转化成一个class的object时候的version.如果不匹配，需要自己做一些修改

    @Id
    private String username;

    private String authority;   //如果一对多关系，要用组合键

    public Authority() {}  //空的constructor 是hibernate用的，取数据后要把数据转化成object返回的时候，需要先call这个constructor, 再去调用里面的set() method来获得完整的Authority对象
                            //hibernate只能用最基本的空的构造函数，然后挨个调用set()函数
    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public Authority setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getAuthority() {
        return authority;
    }

    public Authority setAuthority(String authority) {
        this.authority = authority;
        return this;
    }
    //Authority class不需要用builder,是因为数据是从controller直接传进去的,就不需要从前端注册的时候发送一个guest or host

}
