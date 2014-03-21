package is.grumpy.gui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.adapters.FeedAdapter;
import is.grumpy.contracts.FeedData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.RestAdapter;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Arnar on 4.2.2014.
 */
public class FeedFragment extends BaseFragment implements OnRefreshListener
{
    public static final int RefreshCallback = 1;

    @InjectView(R.id.listViewGrumpyFeed) ListView mListView;
    @InjectView(R.id.no_network)         TextView mNoNetworkView;
    @InjectView(R.id.progressIndicator)  ProgressBar mProgressBar;
    @InjectView(R.id.refreshLayout)      PullToRefreshLayout mPullToRefreshLayout;

    private FeedAdapter mAdapter;
    private GrumpyService mGrumpyApi;
    private MenuItem refreshMenuItem;

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

        ActionBarPullToRefresh.from((Activity)IActivity.context())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        RestAdapter restAdapter = RetrofitUtil.RestAdapterGetInstance();

        mGrumpyApi = restAdapter.create(GrumpyService.class);

        AttachBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.inject(this, rootView);
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
                new MenuGrumpyFeedWorker().execute();
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
        new GrumpyFeedWorker().execute();
    }

    private class GrumpyFeedWorker extends AsyncTask<String, Void, List<FeedData>>
    {
        @Override
        protected List<FeedData> doInBackground(String... params)
        {
            try
            {
                return mGrumpyApi.getPosts();
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<FeedData> grumpyFeed)
        {
            mPullToRefreshLayout.setRefreshComplete();

            if (grumpyFeed != null)
            {
                mAdapter = new FeedAdapter(IActivity.context(), R.layout.listview_launcher_feed, grumpyFeed);

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
        Bundle newPostBundle = ActivityOptions.makeCustomAnimation(IActivity.context(),
                               R.anim.slide_in_bottom, R.anim.slide_out_top).toBundle();
        startActivityForResult(intent, RefreshCallback, newPostBundle);
    }

    private class MenuGrumpyFeedWorker extends AsyncTask<String, Void, List<FeedData>>
    {
        @Override
        protected List<FeedData> doInBackground(String... params)
        {
            try
            {
                //Simulate sleep for 1 sec to show the menu rotation
                Thread.sleep(1000);
                return mGrumpyApi.getPosts();
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        @Override
        protected void onPreExecute()
        {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

            Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.clockwise_refresh);
            iv.startAnimation(rotation);

            refreshMenuItem.setActionView(iv);
            refreshMenuItem.expandActionView();
        }

        @Override
        protected void onPostExecute(List<FeedData> feed)
        {
            if (mAdapter != null)
            {
                mAdapter = new FeedAdapter(IActivity.context(), R.layout.listview_launcher_feed, feed);

                SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
                swingBottomInAnimationAdapter.setInitialDelayMillis(500);
                swingBottomInAnimationAdapter.setAnimationDelayMillis(500);
                swingBottomInAnimationAdapter.setAbsListView(mListView);

                mListView.setAdapter(swingBottomInAnimationAdapter);
            }

            // remove the progress bar view and animation
            refreshMenuItem.collapseActionView();
            refreshMenuItem.getActionView().clearAnimation();
            refreshMenuItem.setActionView(null);
        }
    }

    //TODO: Extract Broadcast to another file
    private void AttachBroadcastReceiver()
    {
        if (getActivity() == null) return;

        getActivity().registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo wifiStatus   = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobileStatus = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                boolean wifiNetworkStatus = (wifiStatus != null && wifiStatus.isConnected());
                boolean mobileNetworkStatus = (mobileStatus != null && mobileStatus.isConnected());

                if(wifiNetworkStatus || mobileNetworkStatus)
                {
                    new GrumpyFeedWorker().execute();
                    mNoNetworkView.setVisibility(View.GONE);
                }
                else
                {
                    mNoNetworkView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
        , new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RefreshCallback && resultCode == getActivity().RESULT_OK)
        {
            new GrumpyFeedWorker().execute();
        }
    }
}
