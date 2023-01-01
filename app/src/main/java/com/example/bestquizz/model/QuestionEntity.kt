package com.example.bestquizz.model


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionEntity(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(type)
        parcel.writeString(difficulty)
        parcel.writeString(question)
        parcel.writeString(correct_answer)
        parcel.writeStringList(incorrect_answers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionEntity> {
        override fun createFromParcel(parcel: Parcel): QuestionEntity {
            return QuestionEntity(parcel)
        }

        override fun newArray(size: Int): Array<QuestionEntity?> {
            return arrayOfNulls(size)
        }
    }

}

@JsonClass(generateAdapter = true)
data class QuestionResponse(@Json(name="results")
                            val result : List<QuestionEntity>)