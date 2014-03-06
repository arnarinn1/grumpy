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
import is.grumpy.contracts.CommentData;
import is.grumpy.contracts.FeedData;
import is.grumpy.gui.base.IActivity;


/**
 * Created by Arnar on 6.3.2014.
 */
public class CommentLikeDialog extends DialogFragment
{
    public static CommentLikeDialog newInstance(FeedData feed)
    {
        CommentLikeDialog f = new CommentLikeDialog();
        Bundle args = new Bundle();
        args.putSerializable("feed", feed);
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
        FeedData feed = (FeedData) getArguments().getSerializable("feed");

        final View mLayout = inflater.inflate(R.layout.dialog_comments, null);

        ((ListView) mLayout.findViewById(R.id.postComments)).setAdapter(new CommentsAdapter(getActivity(), feed.getComments()));

        return mLayout;
    }
}
