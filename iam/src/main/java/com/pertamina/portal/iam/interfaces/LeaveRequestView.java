package com.pertamina.portal.iam.interfaces;

import com.pertamina.portal.iam.models.LeaveType;

import java.util.List;

public interface LeaveRequestView {

    void onDestinationCountryClicked(List<String> country, List<String> countryIDs);
    void onDestinationCountryItemClicked(String country, String countryID);
    void onLeaveTypeClicked(List<LeaveType> leaveTypes);
    void onLeaveTypeItemClicked(LeaveType leaveType);
}
