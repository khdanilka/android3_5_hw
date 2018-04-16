package ru.geekbrains.android3_5.model.repo;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.geekbrains.android3_5.model.api.ApiHolder;
import ru.geekbrains.android3_5.model.cache.PaperCache;
import ru.geekbrains.android3_5.model.common.NetworkStatus;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;

public class PaperUserRepo implements IUserRepo
{

    PaperCache paperCache = new PaperCache();

    @Override
    public Observable<User> getUser(String username)
    {
        if(NetworkStatus.isOffline())
        {
//            User user = Paper.book("user").read("user");
//            return Observable.just(user);
            return paperCache.getUserFromCache(username);
        }
        else
        {
            return ApiHolder.getApi().getUser(username).map(user ->
            {
                //Paper.book("user").write("user", user);
                paperCache.addUserToCache(user);
                return user;
            });
        }
    }

    @Override
    public Observable<List<Repository>> getUserRepos(User user)
    {
        if(NetworkStatus.isOffline())
        {
//            List<Repository> repos = Paper.book("repos").read("repos", new ArrayList<>());
//            return Observable.just(repos);
            return paperCache.getUserReposFromCache(user);
        }
        else
        {
            return ApiHolder.getApi().getUserRepos(user.getLogin()).map(repos ->
            {
               // Paper.book("repos").write("repos", repos);
                paperCache.addRepositoriesToCache(user,repos);
                return repos;
            });
        }


    }
}
