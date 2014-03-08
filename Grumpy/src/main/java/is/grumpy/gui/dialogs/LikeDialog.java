package is.grumpy.gui.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import is.grumpy.R;

import is.grumpy.adapters.LikesAdapter;
import is.grumpy.contracts.CommentData;
import is.grumpy.contracts.LikeData;

/**
 * Created by Arnar on 8.3.2014.
 */
public class LikeDialog extends DialogFragment
{
    public static final String EXTRA_LIKES = "is.grumpy.gui.dialogs.LIKES";

    public static LikeDialog newInstance(ArrayList<CommentData> likes)
    {
        LikeDialog dialog = new LikeDialog();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LIKES, likes);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, R.style.CommentLikeDialog_SlideInAnimation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final List<LikeData> likes = (List<LikeData>) getArguments().getSerializable(EXTRA_LIKES);

        final View mLayout = inflater.inflate(R.layout.dialog_likes, null);

        ListView mListView = (ListView) mLayout.findViewById(R.id.postLikes);

        LikesAdapter adapter = new LikesAdapter(getActivity(), likes);
        mListView.setAdapter(adapter);

        return mLayout;
    }
}
