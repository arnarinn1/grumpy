package is.grumpy.gui.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.CommentsAdapter;
import is.grumpy.contracts.FeedData;
import is.grumpy.contracts.LikeData;
import is.grumpy.contracts.UserData;

/**
 * Created by Arnar on 6.3.2014.
 */
public class CommentLikeDialog extends DialogFragment
{
    public static final String EXTRA_FEED = "is.grumpy.gui.dialogs.FEED";

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        FeedData feed = (FeedData) getArguments().getSerializable(EXTRA_FEED);

        final View mLayout = inflater.inflate(R.layout.dialog_comments, null);

        String likes = CreateLikeText(feed.getLikes());

        ((TextView) mLayout.findViewById(R.id.likeCounter)).setText(likes);
        ((ListView) mLayout.findViewById(R.id.postComments)).setAdapter(new CommentsAdapter(getActivity(), feed.getComments()));

        return mLayout;
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
}
