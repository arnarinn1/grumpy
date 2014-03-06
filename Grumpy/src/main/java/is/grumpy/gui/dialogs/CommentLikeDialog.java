package is.grumpy.gui.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.CommentsAdapter;
import is.grumpy.cache.Credentials;
import is.grumpy.contracts.CommentData;
import is.grumpy.contracts.FeedData;
import is.grumpy.contracts.LikeData;
import is.grumpy.contracts.PostRequest;
import is.grumpy.contracts.UserData;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 6.3.2014.
 */
public class CommentLikeDialog extends DialogFragment
{
    public static final String EXTRA_FEED = "is.grumpy.gui.dialogs.FEED";

    private EditText mEditText;
    private ImageButton mPostComment;
    private GrumpyService mService;
    private CommentsAdapter mAdapter;

    public static CommentLikeDialog newInstance(FeedData feed)
    {
        CommentLikeDialog f = new CommentLikeDialog();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_FEED, feed);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, R.style.CommentLikeDialog);
    }

    //Todo: Refactor this method, this is crowded
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final FeedData feed = (FeedData) getArguments().getSerializable(EXTRA_FEED);

        final View mLayout = inflater.inflate(R.layout.dialog_comments, null);

        mAdapter = new CommentsAdapter(getActivity(), feed.getComments());
        String likes = CreateLikeText(feed.getLikes());

        ((TextView) mLayout.findViewById(R.id.likeCounter)).setText(likes);
        ((ListView) mLayout.findViewById(R.id.postComments)).setAdapter(mAdapter);

        RestAdapter restAdapter = RetrofitUtil.GetRetrofitRestAdapter();
        mService = restAdapter.create(GrumpyService.class);

        mEditText = (EditText) mLayout.findViewById(R.id.editTextComment);
        mPostComment = (ImageButton) mLayout.findViewById(R.id.postComment);

        mPostComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PostNewComment(feed.getId());
            }
        });

        return mLayout;
    }

    private void PostNewComment(String postId)
    {
        PostRequest request = new PostRequest();
        request.setAccessToken(new Credentials(getActivity()).GetCacheToken(Credentials.mAccessToken));
        request.setComment(mEditText.getText().toString());
        mService.postNewComment(postId, request, postNewCommentCallback);
    }

    private String CreateLikeText(List<LikeData> likes)
    {
        int commentSize = likes.size();

        if (commentSize == 1)
        {
            UserData user = likes.get(0).getUser();
            return String.format("%s %s likes this", user.getFirstName(), user.getLastName());
        }

        return String.format("%s people like this", commentSize);
    }

    Callback<CommentData> postNewCommentCallback = new Callback<CommentData>()
    {
        @Override
        public void success(CommentData commentData, Response response)
        {
            mAdapter.AddNewItem(commentData);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(getActivity(), "Godzilla has destroyed Tokyo", Toast.LENGTH_SHORT).show();
        }
    };
}
