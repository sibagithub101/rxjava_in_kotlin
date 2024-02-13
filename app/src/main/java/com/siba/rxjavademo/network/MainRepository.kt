package com.siba.rxjavademo.network

import com.siba.rxjavademo.paging.Wallet1ReqModel
import com.siba.rxjavademo.paging.Wallet1RespModel
import com.siba.rxjavademo.rxjava.FetchReqest
import com.siba.rxjavademo.rxjava.FetchResponse
import io.reactivex.Observable
import javax.inject.Inject

class MainRepository @Inject constructor (private val apiService: ApiService){
    fun fetchDynamicData(url:String, fetchReqest: FetchReqest): Observable<FetchResponse> {
        return apiService.fetchDynamicData(url,fetchReqest)
    }

    fun fetchWallet1Data(url:String,wallet1ReqModel: Wallet1ReqModel):Observable<Wallet1RespModel>{
        return apiService.fetchWallet1Data(url,wallet1ReqModel)
    }
}