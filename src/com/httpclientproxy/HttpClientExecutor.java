
package com.httpclientproxy;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.IOException;

public class HttpClientExecutor implements IHttpClientExecutor {

    @Override
    public HttpResponseWrap execute(BasicHttpParams httpClientParam, HttpPostWrap httpPost) {
        HttpClient client = new DefaultHttpClient(httpClientParam);
        try {
            HttpResponse response = client.execute(HttpPostWrap.unwrap(httpPost));
            return HttpResponseWrap.wrap(response);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
