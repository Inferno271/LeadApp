package models;

import android.os.Parcel;
import android.os.Parcelable;

public class Call implements Parcelable {
    private long id; // Добавьте поле для идентификатора звонка
    private String callerName;
    private String phoneNumber;
    private String callDateTime;
    private int callDuration;
    private String additionalInfo;

    // Конструктор
    public Call(long id, String callerName, String phoneNumber, String callDateTime, int callDuration, String additionalInfo) {
        this.id = id;
        this.callerName = callerName;
        this.phoneNumber = phoneNumber;
        this.callDateTime = callDateTime;
        this.callDuration = callDuration;
        this.additionalInfo = additionalInfo;
    }

    // Геттеры для всех полей

    // Геттеры и сеттеры для id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCallerName() {
        return callerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCallDateTime() {
        return callDateTime;
    }

    public int getCallDuration() {
        return callDuration;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    // Методы Parcelable
    protected Call(Parcel in) {
        id = in.readLong();
        callerName = in.readString();
        phoneNumber = in.readString();
        callDateTime = in.readString();
        callDuration = in.readInt();
        additionalInfo = in.readString();
    }

    public static final Creator<Call> CREATOR = new Creator<Call>() {
        @Override
        public Call createFromParcel(Parcel in) {
            return new Call(in);
        }

        @Override
        public Call[] newArray(int size) {
            return new Call[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(callerName);
        dest.writeString(phoneNumber);
        dest.writeString(callDateTime);
        dest.writeInt(callDuration);
        dest.writeString(additionalInfo);
    }
}
