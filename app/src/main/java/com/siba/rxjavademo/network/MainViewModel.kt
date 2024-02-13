package com.siba.rxjavademo.network

import androidx.lifecycle.ViewModel
import com.siba.rxjavademo.paging.Wallet1ReqModel
import com.siba.rxjavademo.paging.Wallet1RespModel
import com.siba.rxjavademo.rxjava.FetchReqest
import com.siba.rxjavademo.rxjava.FetchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository):ViewModel() {
    fun fetchDynamicData(url:String, fetchReqest: FetchReqest): Observable<FetchResponse>{
        return mainRepository.fetchDynamicData(url,fetchReqest)
    }
    fun fetchWallet1Data(url: String,wallet1ReqModel: Wallet1ReqModel):Observable<Wallet1RespModel>{
        return mainRepository.fetchWallet1Data(url,wallet1ReqModel)
    }
}