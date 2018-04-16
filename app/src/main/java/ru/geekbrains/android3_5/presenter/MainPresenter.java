package ru.geekbrains.android3_5.presenter;

import android.util.Log;

import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_5.model.entity.User;
import ru.geekbrains.android3_5.model.repo.ActiveAndroidUserRepo;
import ru.geekbrains.android3_5.model.repo.IUserRepo;
import ru.geekbrains.android3_5.model.repo.PaperUserRepo;
import ru.geekbrains.android3_5.model.repo.RealmUserRepo;
import ru.geekbrains.android3_5.view.MainView;
import ru.geekbrains.android3_5.view.RepoRowView;


public class MainPresenter implements IRepoListPresenter
{
    private static final String TAG = "MainPresenter";
    private MainView view;
    private Scheduler scheduler;
    private IUserRepo userRepo;

    private User user;

    public MainPresenter(MainView view, Scheduler scheduler)
    {
        this.view = view;
        this.scheduler = scheduler;
        userRepo = new RealmUserRepo();
    }

    public void loadInfo()
    {
        userRepo.getUser("SupNacho")
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(user -> {
                    MainPresenter.this.user = user;
                    view.hideLoading();
                    view.showAvatar(user.getAvatarUrl());
                    view.setUsername(user.getLogin());
                    userRepo.getUserRepos(user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(scheduler)
                            .subscribe(repos -> {
                                user.setRepos(repos);
                                view.updateRepoList();
                            });

                }, throwable ->
                {
                    Log.e(TAG, "Failed to get user", throwable);
                    view.showError(throwable.getMessage());
                });
    }

    public void onPermissionsGranted()
    {
        loadInfo();
    }

    @Override
    public void bindRepoListRow(int pos, RepoRowView rowView)
    {
        if (user != null)
        {
            rowView.setTitle(user.getRepos().get(pos).getName());
        }
    }

    @Override
    public int getRepoCount()
    {
        return user == null  || user.getRepos() == null ? 0 : user.getRepos().size();
    }
}
