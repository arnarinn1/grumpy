package is.grumpy.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import is.grumpy.contracts.LoginResponse;
import is.grumpy.contracts.UserData;

/**
 * Created by Arnar on 20.2.2014.
 */
public class Credentials
{
    //Todo: Create SharedPreferences instance in constructor

    public static final String mUsername = "is.grumpy.cache.USERNAME";
    public static final String mFullName = "is.grumpy.cache.FULLNAME";
    public static final String mId = "is.grumpy.cache.ID";
    public static final String mAccessToken = "is.grumpy.cache.ACCESSTOKEN";
    public static final String mAvatar = "is.grumpy.cache.AVATAR";

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

    public String GetCacheToken(String cacheToken)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(cacheToken, null);
    }

    public void WriteCredentialsToCache(LoginResponse response)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(mUsername, response.getUser().getUsername());
        editor.putString(mFullName, String.format("%s %s", response.getUser().getFirstName(), response.getUser().getLastName()));
        editor.putString(mAccessToken, response.getAccessToken());
        editor.putString(mId, response.getUser().getId());
        editor.putString(mAvatar, response.getUser().getAvatar());
        editor.commit();
    }

    public void updateCache(UserData response)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(mFullName, String.format("%s %s", response.getFirstName(), response.getLastName()));
        editor.commit();
    }

    public void ClearCredentialsCache()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        preferences.edit()
                   .clear()
                   .commit();
    }
}
