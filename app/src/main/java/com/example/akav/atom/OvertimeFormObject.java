package com.example.akav.atom;

import android.widget.TextView;

/**
 * Created by ankit on 01-02-2018.
 */

public class OvertimeFormObject {

    private String date;
    private String platformNumber;
    private String name;
    private String shift;
    private String actualStart;
    private String actualEnd;
    private String extraHours;
    private String reason;

    private Integer interVerification;
    private Integer finalVerification;

    private Integer notifyUser = 0;

    public OvertimeFormObject(String date, String platformNumber,
                              String name, String shift, String actualStart,
                              String actualEnd, String extraHours,
                              String reason) {
        this.date = date;
        this.platformNumber = platformNumber;
        this.name = name;
        this.shift = shift;
        this.actualStart = actualStart;
        this.actualEnd = actualEnd;
        this.extraHours = extraHours;
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public String getPlatformNumber() {
        return platformNumber;
    }

    public String getName() {
        return name;
    }

    public String getShift() {
        return shift;
    }

    public String getActualStart() {
        return actualStart;
    }

    public String getActualEnd() {
        return actualEnd;
    }

    public String getExtraHours() {
        return extraHours;
    }

    public String getReason() {
        return reason;
    }

    public Integer getInterVerification() {
        return interVerification;
    }

    public Integer getFinalVerification() {
        return finalVerification;
    }

    public Integer getNotifyUser() {
        return notifyUser;
    }

    public void setInterVerification(Integer interVerification) {
        this.interVerification = interVerification;
    }

    public void setFinalVerification(Integer finalVerification) {
        this.finalVerification = finalVerification;
    }

    public void setNotifyUser(Integer notifyUser) {
        this.notifyUser = notifyUser;
    }
}