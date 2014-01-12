package com.dengmin.app.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class AppUpgrade implements Serializable{

	private static final long serialVersionUID = -3895912484720757992L;

	//版本号
	private int version_code;
	
	//版本名称
	private String version_name;
	
	//下载地址
	private String download_url;
	
	//更新日志
	private String update_log;

	public int getVersion_code() {
		return version_code;
	}

	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	public String getUpdate_log() {
		return update_log;
	}

	public void setUpdate_log(String update_log) {
		this.update_log = update_log;
	}
		
	public static AppUpgrade parse(String jsonStr) throws Exception {
		AppUpgrade update = null;
		try{
			JSONObject json = new JSONObject(jsonStr);
			if (json.has("success") && json.getBoolean("success")){
				update =  new AppUpgrade();
				update.setVersion_code(json.getInt("version_code"));
				update.setVersion_name(json.getString("version_name"));
				update.setDownload_url(json.getString("download_url"));
				update.setUpdate_log(json.getString("update_log"));
			}
		}catch(JSONException e){
			throw e;
		}
		return update;
	}
}
