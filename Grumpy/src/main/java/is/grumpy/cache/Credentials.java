package is.grumpy.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import is.grumpy.contracts.LoginResponse;

/**
 * Created by Arnar on 20.2.2014.
 */
public class Credentials
{
    public static final String mUsername = "is.grumpy.cache.USERNAME";
    public static final String mId = "is.grumpy.cache.ID";
    public static final String mAccessToken = "is.grumpy.cache.ACCESSTOKEN";

    public Context mContext;

    public Credentials(Context mContext)
    {
        this.mContext = mContext;
    }

    public boolean UserLoggedIn()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(mAccessToken, null) != null;
    }

    public void WriteCredentialsToCache(LoginResponse response)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(mUsername, response.getUser().getUsername());
        editor.putString(mId, response.getUser().getId());
        editor.putString(mAccessToken, response.getAccessToken());
        editor.commit();
    }
}
