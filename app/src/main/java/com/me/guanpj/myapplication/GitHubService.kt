package com.me.guanpj.myapplication

import com.me.guanpj.myapplication.Repo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.CompletableFuture

interface GitHubService {
  @GET("users/{user}/repos")
  fun listRepos(@Path("user") user: String?): Call<List<Repo>>

  @GET("users/{user}/repos")
  fun listReposRx(@Path("user") user: String?): Observable<List<Repo>>

  @GET("users/{user}/repos")
  suspend fun listReposSuspend(@Path("user") user: String?): List<Repo>

  @GET("users/{user}/repos")
  fun listReposCompletable(@Path("user") user: String?): CompletableFuture<List<Repo>>
}