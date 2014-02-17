package is.grumpy.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import is.grumpy.contracts.GrumpyFeedData;
import is.grumpy.contracts.MessagesData;

/**
 * Created by Arnar on 4.2.2014.
 */
public class GrumpyClient<T>
{
    private RestClient mRestClient;
    private final String mClientHost = "arnarh.com/grumpy/public/";

    public GrumpyClient()
    {
        this.mRestClient = new RestClient();
    }

    public List<GrumpyFeedData> GetGrumpyFeed()
    {
        try
        {
            String grumpyFeedUrl = "https://notendur.hi.is/~thg112/test_feed.json";
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

    public List<MessagesData> GetMessages()
    {
        try
        {
            String grumpyFeedUrl = "https://notendur.hi.is/~arh36/Grumpy/rest/api/test_messages.php";
            String content = mRestClient.Get(grumpyFeedUrl);

            Type listType = new TypeToken<ArrayList<MessagesData>>() {}.getType();

            return new Gson().fromJson(content, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error getting feed");
        }

        return null;
    }

    public List<T> GetDataSource()
    {
        try
        {
            String grumpyFeedUrl = "https://notendur.hi.is/~arh36/Grumpy/rest/api/test_messages.php";
            String content = mRestClient.Get(grumpyFeedUrl);

            Type listType = new TypeToken<ArrayList<T>>() {}.getType();

            return new Gson().fromJson(content, listType);
        }
        catch (Exception ex)
        {
            Log.e(getClass().getName(), "Error getting feed");
        }

        return null;
    }
}
