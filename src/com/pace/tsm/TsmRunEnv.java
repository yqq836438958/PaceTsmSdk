
package com.pace.tsm;

public class TsmRunEnv {
    private static int sRunEnv = 0;

    void setRunEnv(int env) {
        sRunEnv = env;
    }

    public static boolean isApiRunLocal() {
        return sRunEnv == TsmApi.API_RUN_LOCAL;
    }
}
