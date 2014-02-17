package is.grumpy.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.GrumpyFeedAdapter;
import is.grumpy.contracts.GrumpyFeedData;
import is.grumpy.contracts.GrumpyUserData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.gui.base.IActivity;
import is.grumpy.rest.GrumpyClient;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Arnar on 4.2.2014.
 */
public class FeedFragment extends BaseFragment implements OnRefreshListener
{
    private MenuItem refreshMenuItem;
    private ListView mListView;
    private GrumpyFeedAdapter mAdapter;
    private ProgressBar mProgressBar;

    private PullToRefreshLayout mPullToRefreshLayout;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        //Notify the fragment to participate in populating the MENU
        setHasOptionsMenu(true);

        mListView = (ListView) rootView.findViewById(R.id.listViewGrumpyFeed);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressIndicator);
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresLayout);

        ActionBarPullToRefresh.from((Activity)IActivity.context())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        new GrumpyFeedWorker().execute();

        return rootView;
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
            case R.id.action_new_post:
                StartNewPostActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefreshStarted(View view)
    {
        /**
         * Simulate Refresh with 4 seconds sleep
         */
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result)
            {
                super.onPostExecute(result);

                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();

                if (mAdapter != null)
                {
                    GrumpyFeedData testData = GetTestData();
                    mAdapter.AddNewItem(testData);
                    mAdapter.notifyDataSetChanged();
                }

            }
        }.execute();
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
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

            Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.clockwise_refresh);
            rotation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(rotation);

            refreshMenuItem.setActionView(iv);
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
            }

            // remove the progress bar view and animation
            refreshMenuItem.collapseActionView();
            refreshMenuItem.getActionView().clearAnimation();
            refreshMenuItem.setActionView(null);
        }
    }

    private GrumpyFeedData GetTestData()
    {
        //Just some hardcoded example to show functionality
        GrumpyFeedData testData = new GrumpyFeedData();
        GrumpyUserData testUser = new GrumpyUserData();
        testData.setPost("This is an test post to show functionality");
        testData.setUser(testUser);
        testUser.setUsername("Arnarinn");
        testUser.setFirstName("Arnar");
        testUser.setLastName("Heimisson");
        testData.setTimeCreated("2014-02-14 14:52:45");
        testData.getUser().setAvatar("https://notendur.hi.is/~arh36/Grumpy/rest/api/arnar2.jpg");
        return testData;
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

    private void StartNewPostActivity()
    {
        Intent intent = new Intent(IActivity.context(), NewPostActivity.class);
        startActivity(intent);
        ((Activity)IActivity.context()).overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}
