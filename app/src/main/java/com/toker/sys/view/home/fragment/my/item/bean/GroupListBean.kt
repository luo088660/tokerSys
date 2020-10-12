package com.toker.sys.view.home.fragment.my.item.bean

import android.os.Parcel
import android.os.Parcelable

class GroupListBean {
    data class GroupLBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ) {
        /**
         * 请求是否成功
         */
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }


    data class Data(
        val groupName: String,
        val id: String,
        val projectId: String
    ):Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(groupName)
            parcel.writeString(id)
            parcel.writeString(projectId)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }
    }
}