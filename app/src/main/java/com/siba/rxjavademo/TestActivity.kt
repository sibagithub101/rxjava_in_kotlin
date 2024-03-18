package com.siba.rxjavademo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        CoroutineScope(Dispatchers.IO).launch {
            printFollwers()
        }
    }

    suspend fun printFollwers() {
        var follwers = 0
     val job= CoroutineScope(Dispatchers.IO).launch {
            follwers = getFollwers()
        }
        job.join()
        Log.e("TAG", "printFollwers: $follwers")
    }
    suspend fun getFollwers():Int{
        delay(2000)
        return 50
    }

}