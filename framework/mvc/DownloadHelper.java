package com.learn2crack.filedownload;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.learn2crack.filedownload.models.Download;

import protoarchi.com.utils.Constants;
import protoarchi.com.utils.MainThreadImpl;

/**
 * Created by lihaifeng on 16/6/27.
 */
public final class DownloadHelper {

    private static SparseArray<Download> downloadMap = new SparseArray<>();


    public synchronized static void download(Context context, String url, String output){
        download(context,url,output,true);
    }

    public synchronized static void download(Context context, String url, String output,boolean notify){
        download(context,url,output,notify,null);
    }

    public synchronized static void download(Context context, String url, String output,boolean notify,Class resultActivity){
        download(context,url,output,notify,null,false);
    }

    public synchronized static void download(Context context, String url, String output,boolean notify,Class resultActivity,boolean notifyIndeterminate){
        if(null != getDownload(url)){
            // download existing.
            MainThreadImpl.toast(context,R.string.download_exist);
            return;
        }
        Download download = new Download();
        download.setUrl(url);
        download.setOutput(output);
        download.setResultActivity(resultActivity);
        download.setNotify(notify);
        download.setIndeterminate(notifyIndeterminate);

        downloadMap.put(downloadMap.size(),download);

        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.IntentExtra.PARCELABLE_EXTRA,download);
        context.startService(intent);
    }

    private synchronized static Download getDownload(String url){
        Download download = null;
        for(int i = 0;i<downloadMap.size();i++){
            download = downloadMap.valueAt(i);
            if(download.getUrl().equals(url)){
                return download;
            }
        }
        return null;
    }

    private synchronized static void removeDownload(String url){
        int index = Integer.MIN_VALUE;
        Download download = null;
        for(int i = 0;i<downloadMap.size();i++){
            download = downloadMap.valueAt(i);
            if(download.getUrl().equals(url)){
                index = i;
                break;
            }
        }
        if(index != Integer.MIN_VALUE){
            downloadMap.remove(index);
        }
    }
}
