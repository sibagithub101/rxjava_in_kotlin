package com.siba.rxjavademo.paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.siba.rxjavademo.R
import com.siba.rxjavademo.databinding.ActivityPagingBinding
import com.siba.rxjavademo.network.MainViewModel
import com.siba.rxjavademo.network.NetworkConstants.Companion.WALLET1_REPORT_API
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Flow.Subscriber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PagingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPagingBinding
    private val mainViewModel:MainViewModel by viewModels()
    private val disposal = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReport.setOnClickListener{
            binding.progressCircular.visibility = View.VISIBLE

            val trnsType: ArrayList<String> = ArrayList()
            trnsType.add("virtual_balance")
            trnsType.add("Inter_Wallet")
            trnsType.add("Virtual_Balance_Transfer")
            trnsType.add("IMPS_FUND_TRANSFER")
            trnsType.add("BENE_VERIFICATION")
            trnsType.add("COMMISSION")
            trnsType.add("NEFT_FUND_TRANSFER")
            trnsType.add("WALLET_INTERCHANGE")
            trnsType.add("Recharge_Prepaid")
            trnsType.add("Recharge_Postpaid")
            trnsType.add("Recharge_DTH")
            trnsType.add("QR_COLLECT")
            trnsType.add("UPI_COLLECT")
            trnsType.add("PG_Internet Banking")
            trnsType.add("Recharge_Prepaid")
            trnsType.add("Recharge_Postpaid")
            trnsType.add("Recharge_DTH")
            trnsType.add("LIC_Premium")
            trnsType.add("INSURANCE")



            val wallet1ReqModel = Wallet1ReqModel("wallet1_new_web_common", "Ankeeta01", "2024-01-01", "2024-01-10",trnsType)

            disposal.add(mainViewModel.fetchWallet1Data(url = WALLET1_REPORT_API,wallet1ReqModel=wallet1ReqModel)
                .subscribeOn(Schedulers.io())
                .timeout(1, TimeUnit.MINUTES)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally{ binding.progressCircular.visibility = View.GONE }
                .subscribe(
                    { success -> Log.e("Success", success.message.toString()) },  // getting success results
                    { error ->   }, //getting error results
                    { Log.e("Tag", "Api called completed" ) } // it called when api called success
                )
            )
        }
    }
}