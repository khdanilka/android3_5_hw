package ru.geekbrains.android3_5.model.image.android;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;
import ru.geekbrains.android3_5.model.cache.RealmCache;
import ru.geekbrains.android3_5.model.common.NetworkStatus;
import ru.geekbrains.android3_5.model.entity.realm.RealmPhoto;
import ru.geekbrains.android3_5.model.image.ImageLoader;
import timber.log.Timber;

/**
 * Created by stanislav on 3/12/2018.
 */

public class ImageLoaderGlide implements ImageLoader<ImageView>
{
    private String LOG_TAG = "123";

    @Override
    public void loadInto(@Nullable String url, ImageView container)
    {
       String realmPhoto = new RealmCache().isOnRealm(url);

        if(realmPhoto != null)
        {
            Uri uri = Uri.fromFile(new File(realmPhoto));

            Glide.with(container.getContext())
                    .load(uri)
                    .into(container);
        }
        else
        {
            Glide.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>()
            {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource)
                {
                    Timber.e(e,"failed to load image");
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource)
                {
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    Paper.book("images").write(MD5(url), stream.toByteArray());
                    saveImage(resource,url,container.getContext());
                    return false;
                }
            }).into(container);
        }
    }

    private void saveImage(Bitmap resource, String url, Context context){

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, "avatar.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            resource.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Timber.e(e,e.getMessage());
        }

        Timber.d(mypath.getAbsolutePath(),"IMAGE");

        new RealmCache().addImagePath(url,mypath.getAbsolutePath());
    }


    public static String MD5(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
