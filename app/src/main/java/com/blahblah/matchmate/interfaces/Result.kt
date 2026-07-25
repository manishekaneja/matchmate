package com.blahblah.matchmate.interfaces

sealed interface Result {
    class Success<T>(result: T) : Result
    class Failure() : Result
    class Loading() : Result
    object NoAction : Result
}