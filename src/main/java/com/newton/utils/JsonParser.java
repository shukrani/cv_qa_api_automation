package com.newton.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class JsonParser {
	JSONObject json;
	ObjectMapper mapper;

	public JsonParser() {
		mapper = new ObjectMapper();
		mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	public JsonParser(String jsonData) {
		this.json = new JSONObject(jsonData);
		this.mapper = new ObjectMapper();
		this.mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public String getJson(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		return mapper.writeValueAsString(object);

	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

		String json = " { \"userId\": 1, \"id\": 1, \"title\": \"some title\", \"body\": \"text body\" }";
		ObjectMapper mapper = new ObjectMapper();
		Post post = mapper.readValue(json, Post.class);
		System.out.println(post.getBody());

	}
}
