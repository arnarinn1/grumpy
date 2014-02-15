package is.grumpy.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.GrumpyFeedAdapter;
import is.grumpy.contracts.GrumpyFeedData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyClient;

/**
 * Created by Arnar on 4.2.2014.
 */
public class FeedFragment extends BaseFragment
{
    private MenuItem refreshMenuItem;
    private ListView mListView;
    private GrumpyFeedAdapter mAdapter;
    private ProgressBar mProgressBar;

    public static FeedFragment newInstance(int position)
    {
        FeedFragment fragment = new FeedFragment();

        Bundle args = new Bundle();
        args.putInt(BaseNavigationDrawer.DRAWER_POSITION, position);

        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        //Notify the fragment to participate in populating the MENU
        setHasOptionsMenu(true);
        mListView = (ListView) getView().findViewById(R.id.listViewGrumpyFeed);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.progressIndicator);

        new GrumpyFeedWorker().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_refresh:
                refreshMenuItem = item;
                new UpdateGrumpyFeedWorker().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class UpdateGrumpyFeedWorker extends AsyncTask<String, Void, List<GrumpyFeedData>>
    {
        @Override
        protected List<GrumpyFeedData> doInBackground(String... params)
        {
            try
            {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            refreshMenuItem.setActionView(R.layout.menu_action_progressbar);
            refreshMenuItem.expandActionView();
        }

        @Override
        protected void onPostExecute(List<GrumpyFeedData> feed)
        {
            if (mAdapter != null)
            {
                GrumpyFeedData testData = GetTestData();
                mAdapter.AddNewItem(testData);
                mAdapter.notifyDataSetChanged();

                refreshMenuItem.collapseActionView();
                // remove the progress bar view
                refreshMenuItem.setActionView(null);
            }
        }

        private GrumpyFeedData GetTestData()
        {
            //Just some hardcoded example to show functionality
            GrumpyFeedData testData = new GrumpyFeedData();
            testData.setPost("This is an test post to show functionality");
            testData.setUserName("Arnarinn");
            testData.setTimeCreated("2014-02-14 00:52:45");
            testData.setProfilePicture("https://notendur.hi.is/~arh36/Grumpy/rest/api/arnar2.jpg");
            return testData;
        }
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
                mAdapter = new GrumpyFeedAdapter(IActivity.context(), R.layout.listview_feed, grumpyFeed);

                SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
                swingBottomInAnimationAdapter.setInitialDelayMillis(500);
                swingBottomInAnimationAdapter.setAnimationDelayMillis(500);
                swingBottomInAnimationAdapter.setAbsListView(mListView);

                mListView.setAdapter(swingBottomInAnimationAdapter);
            }

            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
