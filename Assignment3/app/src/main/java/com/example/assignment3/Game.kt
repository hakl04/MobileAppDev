package com.example.assignment3

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

data class Game (
    var name : String,
    var coverURL : String,
    var trailerID : String,
    var price : Float,
    var rating : Float,
    var description : String,
    var tags : List<String>,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),  // Read each property of the data class
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readString().toString(),
        parcel.createStringArrayList() ?: emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)  // Write each property of the data class
        parcel.writeString(coverURL)
        parcel.writeString(trailerID)
        parcel.writeFloat(price)
        parcel.writeFloat(rating)
        parcel.writeString(description)
        parcel.writeStringList(tags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }
}