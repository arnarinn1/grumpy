package is.grumpy.client;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import is.grumpy.cache.Credentials;
import is.grumpy.contracts.PostRequest;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.gui.LauncherActivity;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 21.2.2014.
 */
public class LogOutUser
{
    private Context mContext;

    public LogOutUser(Context context)
    {
        this.mContext = context;
        LogOut();
    }

    private void LogOut()
    {
        RestAdapter restAdapter = RetrofitUtil.RestAdapterPostInstance(mContext);

        GrumpyService service = restAdapter.create(GrumpyService.class);

        service.logOutUser(logoutCallback);
    }

    Callback<ServerResponse> logoutCallback = new Callback<ServerResponse>()
    {
        @Override
        public void success(ServerResponse serverResponse, Response response)
        {
            if (serverResponse.getStatus())
            {
                new Credentials(mContext).ClearCredentialsCache();
                Intent intent = new Intent(mContext, LauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            }
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(mContext, "Couldn't log you out", Toast.LENGTH_SHORT).show();
        }
    };
}
