package is.grumpy.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import is.grumpy.R;
import is.grumpy.adapters.GrumpyFeedAdapter;
import is.grumpy.cache.Credentials;
import is.grumpy.contracts.UserProfileData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 3.3.2014.
 */
public class ProfileFragment extends BaseFragment
{
    private ImageView mProfilePicture;
    private TextView mFullName;
    private RelativeLayout mLayout;
    private ListView mListView;

    private GrumpyService mService;

    public static ProfileFragment newInstance(int position)
    {
        ProfileFragment fragment = new ProfileFragment();

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

        mProfilePicture = (ImageView) getView().findViewById(R.id.profilePicture);
        mFullName = (TextView) getView().findViewById(R.id.profileFullName);
        mLayout = (RelativeLayout) getView().findViewById(R.id.profileLayout);
        mListView = (ListView) getView().findViewById(R.id.profilePosts);

        RestAdapter restAdapter = RetrofitUtil.RestAdapterGetInstance();

        String userId = new Credentials(IActivity.context()).GetCacheToken(Credentials.mId);
        mService = restAdapter.create(GrumpyService.class);
        mService.getUserProfileInfo(userId, userProfileCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    Callback<UserProfileData> userProfileCallback = new Callback<UserProfileData>()
    {
        @Override
        public void success(UserProfileData user, Response response)
        {
            if (user.getUser() != null)
            {
                Picasso.with(IActivity.context())
                        .load(user.getUser().getAvatar())
                        .into(mProfilePicture);

                mFullName.setText(user.getUser().getFullName());

                mListView.setAdapter(new GrumpyFeedAdapter(IActivity.context(), R.layout.listview_feed, user.getPosts()));

                mLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(IActivity.context(), "THE WORLD HAS ENDED!", Toast.LENGTH_SHORT).show();
        }
    };
}
