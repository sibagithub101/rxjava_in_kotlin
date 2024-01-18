package com.siba.rxjavademo.rxjava

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val fetchRepository: FetchRepository):ViewModel() {
    fun fetchReport(fetchReqest: FetchReqest): Observable<FetchResponse>{
        return fetchRepository.fetchReport(fetchReqest)
    }
}