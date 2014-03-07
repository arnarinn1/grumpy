package is.grumpy.rest;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;

/**
 * Created by Arnar on 21.2.2014.
 */
public class RetrofitUtil
{
    public static final String ApiUrl = "http://arnarh.com/grumpy/public";

    public static RestAdapter GetRetrofitRestAdapter()
    {
        //NOTE: There seems to be some bug in Retrofit(Might be an Android Bug).  If Connection header is not set to Close
        //      and the client is not set to ApacheClient, java will throw an EOFException.  This seems to work for now
        //      but will need to keep a close a eye on this one.

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request)
            {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
                request.addHeader("Accept-Encoding", "" );
                request.addHeader("Connection", "Close");
            }
        };

        return new
                RestAdapter.Builder()
                .setEndpoint(ApiUrl)
                .setClient(new ApacheClient())
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public static RestAdapter RestAdapterGetInstance()
    {
        return new RestAdapter.Builder()
                .setEndpoint(ApiUrl)
                .setLogLevel(RestAdapter.LogLevel.FULL  )
                .build();
    }
}
