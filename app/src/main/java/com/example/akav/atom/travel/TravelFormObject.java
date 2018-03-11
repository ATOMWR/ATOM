package com.example.akav.atom.travel;

import com.example.akav.atom.R;

/**
 * Created by Ankit on 04-03-2018.
 */

public class TravelFormObject {

    private String dateOfTravel;
    private String pfNumber;
    private String name;
    private String tspNumber;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String startStation;
    private String endStation;
    private String extraHours;
    private String reason;

    private Integer percentageCategory;
    private Integer interVerification;
    private Integer finalVerification;

    private Integer verificationStatusImageId;

    public TravelFormObject(String dateOfTravel, String pfNumber, String name, String tspNumber,
                            String startDate, String endDate, String startTime, String endTime,
                            String startStation, String endStation, String extraHours, String reason,
                            Integer percentageCategory, Integer interVerification, Integer finalVerification) {

        this.dateOfTravel = dateOfTravel;
        this.pfNumber = pfNumber;
        this.name = name;
        this.tspNumber = tspNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startStation = startStation;
        this.endStation = endStation;
        this.extraHours = extraHours;
        this.reason = reason;
        this.percentageCategory = percentageCategory;
        this.interVerification = interVerification;
        this.finalVerification = finalVerification;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPfNumber() {
        return pfNumber;
    }

    public void setPfNumber(String pfNumber) {
        this.pfNumber = pfNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTspNumber() {
        return tspNumber;
    }

    public void setTspNumber(String tspNumber) {
        this.tspNumber = tspNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getExtraHours() {
        return extraHours;
    }

    public void setExtraHours(String extraHours) {
        this.extraHours = extraHours;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getPercentageCategory() {
        return percentageCategory;
    }

    public void setPercentageCategory(Integer percentageCategory) {
        this.percentageCategory = percentageCategory;
    }

    public Integer getInterVerification() {
        return interVerification;
    }

    public void setInterVerification(Integer interVerification) {
        this.interVerification = interVerification;
    }

    public Integer getFinalVerification() {
        return finalVerification;
    }

    public void setFinalVerification(Integer finalVerification) {
        this.finalVerification = finalVerification;
    }

    public Integer getVerificationStatusImageId() {
        switch (interVerification) {
            case 1:
                verificationStatusImageId = R.drawable.check_mark;
                break;

            case 2:
                verificationStatusImageId = R.drawable.exclamation_mark;
                break;

            default:
                verificationStatusImageId = 0;
        }
        return verificationStatusImageId;
    }

    public void setVerificationStatusImageId(Integer verificationStatusImageId) {
        this.verificationStatusImageId = verificationStatusImageId;
    }
}
