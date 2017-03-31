
package com.pace.httpclientproxy;

public class HttpClientExecutorFactory {
    private static HttpClientExecutorFactory sInstance;
    private IHttpClientExecutor mHttpClientImpl = null;

    public static HttpClientExecutorFactory get() {
        if (sInstance == null) {
            synchronized (HttpClientExecutorFactory.class) {
                if (sInstance == null) {
                    sInstance = new HttpClientExecutorFactory();
                }
            }
        }
        return sInstance;
    }

    public void regist(IHttpClientExecutor httpClientImpl) {
        mHttpClientImpl = httpClientImpl;
    }

    public IHttpClientExecutor getHttpClientExecutor() {
        return mHttpClientImpl;
    }
}
