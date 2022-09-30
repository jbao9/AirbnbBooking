package com.joanna.staybooking.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stay_reserved_date")
public class StayReservedDate implements Serializable {  //这个table里有3个column:stay_id-date，date, stay
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private StayReservedDateKey id;

    // column stay_id also is a forenign key of table stay
    @MapsId("stay_id")   //"stay_id" column 做为foreign key, 指向stay table, 让hibernate不要创建额外的foreign key
    @ManyToOne
    private Stay stay;  //实现多对一的效果     多对一(n to 1)的关系，foreign key在n的一侧

    public StayReservedDate() {}

    public StayReservedDate(StayReservedDateKey id, Stay stay) {
        this.id = id;
        this.stay = stay;
    }

    public StayReservedDateKey getId() {
        return id;
    }

    public Stay getStay() {
        return stay;
    }

}
