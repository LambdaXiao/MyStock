package com.android.mystock.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import java.io.File;

public class CacheClearHelper {
	public static void clearAppData(Activity context){
	}
	
	public static void clearWebViewCacheAndLocalStorage(Context context){
		//WebView��������
		clearWebViewCache(context);
		
		//LocalStorage����
		clearWebViewLocalStorage(context);
	}
	
	public static void clearWebViewCache(Context context){
		WebView webview = new WebView(context);
		webview.clearCache(true);
	}
	
	public static void clearWebViewCookies(Context context){
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}
	
	public static void clearWebViewLocalStorage(Context context){
		//���/data/data/com.xxx.xxx/app_database
		String cachepath = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		deleteFilesByDirectory(new File(cachepath));
		
		//���/data/data/com.xxx.xxx/app_database/localstorage
		cachepath = cachepath + "/localstorage";
		deleteFilesByDirectory(new File(cachepath));
	}
	
    /** * �����Ӧ���ڲ�����(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void clearInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }
	
    /**
     * * ����ⲿcache�µ�����(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    @SuppressLint("NewApi")
	public static void clearExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }
    
    /** * �����Ӧ���������ݿ�(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void clearDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * �����Ӧ��SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public static void clearSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }
    
    /** * ����Զ���·���µ��ļ���ʹ����С�ģ��벻Ҫ��ɾ������ֻ֧��Ŀ¼�µ��ļ�ɾ�� * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }
    
    /** * �����������Ӧ�����ݿ� * * @param context * @param dbName */
    public static void clearDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }
    
    /** * ���/data/data/com.xxx.xxx/files�µ����� * * @param context */
    public static void clearFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }
    
    /** * �����Ӧ�����е����� * * @param context * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
    	clearInternalCache(context);
        clearExternalCache(context);
        clearDatabases(context);
        clearSharedPreference(context);
        clearFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }
    
    /** * ɾ������ ����ֻ��ɾ��ĳ���ļ����µ��ļ�����������directory�Ǹ��ļ������������� * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
