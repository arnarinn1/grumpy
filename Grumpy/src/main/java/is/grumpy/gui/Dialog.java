package is.grumpy.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import is.grumpy.cache.Credentials;
import is.grumpy.contracts.GrumpyPostRequest;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.rest.GrumpyApi;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 20.2.2014.
 */
public class Dialog
{
    public static final int THEME_HOLO_DARK = 2;

    public static void LoginFailedDialog(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, THEME_HOLO_DARK);
        builder
                .setTitle("Login Failed")
                .setMessage("Whoops, Couldn't log you in")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.show();
        alert.show();
    }
}
