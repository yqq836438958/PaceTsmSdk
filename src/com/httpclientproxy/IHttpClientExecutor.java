
package com.httpclientproxy;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public interface IHttpClientExecutor {
    public HttpResponseWrap execute(BasicHttpParams httpClientParam, HttpPostWrap httpPost);
}
