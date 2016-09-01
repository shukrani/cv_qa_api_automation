package com.newton.utils;

public class Category {

	int category_id;
	String category_name;
	String category_name_suggest;
	String category_description;
	int parent_category_id;
	String page_title;
	String category_url;
	String category_image;

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getCategory_name_suggest() {
		return category_name_suggest;
	}

	public void setCategory_name_suggest(String category_name_suggest) {
		this.category_name_suggest = category_name_suggest;
	}

	public String getCategory_description() {
		return category_description;
	}

	public void setCategory_description(String category_description) {
		this.category_description = category_description;
	}

	public int getParent_category_id() {
		return parent_category_id;
	}

	public void setParent_category_id(int parent_category_id) {
		this.parent_category_id = parent_category_id;
	}

	public String getPage_title() {
		return page_title;
	}

	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}

	public String getCategory_url() {
		return category_url;
	}

	public void setCategory_url(String category_url) {
		this.category_url = category_url;
	}

	public String getCategory_image() {
		return category_image;
	}

	public void setCategory_image(String category_image) {
		this.category_image = category_image;
	}

}
