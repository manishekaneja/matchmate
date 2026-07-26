package com.blahblah.matchmate.ui.recyclerview.interfaces

interface PaginationProvider {
    fun paginationThreshold(): Int
    fun performPaginationCall()
}