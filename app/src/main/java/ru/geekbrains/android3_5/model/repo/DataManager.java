package ru.geekbrains.android3_5.model.repo;

import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_5.model.api.ApiHolder;
import ru.geekbrains.android3_5.model.cache.CacheInterface;
import ru.geekbrains.android3_5.model.common.NetworkStatus;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;

public class DataManager implements DMInterface
{

    CacheInterface realmCache;

    public DataManager(CacheInterface realmCache) {
        this.realmCache = realmCache;
    }

    @Override
    public Observable<User> getUser(String username)
    {
        if(NetworkStatus.isOffline())
        {
           return realmCache.getUserFromCache(username);
        }
        else
        {
            return ApiHolder.getApi().getUser(username).map(user ->
            {
                realmCache.addUserToCache(user);
                return user;
            });
        }
    }

    @Override
    public Observable<List<Repository>> getUserRepos(User user)
    {
        if(NetworkStatus.isOffline())
        {
            return realmCache.getUserReposFromCache(user);
        }
        else
        {
            return ApiHolder.getApi().getUserRepos(user.getLogin()).map(repos ->
            {
                realmCache.addRepositoriesToCache(user,repos);
                return repos;
            });
        }
    }
}
