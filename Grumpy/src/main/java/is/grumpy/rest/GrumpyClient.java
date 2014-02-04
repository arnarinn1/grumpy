package is.grumpy.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.grumpy.contracts.GrumpyFeedData;

/**
 * Created by Arnar on 4.2.2014.
 */
public class GrumpyClient
{
    private RestClient mRestClient;

    public GrumpyClient()
    {
        this.mRestClient = new RestClient();
    }

    public List<GrumpyFeedData> GetGrumpyFeed()
    {
        try
        {
            String grumpyFeedUrl = "https://notendur.hi.is/~arh36/Grumpy/rest/api/test_feed.php";
            String content = mRestClient.Get(grumpyFeedUrl);

            Type listType = new TypeToken<ArrayList<GrumpyFeedData>>() {}.getType();

            return new Gson().fromJson(content, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error getting feed");
        }

        return null;
    }
}
