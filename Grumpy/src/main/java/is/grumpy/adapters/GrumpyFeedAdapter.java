package is.grumpy.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import is.grumpy.R;
import is.grumpy.contracts.GrumpyFeedData;
import is.grumpy.utils.BitmapTask;

/**
 * Created by Arnar on 4.2.2014.
 */
public class GrumpyFeedAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutResourceId;
    private List<GrumpyFeedData> feed;
    private LruCache<String, Bitmap> mMemoryCache;

    public GrumpyFeedAdapter(Context context, int layoutResourceId, List<GrumpyFeedData> feed)
    {
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.feed = feed;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        //Key will be UserId when the Rest API is ready
        // Use 1/8th of the available memory for this memory cache.
        mMemoryCache = new LruCache<String, Bitmap>(maxMemory)
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap)
            {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap)
    {
        if (getBitmapFromMemCache(key) == null)
        {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key)
    {
        return mMemoryCache.get(key);
    }

    static class GrumpyFeedHolder
    {
        ImageView profilePicture;
        TextView userName;
        TextView post;
        TextView timeCreated;
        int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final GrumpyFeedHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GrumpyFeedHolder();
            holder.userName = (TextView) row.findViewById(R.id.grumpyFeedUserName);
            holder.profilePicture = (ImageView) row.findViewById(R.id.feedProfilePicture);
            holder.post = (TextView) row.findViewById(R.id.grumpyFeedPost);
            holder.timeCreated = (TextView) row.findViewById(R.id.grumpyFeedTimeCreated);
            row.setTag(holder);
        }
        else
        {
            holder = (GrumpyFeedHolder)row.getTag();
        }

        final GrumpyFeedData feed = getItem(position);

        if(getBitmapFromMemCache(feed.getProfilePicture()) == null)
        {
            new ProfilePictureWorker(feed.getProfilePicture(), position).execute(holder);
        }
        else
        {
            holder.profilePicture.setImageBitmap(getBitmapFromMemCache(feed.getProfilePicture()));
        }

        holder.position = position;
        holder.userName.setText(feed.getUserName());
        holder.post.setText(feed.getPost());
        holder.timeCreated.setText(feed.getTimeCreated());

        return row;
    }

    private class ProfilePictureWorker extends AsyncTask<GrumpyFeedHolder, Void, Bitmap>
    {
        private String posterUrl;
        private GrumpyFeedHolder holder;
        private int position;

        /**
         * @param posterUrl The url of a picture to download
         */
        public ProfilePictureWorker(String posterUrl, int position)
        {
            this.posterUrl = posterUrl;
            this.position = position;
        }

        @Override

        protected Bitmap doInBackground(GrumpyFeedHolder... view)
        {
            holder = view[0];
            return GetPoster();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (holder.position == position)
            {
                holder.profilePicture.setImageBitmap(bitmap);
                addBitmapToMemoryCache(getItem(position).getProfilePicture(), bitmap);
            }
        }

        private Bitmap GetPoster()
        {
            try
            {
                return BitmapTask.getBitmapFromUrl(posterUrl);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }
    }

    @Override
    public int getCount()
    {
        return (feed == null) ? 0 : feed.size();
    }

    @Override
    public GrumpyFeedData getItem(int position)
    {
        return feed.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return feed.indexOf(getItem(position));
    }
}
