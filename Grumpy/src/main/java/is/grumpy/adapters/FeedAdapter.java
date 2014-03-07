package is.grumpy.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import is.grumpy.R;
import is.grumpy.cache.Credentials;
import is.grumpy.contracts.FeedData;
import is.grumpy.contracts.LikeData;
import is.grumpy.contracts.PostRequest;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.gui.dialogs.CommentLikeDialog;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import is.grumpy.utils.DateCleaner;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 4.2.2014.
 */
public class FeedAdapter extends BaseAdapter
{
    private static int mFeedPosition;

    private Context mContext;
    private int layoutResourceId;
    private List<FeedData> feed;

    public FeedAdapter(Context context, int layoutResourceId, List<FeedData> feed)
    {
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.feed = feed;
    }

    static class GrumpyFeedHolder
    {
        com.makeramen.RoundedImageView profilePicture;
        TextView userName;
        TextView post;
        TextView timeCreated;
        ImageButton showOptions;
        TextView commentLikeCount;
        Button likePost;
        Button commentPost;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final GrumpyFeedHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GrumpyFeedHolder();
            holder.userName = (TextView) row.findViewById(R.id.grumpyFeedUserName);
            holder.profilePicture = (com.makeramen.RoundedImageView) row.findViewById(R.id.feedProfilePicture);
            holder.post = (TextView) row.findViewById(R.id.grumpyFeedPost);
            holder.timeCreated = (TextView) row.findViewById(R.id.grumpyFeedTimeCreated);
            holder.showOptions = (ImageButton) row.findViewById(R.id.postOptions);
            holder.commentLikeCount = (TextView) row.findViewById(R.id.commentLikeCount);
            holder.likePost = (Button) row.findViewById(R.id.likePost);
            holder.commentPost = (Button) row.findViewById(R.id.commentOnPost);
            row.setTag(holder);
        }
        else
        {
            holder = (GrumpyFeedHolder)row.getTag();
        }

        final FeedData feed = getItem(position);

        String picture = feed.getUser().getAvatar();

        Picasso.with(mContext)
                .load(picture == null ? "https://notendur.hi.is/~arh36/Grumpy/rest/api/arnar2.jpg" : picture)
                .noFade()
                .into(holder.profilePicture);

        holder.userName.setText(feed.getUser().getFullName());
        holder.post.setText(feed.getPost());

        String postedAt = FormatDate(feed.getTimeCreated());
        holder.timeCreated.setText(postedAt);

        holder.showOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RemoveItem(position, feed);
            }
        });

        SetCommentLikeLogic(feed, holder.commentLikeCount);

        holder.commentLikeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                StartCommentLikeDialog(feed);
            }
        });

        holder.likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(mContext, "Sauron rules", Toast.LENGTH_SHORT).show();
                LikePost(feed, position);
            }
        });

        holder.commentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                StartCommentLikeDialog(feed);
            }
        });

        return row;
    }

    public void AddNewItem(FeedData data)
    {
        feed.add(0, data);
    }

    public void AddNewLike(int position, LikeData like)
    {
        feed.get(position).getLikes().add(like);
        notifyDataSetChanged();
    }

    private void RemoveItem(int position)
    {
        feed.remove(position);
        notifyDataSetChanged();
    }

    private void LikePost(FeedData feed, int position)
    {
        RestAdapter restAdapter = RetrofitUtil.GetRetrofitRestAdapter();
        GrumpyService mService = restAdapter.create(GrumpyService.class);

        PostRequest request = new PostRequest();
        request.setAccessToken(new Credentials(mContext).GetCacheToken(Credentials.mAccessToken));

        mFeedPosition = position;
        mService.likePost(feed.getId(), request, likePostCallback);
    }

    public void RemoveItem(final int position, final FeedData post)
    {
        String userName = new Credentials(mContext).GetCacheToken(Credentials.mUsername);

        boolean userCreatedPost = userName.equals(post.getUser().getUsername());

        String message;

        if (userCreatedPost)
            message = "Are you sure you want to destroy this post ?";
        else
            message = "You can only destroy your own posts";

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, 2);
        builder
            .setTitle("Destroy Post")
            .setMessage(message);

        if (userCreatedPost)
        {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    new DeletePostWorker(position).execute(post.getId());

                    dialog.dismiss();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        else
        {
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
        }

        AlertDialog alert = builder.show();
        alert.show();
    }

    private void StartCommentLikeDialog(FeedData feed)
    {
        CommentLikeDialog dialog = CommentLikeDialog.newInstance(feed);
        dialog.show(((Activity) mContext).getFragmentManager(), "dialog");
    }

    Callback<LikeData> likePostCallback = new Callback<LikeData>()
    {
        @Override
        public void success(final LikeData like, Response response)
        {
            AddNewLike(mFeedPosition, like);
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(mContext, "Negative", Toast.LENGTH_SHORT).show();
        }
    };

    private class DeletePostWorker extends AsyncTask<String, Void, ServerResponse>
    {
        private int position;
        private ProgressDialog mProgressDialog;

        public DeletePostWorker(int position)
        {
            this.position = position;
            this.mProgressDialog = new ProgressDialog(mContext, 2);
            mProgressDialog.setMessage("Deleting Post");
        }

        @Override
        protected ServerResponse doInBackground(String... params)
        {
            try
            {
                return DestroyPost(params[0]);
            }
            catch (Exception ex)
            {
                //Show some toast with error message
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(ServerResponse response)
        {
            if(response.getStatus())
            {
                RemoveItem(position);
            }

            mProgressDialog.dismiss();
        }

        private ServerResponse DestroyPost(String postId)
        {
            RestAdapter restAdapter = RetrofitUtil.GetRetrofitRestAdapter();
            GrumpyService service = restAdapter.create(GrumpyService.class);

            String accessToken = new Credentials(mContext).GetCacheToken(Credentials.mAccessToken);

            return service.deletePost(postId, accessToken);
        }
    }

    private String FormatDate(String timeCreated)
    {
        String posted;
        try
        {
            DateCleaner cleaner = new DateCleaner(timeCreated, mContext);
            posted = cleaner.getRelativeDate();
        }
        catch (Exception e)
        {
            posted = timeCreated;
        }

        return posted;
    }

    private void SetCommentLikeLogic(FeedData feed, TextView commentLikeTextView)
    {
        String commentLikes = null;
        String commentArray[] = {"comment", "comments"};
        String likeArray[] = {"like", "likes"};

        if(feed.getComments().size() > 0)
        {
            int commentSize = feed.getComments().size();
            commentLikes = String.format("%d %s", commentSize, commentArray[commentSize == 1 ? 0 : 1]);
        }

        if (feed.getLikes().size() > 0)
        {
            int likesSize = feed.getLikes().size();

            if (commentLikes != null)
                commentLikes += (String.format(" %d %s", likesSize, likeArray[likesSize == 1 ? 0 : 1]));
            else
                commentLikes = String.format("%d %s", likesSize, likeArray[likesSize == 1 ? 0 : 1]);
        }

        if (commentLikes != null)
            commentLikeTextView.setText(commentLikes);

        commentLikeTextView.setVisibility(commentLikes == null ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getCount()
    {
        return (feed == null) ? 0 : feed.size();
    }

    @Override
    public FeedData getItem(int position)
    {
        return feed.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return feed.indexOf(getItem(position));
    }
}
