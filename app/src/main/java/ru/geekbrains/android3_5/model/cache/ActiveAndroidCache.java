package ru.geekbrains.android3_5.model.cache;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_5.model.entity.Repository;
import ru.geekbrains.android3_5.model.entity.User;
import ru.geekbrains.android3_5.model.entity.activeandroid.AARepository;
import ru.geekbrains.android3_5.model.entity.activeandroid.AAUser;


public class ActiveAndroidCache implements CacheInterface {

    @Override
    public void addUserToCache(User user) {

        AAUser aaUser = new Select()
                .from(AAUser.class)
                .where("login = ?", user.getLogin())
                .executeSingle();

        if (aaUser == null)
        {
            aaUser = new AAUser();
            aaUser.login = user.getLogin();
        }

        aaUser.avatarUrl = user.getAvatarUrl();
        aaUser.save();

    }

    @Override
    public void addRepositoriesToCache(User user, List<Repository> repositories) {

        AAUser aaUser = new Select()
                .from(AAUser.class)
                .where("login = ?", user.getLogin())
                .executeSingle();

        if (aaUser == null)
        {
            aaUser = new AAUser();
            aaUser.login = user.getLogin();
            aaUser.avatarUrl = user.getAvatarUrl();
            aaUser.save();
        }

        new Delete().from(AARepository.class).where("user = ?", aaUser).execute();

        ActiveAndroid.beginTransaction();
        try
        {
            for (Repository repo : repositories)
            {
                AARepository aaRepository = new AARepository();
                aaRepository.id = repo.getId();
                aaRepository.name = repo.getName();
                aaRepository.user = aaUser;
                aaRepository.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally
        {
            ActiveAndroid.endTransaction();
        }
    }

    @Override
    public Observable<User> getUserFromCache(String username) {

        return Observable.create(e ->
        {
            AAUser aaUser = new Select()
                    .from(AAUser.class)
                    .where("login = ?", username)
                    .executeSingle();

            if (aaUser == null)
            {
                e.onError(new RuntimeException("No user in cache"));
            } else
            {
                e.onNext(new User(aaUser.login, aaUser.avatarUrl));
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<Repository>> getUserReposFromCache(User user) {
        return Observable.create(e ->{
            AAUser aaUser = new Select()
                    .from(AAUser.class)
                    .where("login = ?", user.getLogin())
                    .executeSingle();

            if (aaUser == null)
            {
                e.onError(new RuntimeException("No user in cache"));
            }
            else
            {
                List<Repository> repos = new ArrayList<>();
                for(AARepository repo : aaUser.repositories())
                {
                    repos.add(new Repository(repo.id, repo.name));
                }
                e.onNext(repos);

            }
            e.onComplete();

        });
    }
}
