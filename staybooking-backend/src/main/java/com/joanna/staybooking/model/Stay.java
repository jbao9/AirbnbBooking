package com.joanna.staybooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity     //表示是个table
@Table(name = "stay")    //代表对应的是database里的stay table
@JsonDeserialize(builder = Stay.Builder.class)  //用builder pattern创建对象
//@JsonDeserialize makes sure the Jackson library will use the Builder class to convert JSON format data to the Stay object
public class Stay implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String address;

    //@JsonProperty makes sure to map guestNumber field to the guest_number key in JSON format data
    @JsonProperty("guest_number")
    private int guestNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User host;

                                    //CascadeType.ALL: 当save stay时，hibernate会自动把stay对应的image存储过来
    @OneToMany(mappedBy = "stay", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private List<StayImage> images;

    @JsonIgnore   //1.不让前端返回敏感数据。2.如果不写，数据返回到前端会crash。方式死循环
    @OneToMany(mappedBy = "stay", cascade = CascadeType.ALL, fetch=FetchType.LAZY) //cascade = CascadeType.ALL 即支持删除也支持插入
    private List<StayReservedDate> reservedDates;

    public Stay() {}

    private Stay(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.address = builder.address;
        this.guestNumber = builder.guestNumber;
        this.host = builder.host;
        this.reservedDates = builder.reservedDates;
        this.images = builder.images;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public User getHost() {
        return host;
    }

    public List<StayReservedDate> getReservedDates() {
        return reservedDates;
    }

    public List<StayImage> getImages() {
        return images;
    }

    public Stay setImages(List<StayImage> images) {
        this.images = images;
        return this;
    }

    public static class Builder {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("address")
        private String address;

        @JsonProperty("guest_number")
        private int guestNumber;

        @JsonProperty("host")
        private User host;

        @JsonProperty("dates")
        private List<StayReservedDate> reservedDates;

        @JsonProperty("images")
        private List<StayImage> images;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setGuestNumber(int guestNumber) {
            this.guestNumber = guestNumber;
            return this;
        }

        public Builder setHost(User host) {
            this.host = host;
            return this;
        }

        public Builder setReservedDates(List<StayReservedDate> reservedDates) {
            this.reservedDates = reservedDates;
            return this;
        }

        public Builder setImages(List<StayImage> images) {
            this.images = images;
            return this;
        }

        public Stay build() {
            return new Stay(this);
        }
    }
}
