package com.blahblah.matchmate.models.common

data class NameData(val first: String, val last: String) {
    val fullname: String
        get() = "$first $last"
}
