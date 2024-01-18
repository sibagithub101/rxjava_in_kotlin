package com.siba.rxjavademo.rxjava

import io.reactivex.Observable
import javax.inject.Inject

class FetchRepository @Inject constructor (private val apiService: ApiService){
    fun fetchReport(fetchReqest: FetchReqest): Observable<FetchResponse> {
        return apiService.fetchReportData(fetchReqest)
    }
}