package ru.geekbrains.android3_5.model.cache;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;


public class PaperCache implements CacheInterface {

    @Override
    public void addUserToCache(User user) {
        Paper.book("user").write("user", user);
    }

    @Override
    public void addRepositoriesToCache(User user, List<Repository> repositories) {
        Paper.book("repos").write("repos", repositories);
    }

    @Override
    public Observable<User> getUserFromCache(String username) {
        User user = Paper.book("user").read("user");
        return Observable.just(user);
    }

    @Override
    public Observable<List<Repository>> getUserReposFromCache(User user) {
        List<Repository> repos = Paper.book("repos").read("repos", new ArrayList<>());
        return Observable.just(repos);
    }


}
