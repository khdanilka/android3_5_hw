package ru.geekbrains.android3_5.model.image.android;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;
import ru.geekbrains.android3_5.model.common.NetworkStatus;
import ru.geekbrains.android3_5.model.image.ImageLoader;
import timber.log.Timber;

/**
 * Created by stanislav on 3/12/2018.
 */

public class ImageLoaderGlide implements ImageLoader<ImageView>
{
    @Override
    public void loadInto(@Nullable String url, ImageView container)
    {
        if(Paper.book("images").contains(MD5(url)))
        {
            byte[] bytes = Paper.book("images").read(MD5(url));
            Glide.with(container.getContext())
                    .load(bytes)
                    .into(container);
        }
        else
        {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>()
            {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource)
                {
                    Timber.e("failed to load image", e);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource)
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    Paper.book("images").write(MD5(url), stream.toByteArray());
                    return false;
                }
            }).into(container);
        }
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
