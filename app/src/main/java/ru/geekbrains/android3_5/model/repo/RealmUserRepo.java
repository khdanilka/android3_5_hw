package ru.geekbrains.android3_5.model.repo;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.realm.Realm;
import ru.geekbrains.android3_5.model.api.ApiHolder;
import ru.geekbrains.android3_5.model.cache.RealmCache;
import ru.geekbrains.android3_5.model.common.NetworkStatus;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;
import ru.geekbrains.android3_5.model.entity.realm.RealmRepository;
import ru.geekbrains.android3_5.model.entity.realm.RealmUser;

public class RealmUserRepo implements IUserRepo
{

    RealmCache realmCache = new RealmCache();

    @Override
    public Observable<User> getUser(String username)
    {
        if(NetworkStatus.isOffline())
        {
//           return  Observable.create(e -> {
//                Realm realm = Realm.getDefaultInstance();
//                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
//                if(realmUser == null)
//                {
//                    e.onError(new RuntimeException("No user in cache"));
//                }
//                else
//                {
//                    e.onNext(new User(realmUser.getLogin(), realmUser.getAvatarUrl()));
//                }
//                e.onComplete();
//            });
           return realmCache.getUserFromCache(username);
        }
        else
        {
            return ApiHolder.getApi().getUser(username).map(user ->
            {
//                Realm realm = Realm.getDefaultInstance();
//                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
//                if(realmUser == null)
//                {
//                    realm.executeTransaction(innerRealm ->
//                    {
//                        RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
//                        newRealmUser.setAvatarUrl(user.getAvatarUrl());
//                    });
//                }
//                else
//                {
//                    realm.executeTransaction(innerRealm -> realmUser.setAvatarUrl(user.getAvatarUrl()));
//                }
//
//                realm.close();
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
//            return Observable.create(e ->
//            {
//                Realm realm = Realm.getDefaultInstance();
//                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
//                if (realmUser == null)
//                {
//                    e.onError(new RuntimeException("No user in cache"));
//                } else
//                {
//                    List<Repository> repos = new ArrayList<>();
//                    for (RealmRepository realmRepository : realmUser.getRepos())
//                    {
//                        repos.add(new Repository(realmRepository.getId(), realmRepository.getName()));
//                    }
//                    e.onNext(repos);
//                }
//                e.onComplete();
//            });
        }
        else
        {
            return ApiHolder.getApi().getUserRepos(user.getLogin()).map(repos ->
            {
//                Realm realm = Realm.getDefaultInstance();
//                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
//                if(realmUser == null)
//                {
//                    realm.executeTransaction(innerRealm ->
//                    {
//                        RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
//                        newRealmUser.setAvatarUrl(user.getAvatarUrl());
//                    });
//                }
//
//                realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
//
//                RealmUser finalRealmUser = realmUser;
//                realm.executeTransaction(innerRealm -> {
//                    finalRealmUser.getRepos().deleteAllFromRealm();
//                    for (Repository repo : repos)
//                    {
//                        RealmRepository realmRepository = realm.createObject(RealmRepository.class, repo.getId());
//                        realmRepository.setName(repo.getName());
//                        finalRealmUser.getRepos().add(realmRepository);
//                    }
//
//                });
//
//                realm.close();
                realmCache.addRepositoriesToCache(user,repos);
                return repos;
            });
        }


    }
}
