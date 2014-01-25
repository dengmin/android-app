package com.dengmin.app.model;

public class RollImageItem {

	public RollImageItem(String imageUrl, String targeUrl, String text) {
		super();
		this.imageUrl = imageUrl;
		this.targeUrl = targeUrl;
		this.text = text;
	}

	private String imageUrl;
	
	private String targeUrl;
	
	private String text;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTargeUrl() {
		return targeUrl;
	}

	public void setTargeUrl(String targeUrl) {
		this.targeUrl = targeUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
