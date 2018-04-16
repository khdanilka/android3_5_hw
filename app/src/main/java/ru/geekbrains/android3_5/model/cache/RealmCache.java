package ru.geekbrains.android3_5.model.cache;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;
import ru.geekbrains.android3_5.model.entity.realm.RealmRepository;
import ru.geekbrains.android3_5.model.entity.realm.RealmUser;

public class RealmCache implements CacheInterface {

    @Override
    public void addUserToCache(User user) {

        Realm realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        if(realmUser == null)
        {
            realm.executeTransaction(innerRealm ->
            {
                RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
            });
        }
        else
        {
            realm.executeTransaction(innerRealm -> realmUser.setAvatarUrl(user.getAvatarUrl()));
        }

        realm.close();
    }

    @Override
    public void addRepositoriesToCache(User user, List<Repository> repositories) {

        Realm realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        if(realmUser == null)
        {
            realm.executeTransaction(innerRealm ->
            {
                RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
            });
        }

        realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();

        RealmUser finalRealmUser = realmUser;
        realm.executeTransaction(innerRealm -> {
            finalRealmUser.getRepos().deleteAllFromRealm();
            for (Repository repo : repositories)
            {
                RealmRepository realmRepository = realm.createObject(RealmRepository.class, repo.getId());
                realmRepository.setName(repo.getName());
                finalRealmUser.getRepos().add(realmRepository);
            }

        });

        realm.close();

    }

    @Override
    public Observable<User> getUserFromCache(String username) {
        return  Observable.create(e -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
            if(realmUser == null)
            {
                e.onError(new RuntimeException("No user in cache"));
            }
            else
            {
                e.onNext(new User(realmUser.getLogin(), realmUser.getAvatarUrl()));
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<Repository>> getUserReposFromCache(User user) {
        return Observable.create(e ->
        {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
            if (realmUser == null)
            {
                e.onError(new RuntimeException("No user in cache"));
            } else
            {
                List<Repository> repos = new ArrayList<>();
                for (RealmRepository realmRepository : realmUser.getRepos())
                {
                    repos.add(new Repository(realmRepository.getId(), realmRepository.getName()));
                }
                e.onNext(repos);
            }
            e.onComplete();
        });
    }
}
