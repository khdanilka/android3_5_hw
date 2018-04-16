package ru.geekbrains.android3_5.model.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;

/**
 * Created by stanislav on 3/12/2018.
 */

public interface ApiService
{
    @GET("users/{user}")
    Observable<User> getUser(@Path("user") String userName);

    @GET("users/{user}/repos")
    Observable<List<Repository>> getUserRepos(@Path("user") String userName);

    @GET
    Observable<List<Repository>> getUserReposByUrl(@Url String url);

}
