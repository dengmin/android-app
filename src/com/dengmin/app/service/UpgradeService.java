package com.dengmin.app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.dengmin.app.R;

public class UpgradeService extends Service{
	
	private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    
	private NotificationManager notificationManager;
	private NotificationCompat.Builder builder;
	
	private Thread downloadThread;
	
	private String destPath;
	//apk保存完整路径
	private String apkFilePath = "";
	//临时下载文件路径
	private String tmpFilePath = "";
	//下载文件大小
	private String apkFileSize;
	//已下载文件大小
	private String tmpFileSize;
	
	private String downloadUrl = "http://yomo-uploads.stor.sinaapp.com/App.apk";
	
	private boolean interceptFlag = false;
	//进度值
    private int progress;
    
    /**
	 * 更新进度条
	 */
	private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				//正在下载,更新进度条的进度
				builder.setProgress(100, progress, false).setContentText(tmpFileSize + "/" + apkFileSize);
				notificationManager.notify(0, builder.build());
				break;
			case DOWN_OVER:
				builder.setContentText("下载完成.").setProgress(0, 0, false);
				notificationManager.notify(0, builder.build());
				installApk();
				break;
			case DOWN_NOSDCARD:
				builder.setContentText("SD卡没有挂载.").setProgress(0, 0, false);
				notificationManager.notify(0, builder.build());
				break;
			}
    	};
    };
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		createNotification();
		downloadApk();
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void createNotification() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher).setContentTitle("My App");
		builder.setContentText("开始下载");
		builder.setAutoCancel(true);
		notificationManager.notify(0, builder.build());
	}
	
	private void initDownloadEnv(){
		String apkName = "app.apk";
		String tmpApk = "app.tmp";
		//判断是否挂载了SD卡
		String storageState = Environment.getExternalStorageState();
		if(storageState.equals(Environment.MEDIA_MOUNTED)){
			destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yoyo/";
			File file = new File(destPath);
			if(!file.exists()){
				file.mkdirs();
			}
			apkFilePath = destPath + apkName;
			tmpFilePath = destPath + tmpApk;
		}
		//没有挂载SD卡，无法下载文件
		if(apkFilePath == null || apkFilePath == ""){
			mHandler.sendEmptyMessage(DOWN_NOSDCARD);
			return;
		}
	}
	
	/**
	 * 文件下载
	 */
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try{
				initDownloadEnv();
				File ApkFile = new File(apkFilePath);
				//是否已下载更新文件
				if(ApkFile.exists()){
					mHandler.sendEmptyMessage(DOWN_OVER);
					return;
				}
				//输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);
				
				URL url = new URL(downloadUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				//显示文件大小格式：2个小数点显示
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//进度条下面显示的总文件大小
		    	apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				
				int count = 0;
				byte buf[] = new byte[1024];
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    		//进度条下面显示的当前下载文件大小
		    		tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//当前进度值
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){
		    			interceptFlag= true;
		    			//下载完成 - 将临时下载文件转成APK文件
						if(tmpFile.renameTo(ApkFile)){
							//通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);
				
				fos.close();
				is.close();
			}catch(Exception e){
				
			}
		}
	};
	
	private void downloadApk(){
		downloadThread = new Thread(mdownApkRunnable);
		downloadThread.start();
	}

	private void installApk(){
		File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        startActivity(i);
	}
}
