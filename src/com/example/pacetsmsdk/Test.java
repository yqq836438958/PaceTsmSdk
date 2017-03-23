
package com.example.pacetsmsdk;

import android.util.Log;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class Test {
    private static final String TAG = "YQQ";

    private static void print(String msg) {
        Log.d(TAG, msg);
    }

    public static void invoke() {
        final int i = 1;
        Task.call(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                print("task1 called");
                if (i > 0) {
                    throw new Exception("");
                }
                return Boolean.TRUE;
            }
        }).continueWith(new Continuation<Boolean, String>() {

            @Override
            public String then(Task<Boolean> task) throws Exception {
                // TODO Auto-generated method stub
                return null;
            }
        }).continueWithTask(new Continuation<String, Task<String>>() {

            @Override
            public Task<String> then(Task<String> task) throws Exception {
                print("task2 start called");
                return Task.forResult("aaaaaaaaa");
            }
        });
    }
}
