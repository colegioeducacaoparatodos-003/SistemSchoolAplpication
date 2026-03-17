package com.angola_argentina_portal.interfaces;

public interface ConsularServiceTableProjection {

    int getPkService();

    String getName();

    String getRequirements();

    String getFees();

    Double getPrice();

    String getDetails();

    String getStatus();

    String getAvailableDays();

    Boolean getOnlineBooking();
}