package com.example.akav.atom;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ankit on 09-02-2018.
 */

public class CycleDateObject {
    private Long startDate;
    private Long endDate;
    private Integer flag;

    public CycleDateObject(Long startDate, Long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CycleDateObject(Long startDate, Long endDate, Integer flag) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.flag = flag;
    }

    public String getStartDate() {
        return new SimpleDateFormat("dd - MM - yyyy").format(new Date(startDate * 1000));
    }

    public String getEndDate() {
        return new SimpleDateFormat("dd - MM - yyyy").format(new Date(endDate * 1000));
    }

    public Integer getFlag() {
        return flag;
    }
}