package com.daday.finalgithubuser.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItems(
    var username: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var url: String? = null,
    var company: String? = null,
    var location: String? = null,
    var followers: String? = null,
    var following: String? = null
) : Parcelable