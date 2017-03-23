
package com.pace.api;

import org.apache.http.client.methods.HttpPost;

public interface IHttpChannel {
    public void request(HttpPost post);
}
