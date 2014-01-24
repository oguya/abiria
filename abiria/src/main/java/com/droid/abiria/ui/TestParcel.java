package com.droid.abiria.ui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by james on 23/01/14.
 */
public class TestParcel implements Parcelable {
    int a;
    String b;

    public TestParcel(int a, String b){
        this.a = a;
        this.b = b;
    }


    //parcel part
    public TestParcel(Parcel parcel){
        this.a = parcel.readInt();
        this.b = parcel.readString();
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(this.a);
        dest.writeString(this.b);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new TestParcel(source);
        }

        @Override
        public TestParcel[] newArray(int size) {
            return new TestParcel[size];
        }
    };
}