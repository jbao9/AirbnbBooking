package com.joanna.staybooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@JsonDeserialize(builder = Reservation.Builder.class)  //在解析JSON数据时(json 变成 object)
// POJO class（没有任何逻辑功能的class,更像是保存数据的对象）会被应用到各种各样的数据传输的流程中。如果是定义的restful api,一定会被序列化成json
// POJO class很可能会被serialization, deserialization用，一般都需要实现Serializable interface, 并且有一个版本好
public class Reservation implements Serializable {  //serialization 序列化:java object变JSON    //deserialization 反序列化：JSON format数据 变Java object
    private static final long serialVersionUID = 1L;  //1L 是class的版本号。L代表Long type. 如果后面程序要变，就写成L2.要保证L2版本能兼容L1版本

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("checkin_date")
    private LocalDate checkinDate;

    @JsonProperty("checkout_date")
    private LocalDate checkoutDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User guest;

    @ManyToOne
    @JoinColumn(name = "stay_id")
    private Stay stay;

    public Reservation() {
    } //hibernate 用。hibernate 把数据库里的记录转化成对象

    private Reservation(Builder builder) {
        this.id = builder.id;
        this.checkinDate = builder.checkinDate;
        this.checkoutDate = builder.checkoutDate;
        this.guest = builder.guest;
        this.stay = builder.stay;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public User getGuest() {
        return guest;
    }

    public Reservation setGuest(User guest) {
        this.guest = guest;
        return this;
    }

    public Stay getStay() {
        return stay;
    }

    public static class Builder {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("checkin_date")
        private LocalDate checkinDate;

        @JsonProperty("checkout_date")
        private LocalDate checkoutDate;

        @JsonProperty("guest")
        private User guest;

        @JsonProperty("stay")
        private Stay stay;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setCheckinDate(LocalDate checkinDate) {
            this.checkinDate = checkinDate;
            return this;
        }

        public Builder setCheckoutDate(LocalDate checkoutDate) {
            this.checkoutDate = checkoutDate;
            return this;
        }

        public Builder setGuest(User guest) {
            this.guest = guest;
            return this;
        }

        public Builder setStay(Stay stay) {
            this.stay = stay;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }

}
