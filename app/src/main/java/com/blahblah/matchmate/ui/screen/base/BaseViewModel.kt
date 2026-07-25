package com.blahblah.matchmate.ui.screen.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blahblah.matchmate.interfaces.ApiService
import com.blahblah.matchmate.repository.base.BaseRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private const val BASE_VIEW_MODEL_TAG = "view_model_error"

abstract class BaseViewModel<S : ApiService>(
    protected val tag: String?,
    protected val repository: BaseRepository<S>,
    protected val loggingCoroutineCtx: CoroutineContext = Dispatchers.IO + CoroutineExceptionHandler { _, e ->
        Log.e(tag?.takeIf { it.isNotEmpty() } ?: BASE_VIEW_MODEL_TAG, e.stackTraceToString())
    }
) : ViewModel() {

    init {
        viewModelScope.launch(loggingCoroutineCtx) { repository.initialFetch() }
    }
}