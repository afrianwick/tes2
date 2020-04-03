package com.pertamina.portal.iam.models;

import android.os.Parcel;
import android.os.Parcelable;

public class IamComment implements Parcelable {
    public String username, message, strDate;
    public String status;
    public String createdBy;

    public IamComment() {
    }

    protected IamComment(Parcel in) {
        username = in.readString();
        message = in.readString();
        strDate = in.readString();
        status = in.readString();
        createdBy = in.readString();
    }

    public static final Creator<IamComment> CREATOR = new Creator<IamComment>() {
        @Override
        public IamComment createFromParcel(Parcel in) {
            return new IamComment(in);
        }

        @Override
        public IamComment[] newArray(int size) {
            return new IamComment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(message);
        parcel.writeString(strDate);
        parcel.writeString(status);
        parcel.writeString(createdBy);
    }
}
