package com.joanna.staybooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity   //把表格通过jpa persistence api映射到MySQL database里了
@Table(name = "user")
@JsonDeserialize(builder = User.Builder.class)  //通过builder把JSON user转化成了一个object，using jackson library
public class User implements Serializable { //网络传输使用Serializable，把字节流从本地发送给网络上其他service

    //https://www.baeldung.com/java-serial-version-uid
    //用来做version check
    //数据库存的时候的record的version vs. 将来从数据库取出来之后要转化成一个class的object时候的version.如果不匹配，需要自己做一些修改

    private static final long serialVersionUID = 1L;
    @Id
    private String username;

    @JsonIgnore     //ignore后，service code不会返回这些信息
    private String password;

    @JsonIgnore
    private boolean enabled;

    //空的constructor:在hibernate从数据库里load完数据后，在通过set()函数赋值空的User对应的fields,从而得到了一个完整的User object
    public User() {}

    //JSON生成User object时候用的constructor,在前后端传数据的时候调用的
    private User(Builder builder) { //设成private，是专门给Builder 用的
        this.username = builder.username;
        this.password = builder.password;
        this.enabled = builder.enabled;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    //建造者模式（Builder Pattern）使用多个简单的对象一步一步构建成一个复杂的对象
    //通过builder类，在register时候的把JSON对应的数据的key读出来后，赋值到对应的fields上
    public static class Builder {
        @JsonProperty("username")  //deserialization 反序列化 把Json convert成 object
        private String username;

        @JsonProperty("password")
        private String password;

        @JsonProperty("enabled")
        private boolean enabled;

        public Builder setUsername(String username) { //通过Builder class来创建User object
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        //通过build() method，generate出User class
        public User build() {
            return new User(this);
        }
    }
}
