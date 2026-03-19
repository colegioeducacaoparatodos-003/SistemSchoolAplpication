package com.angola_argentina_portal.interfaces;

import java.time.LocalDate;

public interface BookingTableProjection {

    int getPkBooking();

    String getServiceName();

    String getFullName();

    String getEmail();

    String getPhone();

    LocalDate getBookingDate();

    String getBookingTime();

    String getStatus();
}