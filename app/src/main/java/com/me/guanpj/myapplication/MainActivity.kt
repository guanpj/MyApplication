package com.me.guanpj.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, LeakActivity::class.java))

        val user = "guanpj"

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .proxy(Proxy.NO_PROXY)
            .addInterceptor(HttpLoggingInterceptor { message ->
                if (BuildConfig.DEBUG) {
                    Log.i("OkHttp", message)
                }
            })
            .build()

        val request: Request = Request.Builder()
            .url("https://api.github.com/users/$user/repos")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    println("Response status code: ${response.code}")
                }
            })

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)

        val listRepos = service.listRepos(user)
        listRepos.enqueue(object : retrofit2.Callback<List<Repo>?> {
            override fun onFailure(call: retrofit2.Call<List<Repo>?>, t: Throwable) {

            }

            override fun onResponse(
                call: retrofit2.Call<List<Repo>?>,
                response: retrofit2.Response<List<Repo>?>
            ) {
                println("Response: ${response.body()!![0].name}")
            }
        })

        val listReposRx = service.listReposRx(user)
        listReposRx.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Repo>> {
                override fun onSubscribe(d: Disposable?) {
                }

                override fun onNext(t: List<Repo>?) {
                }

                override fun onComplete() {
                }

                override fun onError(e: Throwable?) {
                }
            })

        MainScope().launch {
            val list = service.listReposSuspend(user)
            println("size:${list.size}")
        }

        Glide.with(this).load("")
    }


}