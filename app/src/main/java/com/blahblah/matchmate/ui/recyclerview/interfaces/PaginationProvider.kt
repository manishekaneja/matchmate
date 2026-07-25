package com.blahblah.matchmate.ui.recyclerview.interfaces

interface PaginationProvider {
    fun canPaginate(): Boolean
    fun paginationThreshold(): Int
    fun performPaginationCall(): Unit
}