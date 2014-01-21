package com.dengmin.app.http;

import com.dengmin.app.AppException;
import com.dengmin.app.model.AppUpgrade;

public class APIClient {

	public final static String URL_UPDATE_VERSION = "http://yomo.sinaapp.com/appversion";
	
	public static AppUpgrade checkVersion() throws AppException {
		try{
			return AppUpgrade.parse(HttpRequest.get(URL_UPDATE_VERSION).body());		
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	}
}
