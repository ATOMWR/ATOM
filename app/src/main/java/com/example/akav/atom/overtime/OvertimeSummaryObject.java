package com.example.akav.atom.overtime;

import com.example.akav.atom.R;

public class OvertimeSummaryObject {

    private String name;
    private String category;
    private String totalEightHours;
    private String totalSixHours;
    private String totalRosteredDutyHours;
    private String totalActualDutyHours;
    private String extraDutyHours;
    private String reason;

    private Integer interVerification;

    private Integer verificationStatusImageId;

    public OvertimeSummaryObject(String name, String category, String totalEightHours,
                                 String totalSixHours, String totalRosteredDutyHours,
                                 String totalActualDutyHours, String extraDutyHours,
                                 String reason, Integer interVerification) {
        this.name = name;
        this.totalEightHours = totalEightHours;
        this.totalSixHours = totalSixHours;
        this.totalRosteredDutyHours = totalRosteredDutyHours;
        this.totalActualDutyHours = totalActualDutyHours;
        this.extraDutyHours = extraDutyHours;
        this.reason = reason;
        this.category = category;
        this.interVerification = interVerification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalEightHours() {
        return totalEightHours;
    }

    public void setTotalEightHours(String totalEightHours) {
        this.totalEightHours = totalEightHours;
    }

    public String getTotalSixHours() {
        return totalSixHours;
    }

    public void setTotalSixHours(String totalSixHours) {
        this.totalSixHours = totalSixHours;
    }

    public String getTotalRosteredDutyHours() {
        return totalRosteredDutyHours;
    }

    public void setTotalRosteredDutyHours(String totalRosteredDutyHours) {
        this.totalRosteredDutyHours = totalRosteredDutyHours;
    }

    public String getTotalActualDutyHours() {
        return totalActualDutyHours;
    }

    public void setTotalActualDutyHours(String totalActualDutyHours) {
        this.totalActualDutyHours = totalActualDutyHours;
    }

    public String getExtraDutyHours() {
        return extraDutyHours;
    }

    public void setExtraDutyHours(String extraDutyHours) {
        this.extraDutyHours = extraDutyHours;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
