package com.example.travelday_2

import android.os.Parcel
import android.os.Parcelable


class TravelListItem(var country: String?, var startDate: String?, var endDate: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(country)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelListItem> {
        override fun createFromParcel(parcel: Parcel): TravelListItem {
            return TravelListItem(parcel)
        }

        override fun newArray(size: Int): Array<TravelListItem?> {
            return arrayOfNulls(size)
        }
    }

}