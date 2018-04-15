package com.example.akav.atom.travel;

import com.example.akav.atom.R;

public class TravelSummaryObject {

    private String name;
    private String category;
    private String totalTravels;
    private String totalTravelHours;

    private String reason;

    private Integer interVerification;

    private Integer verificationStatusImageId;

    public TravelSummaryObject(String name, String category, String totalTravels,
                               String totalTravelHours, String reason,
                               Integer interVerification) {
        this.name = name;
        this.category = category;
        this.totalTravels = totalTravels;
        this.totalTravelHours = totalTravelHours;
        this.reason = reason;
        this.interVerification = interVerification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTotalTravels() {
        return totalTravels;
    }

    public void setTotalTravels(String totalTravels) {
        this.totalTravels = totalTravels;
    }

    public String getTotalTravelHours() {
        return totalTravelHours;
    }

    public void setTotalTravelHours(String totalTravelHours) {
        this.totalTravelHours = totalTravelHours;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getInterVerification() {
        return interVerification;
    }

    public void setInterVerification(Integer interVerification) {
        this.interVerification = interVerification;
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
