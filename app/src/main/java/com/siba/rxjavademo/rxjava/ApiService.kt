package com.siba.rxjavademo.rxjava

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("fetchData")
    fun fetchReportData(@Body fetchRequest :FetchReqest):Observable<FetchResponse>
}
