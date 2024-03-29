package com.me.guanpj.myapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.me.guanpj.myapplication.event.MessageEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    lateinit var service: GitHubService
    val user = "guanpj"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)

        val interceptor = HttpLoggingInterceptor { message ->
            Log.i("gpj", message)
        }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
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
                    Log.e("gpj", "Response status code: ${response.code}")
                    Log.e("gpj", "Response : ${response.body.toString()}")
                }
            })

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(GitHubService::class.java)

        val listRepos = service.listRepos(user)
        listRepos.enqueue(object : retrofit2.Callback<List<Repo>?> {
            override fun onFailure(call: retrofit2.Call<List<Repo>?>, t: Throwable) {

            }

            override fun onResponse(
                call: retrofit2.Call<List<Repo>?>,
                response: retrofit2.Response<List<Repo>?>
            ) {
                Log.e("gpj", "Response: ${response.body()!![0].toString()}")
            }
        })

        val listReposRx = service.listReposRx(user)
        listReposRx.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Repo>> {
                override fun onSubscribe(d: Disposable?) {
                }

                override fun onNext(t: List<Repo>?) {
                    Log.e("gpj", "size:${t?.size}")
                }

                override fun onComplete() {
                }

                override fun onError(e: Throwable?) {
                }
            })

        MainScope().launch {
            val list = service.listReposSuspend(user)
            Log.e("gpj", "size:${list.size}")
        }

        val future: CompletableFuture<List<Repo>> = service.listReposCompletable(user)
        future.thenAccept { Log.e("gpj", "size:${it.size}") }

        /*val call = service.listReposOptional(user)
        thread {
            val optionalList = call.execute().body()
            optionalList?.ifPresent { list -> list.map { Log.e("gpj", it.full_name) } }
        }*/
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun load(view: View) {
        val future: CompletableFuture<List<Repo>> = service.listReposCompletable(user)
        future.thenAccept { Log.e("gpj", "size:${it.size}") }
//        val future: CompletableFuture<List<Repo>> = service.listReposJava8(user)
//            .handle(object : BiFunction<List<Repo>, Throwable, List<Repo>> {
//                override fun apply(repos: List<Repo>, throwable: Throwable): List<Repo> {
//                    Log.e("handleAsync", Thread.currentThread().name) //***************(1)
//                    Log.e("handleAsync", repos.toString())
//                    return repos
//                }
//            })
//        try {
//            future.get(5, TimeUnit.SECONDS) //这里必须加 try catch***************(2)
//            future.thenApply(Function { Log.e("thenApply2", Thread.currentThread().name)  }).thenApply(
//                Function { Log.e("thenApply2", Thread.currentThread().name) })
//        } catch (e: Exception) {
//            Log.e("handleAsync", e.toString())
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,  sticky = true, priority = 3)
    fun onEvent3(event: MessageEvent) {
        Log.e("gpj", "MainActivity receive msg: ${event.message}, in thread: ${Thread.currentThread().name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
