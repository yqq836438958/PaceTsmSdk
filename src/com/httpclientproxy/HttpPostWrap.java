
package com.httpclientproxy;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class HttpPostWrap implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String sUrl;
    private HashMap<String, String> mHeaders = new HashMap<String, String>();
    private byte[] bDatas;

    public static HttpPost unwrap(HttpPostWrap obj) {
        HttpPost httpPost = new HttpPost(obj.sUrl);
        Iterator<Entry<String, String>> iter = obj.mHeaders.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();// 返回键（key）和值（value）的一个实体（Entry）
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }
        httpPost.setEntity(new ByteArrayEntity(obj.bDatas));
        return httpPost;
    }

    public static HttpPostWrap wrap(HttpPost obj) {
        return new HttpPostWrap(obj);
    }

    /**
     * <默认构造函数>
     */
    public HttpPostWrap(HttpPost obj) {
        sUrl = obj.getURI().toString();
        mHeaders = new HashMap<String, String>();
        Header[] headers = obj.getAllHeaders();
        for (Header header : headers) {
            mHeaders.put(header.getName(), header.getValue());
        }
        ByteArrayEntity entity = (ByteArrayEntity) obj.getEntity();
        // bDatas = entity.ge;
    }

}
