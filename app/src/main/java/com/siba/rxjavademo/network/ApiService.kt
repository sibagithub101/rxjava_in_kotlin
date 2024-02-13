package com.siba.rxjavademo.network

import com.siba.rxjavademo.paging.Wallet1ReqModel
import com.siba.rxjavademo.paging.Wallet1RespModel
import com.siba.rxjavademo.rxjava.FetchReqest
import com.siba.rxjavademo.rxjava.FetchResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @POST
    fun fetchDynamicData(@Url url: String, @Body fetchRequest : FetchReqest):Observable<FetchResponse>
    @POST
    fun fetchWallet1Data(@Url url:String,@Body wallet1ReqModel: Wallet1ReqModel):Observable<Wallet1RespModel>

}
