package com.newton.utils;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * 
 * @author chhagan
 *
 *         to represent post json as java object
 *
 *         { "userId": 1, "id": 1, "title": "sunt aut facere repellat provident
 *         occaecati excepturi optio reprehenderit", "body": "quia et
 *         suscipit\nsuscipit recusandae consequuntur expedita et
 *         cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est
 *         autem sunt rem eveniet architecto" }
 */
public class Post {

	int userId;
	int id;
	String title;
	String body;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public static void main(String[] args) {

	}

}
