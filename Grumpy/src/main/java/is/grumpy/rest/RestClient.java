package is.grumpy.rest;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arnar on 4.2.2014.
 */
public class RestClient implements IRestClient
{
    @Override
    public String Get(String resource) throws Exception
    {
        InputStream is = null;

        try
        {
            URL url = new URL(resource);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(HttpMethods.GET.toString());
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return ConvertStreamToString(is);
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    private String ConvertStreamToString(InputStream stream) throws Exception
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line);
        }

        return sb.toString();
    }

    enum HttpMethods
    {
        GET,
        PUT,
        POST,
        DELETE
    }
}
