package ru.geekbrains.android3_5.model.cache;


import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;

public interface CacheInterface {

    void addUserToCache(User user);
    void addRepositoriesToCache(User user, List<Repository> repositories);

    Observable<User> getUserFromCache(String username);
    Observable<List<Repository>> getUserReposFromCache(User user);

}
