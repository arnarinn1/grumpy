package is.grumpy.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import is.grumpy.cache.Credentials;
import is.grumpy.contracts.GrumpyPostRequest;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.rest.GrumpyApi;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 21.2.2014.
 */
public class LogOutDialog
{
    public static final int THEME_HOLO_DARK = 2;
    public static final String ApiUrl = "http://arnarh.com/grumpy/public";

    private Context mContext;

    public LogOutDialog(Context context)
    {
        this.mContext = context;
    }

    public void LogOut()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, THEME_HOLO_DARK);
        builder
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        LogOutUser();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.show();
        alert.show();
    }

    private void LogOutUser()
    {
        RestAdapter restAdapter = RetrofitUtil.GetRetrofitRestAdapter();

        GrumpyApi service = restAdapter.create(GrumpyApi.class);

        String accessToken = new Credentials(mContext).GetCacheToken(Credentials.mAccessToken);
        GrumpyPostRequest request = new GrumpyPostRequest();
        request.setAccessToken(accessToken);

        service.logOutUser(request, logoutCallback);
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
