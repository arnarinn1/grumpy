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

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.cache.Credentials;
import is.grumpy.contracts.FeedData;
import is.grumpy.contracts.LikeData;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.gui.dialogs.CommentDialog;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import is.grumpy.utils.StringHelper;
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
    private List<FeedData> mFeed;
    private GrumpyService mService;

    public FeedAdapter(Context context, int layoutResourceId, List<FeedData> feed)
    {
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.mFeed = feed;
        Initialize();
    }

    private void Initialize()
    {
        RestAdapter restAdapter = RetrofitUtil.RestAdapterPostInstance(mContext);
        mService = restAdapter.create(GrumpyService.class);
    }

    static class GrumpyFeedHolder
    {
        public GrumpyFeedHolder(View view)
        {
            ButterKnife.inject(this, view);
        }

        @InjectView(R.id.feedProfilePicture)    com.makeramen.RoundedImageView profilePicture;
        @InjectView(R.id.grumpyFeedUserName)    TextView userName;
        @InjectView(R.id.grumpyFeedPost)        TextView post;
        @InjectView(R.id.grumpyFeedTimeCreated) TextView timeCreated;
        @InjectView(R.id.postOptions)           ImageButton showOptions;
        @InjectView(R.id.commentLikeCount)      TextView commentLikeCount;
        @InjectView(R.id.likePost)              Button likePost;
        @InjectView(R.id.commentOnPost)         Button commentPost;
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

            holder = new GrumpyFeedHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder = (GrumpyFeedHolder)row.getTag();
        }

        final FeedData feed = getItem(position);

        SetLikeButtonStatus(holder.likePost, feed.getLikes());

        String picture = feed.getUser().getAvatar();

        Picasso.with(mContext)
                .load(picture == null ? "https://notendur.hi.is/~arh36/Grumpy/rest/api/arnar2.jpg" : picture)
                .noFade()
                .into(holder.profilePicture);

        holder.userName.setText(feed.getUser().getFullName());
        holder.post.setText(feed.getPost());

        String postedAt = StringHelper.FormatDate(mContext, feed.getTimeCreated());
        holder.timeCreated.setText(postedAt);

        holder.showOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RemoveItem(position, feed);
            }
        });

        StringHelper.SetCommentLikeLogic(feed, holder.commentLikeCount);

        holder.commentLikeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                StartCommentDialog(feed);
            }
        });

        holder.likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LikePost(feed, position);
            }
        });

        holder.commentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                StartCommentDialog(feed);
            }
        });

        return row;
    }

    public void AddNewLike(int position, LikeData like)
    {
        mFeed.get(position).getLikes().add(like);
        notifyDataSetChanged();
    }

    private void RemoveItem(int position)
    {
        mFeed.remove(position);
        notifyDataSetChanged();
    }

    private void LikePost(FeedData feed, int position)
    {
        mFeedPosition = position;
        mService.likePost(feed.getId(), likePostCallback);
    }

    private void SetLikeButtonStatus(Button likeButton, List<LikeData> likes)
    {
        String userId = new Credentials(mContext).GetCacheToken(Credentials.mId);

        //Set likeButton blue if current user has liked this post
        for(LikeData like : likes)
        {
            if (like.getUserId().equals(userId))
            {
                likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_good_blue, 0, 0, 0);
                likeButton.setHasTransientState(true);
                return;
            }
        }
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

    private void StartCommentDialog(FeedData feed)
    {
        CommentDialog dialog = CommentDialog.newInstance(feed);
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
            Toast.makeText(mContext, "You can't like this post again", Toast.LENGTH_SHORT).show();
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
            return mService.deletePost(postId);
        }
    }

    @Override
    public int getCount()
    {
        return (mFeed == null) ? 0 : mFeed.size();
    }

    @Override
    public FeedData getItem(int position)
    {
        return mFeed.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return mFeed.indexOf(getItem(position));
    }
}