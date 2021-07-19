package com.flex.api.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;

public class SlackUtil {
	public static void sendMessage(String message){
		String url = "https://hooks.slack.com/services/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";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		JSONObject json = new JSONObject();
		try {
			json.put("channel", "#submit_status");
			json.put("text", message);
			json.put("icon_emoji", ":tory:");	//커스터마이징으로 아이콘 만들수도 있다!
			json.put("username", "토리");
			post.addParameter("payload", json.toString());
			//처음에 utf-8로 content-type안넣어주니까 한글은 깨져서 오더라. 그래서 content-type넣어줌
			post.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			int responseCode = client.executeMethod(post);
			String response = post.getResponseBodyAsString();
			if (responseCode != HttpStatus.SC_OK) {
				System.out.println("Response: " + response);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException posting to Slack " + e);
		} catch (IOException e) {
			System.out.println("IOException posting to Slack " + e);
		} finally {
			post.releaseConnection();
		}
	}
}
