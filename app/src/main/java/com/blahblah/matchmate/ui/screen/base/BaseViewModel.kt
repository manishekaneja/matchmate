package com.blahblah.matchmate.ui.screen.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blahblah.matchmate.interfaces.ApiService
import com.blahblah.matchmate.interfaces.Response
import com.blahblah.matchmate.repository.base.BaseRepository
import com.blahblah.matchmate.ui.recyclerview.interfaces.PaginationProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private const val BASE_VIEW_MODEL_TAG = "view_model_error"
private const val PAGINATION_TRIGGER_THRESHOLD = 10

abstract class BaseViewModel<S : ApiService, R : Response, BR : BaseRepository<S, R>>(
    val tag: String?,
    protected val repository: BR,
    protected val loggingCoroutineCtx: CoroutineContext = Dispatchers.IO + CoroutineExceptionHandler { _, e ->
        Log.e(tag?.takeIf { it.isNotEmpty() } ?: BASE_VIEW_MODEL_TAG, e.stackTraceToString())
    }
) : ViewModel() {

    private var isPaginationEnabled = true
    private var paginationPageCount = 2

    init {
        viewModelScope.launch(loggingCoroutineCtx) {
            onSuccess(repository.initialFetch())
        }
    }

    val paginationCallback by lazy {
        object : PaginationProvider {
            override fun canPaginate() = isPaginationEnabled

            override fun paginationThreshold() = PAGINATION_TRIGGER_THRESHOLD

            override fun performPaginationCall() {
                viewModelScope.launch(loggingCoroutineCtx) {
                    onPagination(repository.paginationFetch(paginationPageCount))
                }

            }
        }
    }

    abstract fun onSuccess(result: R)

    abstract fun onPagination(result: R)

    abstract fun onFailure()
}