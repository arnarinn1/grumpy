package is.grumpy.gui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.adapters.FollowingAdapter;
import is.grumpy.cache.Credentials;
import is.grumpy.contracts.FollowingData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 15.3.2014.
 */
public class FollowingFragment extends BaseFragment implements AdapterView.OnItemClickListener
{
    @InjectView(R.id.lvFollowingUsers) ListView mListView;
    @InjectView(R.id.tvFollowersText) TextView mFollowersText;

    public static FollowingFragment newInstance(int position)
    {
        FollowingFragment f = new FollowingFragment();

        Bundle args = new Bundle();
        args.putInt(BaseNavigationDrawer.DRAWER_POSITION, position);

        f.setRetainInstance(true);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnItemClickListener(this);

        RestAdapter restAdapter = RetrofitUtil.RestAdapterGetInstance();
        GrumpyService mService = restAdapter.create(GrumpyService.class);

        String userId = new Credentials(IActivity.context()).GetCacheToken(Credentials.mId);
        mService.getFollowingUsers(userId, followingCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_following, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    Callback<List<FollowingData>> followingCallback = new Callback<List<FollowingData>>()
    {
        @Override
        public void success(List<FollowingData> followingData, Response response)
        {
            mFollowersText.setText(String.format("Following %s Users", followingData.size()));

            if (!followingData.isEmpty())
            {
                mFollowersText.setText(String.format("Following %s Users", followingData.size()));
                mListView.setAdapter(new FollowingAdapter(IActivity.context(), followingData));
            }
            else
            {
                mFollowersText.setText("Looser, you are following no user");
            }
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(IActivity.context(), "Oooops, something went wrong", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        FollowingData user = (FollowingData) mListView.getAdapter().getItem(position);

        ProfileFragment fragment = ProfileFragment.newInstance(user.getUser().getId());

        FragmentManager manager = getFragmentManager();

        manager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
