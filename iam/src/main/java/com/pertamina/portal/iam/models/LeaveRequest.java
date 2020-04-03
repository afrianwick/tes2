package com.pertamina.portal.iam.models;

import java.util.Date;

class LeaveRequest {
	public long id;
	public String docNumber;
    public int leaveType;
    public String reason;
    public Date leaveDateStart;
    public Date leaveDateEnd;
    public String destinationCity;
    public int destinationCountry;
    public Comment comment;
    public boolean isCanceled;
    public Comment cancelReason;
}
