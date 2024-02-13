package com.siba.rxjavademo.rxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.siba.rxjavademo.databinding.ActivityMainBinding
import com.siba.rxjavademo.network.MainViewModel
import com.siba.rxjavademo.network.NetworkConstants.Companion.FETCH_REPORT_API
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class RxJavaActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val disposal = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetData.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            val fetchReqest = FetchReqest("common", "technewadmin")

            disposal.add(mainViewModel.fetchDynamicData(url = FETCH_REPORT_API, fetchReqest = fetchReqest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally{ binding.progress.visibility = View.GONE }   // progress dialog visibility if is success or failure
                .subscribe(
                    { success -> getValueApi(success) },  // getting success results
                    { error -> getErrorValue(error.localizedMessage) }, //getting error results
                    { Log.e("Tag", "Api called completed" ) } // it called when api called success
                )
            )

        }
    }


    private fun getValueApi(success: FetchResponse?) {
        binding.getData.text = success?.data?.version
    }

    private fun getErrorValue(message: String?) {
        binding.getData.text = message
    }
}