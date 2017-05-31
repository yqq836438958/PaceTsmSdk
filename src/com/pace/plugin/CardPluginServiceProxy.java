
package com.pace.plugin;

import android.content.Context;

import com.pace.tsm.plugin.ICardPluginService;
import com.pace.tsm.plugin.bean.CardTransactionBean;

import dalvik.system.DexClassLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CardPluginServiceProxy implements ICardPluginService {
    public static final String DEX_NAME = "plugin_dex";
    public static final String CLASS_TARGET_SERVICE = "com.pace.plugin.CardPluginService";
    private ICardPluginService mService = null;
    private Context mContext = null;

    public CardPluginServiceProxy(Context context) {
        mContext = context;
    }

    private boolean loadDex(File jarFile, File outputDir) {
        DexClassLoader classLoader = new DexClassLoader(jarFile.getAbsolutePath(),
                outputDir.getAbsolutePath(), null, mContext.getClassLoader());
        ICardPluginService service = null;
        try {
            service = (ICardPluginService) classLoader.loadClass(CLASS_TARGET_SERVICE)
                    .newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (service != null) {
            mService = service;
            return true;
        }
        return false;
    }

    private boolean loadDexFromAssets(File dexOutputDir, String assetJarName) {
        File file = new File(mContext.getFilesDir().getAbsolutePath(), assetJarName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream in = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            in = mContext.getAssets().open(assetJarName);
            bis = new BufferedInputStream(in);
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];
            int len = 0;
            while ((len = bis.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            fos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                bis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadDex(file, dexOutputDir);
    }

    private boolean loadDexFromSDCard(File dexOutputDir) {
        // File dexOutputDir = mContext.getDir(DEX_NAME, 0);//
        String jarPath = mContext.getFilesDir().getPath() + "/testDex.jar";
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            return false;
        }
        return loadDex(jarFile, dexOutputDir);
    }

    @Override
    public List<String> fetchDetailReq(String aid, String tag) {
        if (mService == null) {
            return null;
        }
        return mService.fetchDetailReq(aid, tag);
    }

    @Override
    public String parseDetailRsp(String aid, String tag, List<String> rsp) {
        if (mService == null) {
            return null;
        }
        return mService.parseDetailRsp(aid, tag, rsp);
    }

    @Override
    public List<String> getSupportAidList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getTagListByAid(String aid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> fetchTransactionReq(String aid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CardTransactionBean> parseTransactionRsp(String aid, List<String> rsp) {
        // TODO Auto-generated method stub
        return null;
    }

}
