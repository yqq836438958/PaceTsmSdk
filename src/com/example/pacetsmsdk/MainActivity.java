
package com.example.pacetsmsdk;

import android.app.Activity;
import android.os.Bundle;
import com.pace.tsmsdk.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Test.invoke();
    }
}
