package ru.geekbrains.android3_5.model.repo;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.geekbrains.android3_5.model.api.ApiHolder;
import ru.geekbrains.android3_5.model.cache.ActiveAndroidCache;
import ru.geekbrains.android3_5.model.common.NetworkStatus;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;
import ru.geekbrains.android3_5.model.entity.activeandroid.AARepository;
import ru.geekbrains.android3_5.model.entity.activeandroid.AAUser;
import timber.log.Timber;

public class ActiveAndroidUserRepo implements IUserRepo
{

    ActiveAndroidCache activeAndroidCache = new ActiveAndroidCache();

    @Override
    public Observable<User> getUser(String username)
    {
        if (NetworkStatus.isOffline())
        {
            return activeAndroidCache.getUserFromCache(username);
//            return Observable.create(e ->
//            {
//                AAUser aaUser = new Select()
//                        .from(AAUser.class)
//                        .where("login = ?", username)
//                        .executeSingle();
//
//                if (aaUser == null)
//                {
//                    e.onError(new RuntimeException("No user in cache"));
//                } else
//                {
//                    e.onNext(new User(aaUser.login, aaUser.avatarUrl));
//                }
//                e.onComplete();
//            });
        } else
        {
            return ApiHolder.getApi().getUser(username).map(user ->
            {
//                AAUser aaUser = new Select()
//                        .from(AAUser.class)
//                        .where("login = ?", username)
//                        .executeSingle();
//
//                if (aaUser == null)
//                {
//                    aaUser = new AAUser();
//                    aaUser.login = user.getLogin();
//                }
//
//                aaUser.avatarUrl = user.getAvatarUrl();
//                aaUser.save();
                activeAndroidCache.addUserToCache(user);
                return user;
            });

        }
    }

    @Override
    public Observable<List<Repository>> getUserRepos(User user)
    {
        if (NetworkStatus.isOffline())
        {
            return activeAndroidCache.getUserReposFromCache(user);
        }
        else
        {
            return ApiHolder.getApi().getUserRepos(user.getLogin()).map(repos ->
            {
//                AAUser aaUser = new Select()
//                        .from(AAUser.class)
//                        .where("login = ?", user.getLogin())
//                        .executeSingle();
//
//                if (aaUser == null)
//                {
//                    aaUser = new AAUser();
//                    aaUser.login = user.getLogin();
//                    aaUser.avatarUrl = user.getAvatarUrl();
//                    aaUser.save();
//                }
//
//                new Delete().from(AARepository.class).where("user = ?", aaUser).execute();
//
//                ActiveAndroid.beginTransaction();
//                try
//                {
//                    for (Repository repo : repos)
//                    {
//                        AARepository aaRepository = new AARepository();
//                        aaRepository.id = repo.getId();
//                        aaRepository.name = repo.getName();
//                        aaRepository.user = aaUser;
//                        aaRepository.save();
//                    }
//                    ActiveAndroid.setTransactionSuccessful();
//                }
//                finally
//                {
//                    ActiveAndroid.endTransaction();
//                }
                activeAndroidCache.addRepositoriesToCache(user,repos);
                return repos;
            });
        }


    }
}
