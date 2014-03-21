package is.grumpy.gui;

import android.content.Context;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.adapters.FeedAdapter;
import is.grumpy.contracts.UserProfileData;
import is.grumpy.gui.base.BaseFragment;
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
    public static final String EXTRA_USERID = "is.grumpy.gui.USERID";

    @InjectView(R.id.profileLayout) RelativeLayout mLayout;
    @InjectView(R.id.profilePosts) ListView mListView;

    private ImageView mProfilePicture;
    private TextView mFullName;

    public static ProfileFragment newInstance(String userId)
    {
        ProfileFragment fragment = new ProfileFragment();

        Bundle args = new Bundle();
        args.putString(EXTRA_USERID, userId);

        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ViewGroup profileHeader = (ViewGroup) inflater.inflate(R.layout.listview_profile_header, mListView , false);
        mProfilePicture = (ImageView) profileHeader.findViewById(R.id.profilePicture);
        mFullName = (TextView) profileHeader.findViewById(R.id.profileFullName);

        mListView.addHeaderView(profileHeader);

        RestAdapter restAdapter = RetrofitUtil.RestAdapterGetInstance();
        GrumpyService mService = restAdapter.create(GrumpyService.class);

        String userId = getArguments().getString(EXTRA_USERID);
        mService.getUserProfileInfo(userId, userProfileCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.reset(this);
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

                mListView.setAdapter(new FeedAdapter(IActivity.context(), R.layout.listview_profile_feed, user.getPosts()));

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
