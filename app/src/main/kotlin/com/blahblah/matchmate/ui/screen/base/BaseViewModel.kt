package com.blahblah.matchmate.ui.screen.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.blahblah.matchmate.interfaces.ApiService
import com.blahblah.matchmate.repository.base.BaseRepository
import com.blahblah.matchmate.ui.recyclerview.interfaces.PaginationProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val PAGINATION_TRIGGER_THRESHOLD = 3
private const val SECOND_PAGE = 2

abstract class BaseViewModel<S : ApiService, BR : BaseRepository<S>>(
    val logTag: String,
    protected val repository: BR
) : ViewModel() {
    private val _loadState = MutableStateFlow<LoadState>(LoadState.Loading)
    val loadState: StateFlow<LoadState> = _loadState.asStateFlow()

    private var isPaginationEnabled = true
    private var nextPage = SECOND_PAGE
    private var paginationJob: Job? = null

    private val _isPaginating = MutableStateFlow(false)
    val isPaginating: LiveData<Boolean> = _isPaginating.asLiveData()

    val paginationCallback: PaginationProvider = object : PaginationProvider {

        override fun paginationThreshold() = PAGINATION_TRIGGER_THRESHOLD

        override fun performPaginationCall() {
            if (!canPaginate()) return
            val page = nextPage
            paginationJob = viewModelScope.launch {
                _isPaginating.value = true
                try {
                    val fetched = repository.paginationFetch(page)
                    if (fetched > 0) nextPage++ else isPaginationEnabled = false
                } catch (cancellation: CancellationException) {
                    throw cancellation
                } catch (error: Exception) {
                    Log.e(logTag, "pagination failed for page $page", error)
                } finally {
                    _isPaginating.value = false
                }
            }
        }
    }

    fun canPaginate() = isPaginationEnabled && paginationJob?.isActive != true

    fun loadFirstPage() {
        viewModelScope.launch {
            _loadState.value = LoadState.Loading
            try {
                repository.initialFetch()
                _loadState.value = LoadState.Loaded
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                Log.e(logTag, "initial fetch failed", error)
                _loadState.value = LoadState.Failed(error)
            }
        }
    }
}
