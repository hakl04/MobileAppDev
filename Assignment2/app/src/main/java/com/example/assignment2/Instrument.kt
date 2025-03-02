package com.example.assignment2

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

data class Instrument (
     var name : String,
     var image : Int,
     var cost : Int,
     var months : Int,
     var rating : Float,
     var description : String,
     var accessories: Map<String, Int>
) : Parcelable {
     constructor(parcel: Parcel) : this(
         parcel.readString().toString(),  // Read each property of the data class
          parcel.readInt(),
          parcel.readInt(),
          parcel.readInt(),
          parcel.readFloat(),
         parcel.readString().toString(),
          parcel.readBundle().let { bundle ->  //Use Bundle to get the accessories Map
               bundle?.keySet()?.associateWith { key -> bundle.getInt(key) } ?: emptyMap()
          }
     ) {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeString(name)  // Write each property of the data class
          parcel.writeInt(image)
          parcel.writeInt(cost)
          parcel.writeInt(months)
          parcel.writeFloat(rating)
          parcel.writeString(description)
          val bundle = Bundle()
          for ((key, value) in accessories) {  // Use Bundle to write the accessories Map
               bundle.putInt(key, value)
          }
          parcel.writeBundle(bundle)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<Instrument> {
          override fun createFromParcel(parcel: Parcel): Instrument {
               return Instrument(parcel)
          }

          override fun newArray(size: Int): Array<Instrument?> {
               return arrayOfNulls(size)
          }
     }
}