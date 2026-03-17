package com.angola_argentina_portal.interfaces;

public interface ServiceTableProjection {
    String getServiceName();

    String getRequirements();

    String getFees();

    double getPrice();

    String getDetails();

    String getStatus();

    String getAvailableDays();

    boolean isOnlineBooking();
}
