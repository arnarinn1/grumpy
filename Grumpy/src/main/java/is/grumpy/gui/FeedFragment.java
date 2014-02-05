package is.grumpy.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.GrumpyFeedAdapter;
import is.grumpy.contracts.GrumpyFeedData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.rest.GrumpyClient;

/**
 * Created by Arnar on 4.2.2014.
 */
public class FeedFragment extends BaseFragment
{
    private ListView mListView;

    public static FeedFragment newInstance()
    {
        FeedFragment fragment = new FeedFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mListView = (ListView) getView().findViewById(R.id.listViewGrumpyFeed);

        new GrumpyFeedWorker().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    private class GrumpyFeedWorker extends AsyncTask<String, Void, List<GrumpyFeedData>>
    {
        @Override
        protected List<GrumpyFeedData> doInBackground(String... params)
        {
            try
            {
                return new GrumpyClient().GetGrumpyFeed();
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<GrumpyFeedData> grumpyFeed)
        {
            if (grumpyFeed != null)
            {
                GrumpyFeedAdapter adapter = new GrumpyFeedAdapter(IActivity.context(), R.layout.listview_feed, grumpyFeed);

                SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
                swingBottomInAnimationAdapter.setInitialDelayMillis(500);
                swingBottomInAnimationAdapter.setAnimationDelayMillis(300);
                swingBottomInAnimationAdapter.setAbsListView(mListView);

                mListView.setAdapter(swingBottomInAnimationAdapter);
            }
        }
    }
}
