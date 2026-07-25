package com.blahblah.matchmate.interfaces

interface RecyclerViewItem {

    val identity: String
    fun isValid(): Boolean
}