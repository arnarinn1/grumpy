package is.grumpy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.cache.Credentials;
import is.grumpy.contracts.PostRequest;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserData;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 15.2.2014.
 */
public class SearchAdapter extends BaseAdapter
{
    private static final int mLayoutResourceId = R.layout.listview_search_user;

    private Context mContext;
    private List<UserData> users;
    private GrumpyService mService;

    public SearchAdapter(Context context, List<UserData> users)
    {
        this.mContext = context;
        this.users = users;

        RestAdapter restAdapter = RetrofitUtil.RestAdapterPostInstance(context);
        mService = restAdapter.create(GrumpyService.class);
    }

    static class UserHolder
    {
        public UserHolder(View view)
        {
            ButterKnife.inject(this ,view);
        }

        @InjectView(R.id.userName)           TextView username;
        @InjectView(R.id.fullName)           TextView fullname;
        @InjectView(R.id.userProfilePicture) ImageView profilePicture;
        @InjectView(R.id.ivFollowUser)       ImageView followUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final UserHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new UserHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder = (UserHolder) row.getTag();
        }

        final UserData user = getItem(position);

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFirstName() + " " + user.getLastName());

        String avatar = user.getAvatar();

        Picasso.with(mContext)
                .load(avatar == null ? "https://notendur.hi.is/~arh36/Grumpy/rest/api/arnar2.jpg" : avatar)
                .into(holder.profilePicture);

        holder.followUser.setVisibility(View.VISIBLE);

        if (user.getFollowing()) {
            holder.followUser.setVisibility(View.GONE);
        }

        holder.followUser.setHasTransientState(true);
        holder.followUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                holder.followUser.setVisibility(View.GONE);
                mService.followUser(user.getId(), followUserCallback);
            }
        });

        return row;
    }

    @Override
    public int getCount()
    {
        return (users == null) ? 0 : users.size();
    }

    @Override
    public UserData getItem(int position)
    {
        return users.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return users.indexOf(getItem(position));
    }

    Callback<ServerResponse> followUserCallback = new Callback<ServerResponse>()
    {
        @Override
        public void success(ServerResponse serverResponse, Response response)
        {
            if(serverResponse.getStatus())
                Toast.makeText(mContext, "Followed User", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(mContext, "Whoops", Toast.LENGTH_SHORT).show();
        }
    };
}
