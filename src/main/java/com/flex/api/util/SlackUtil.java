package com.flex.api.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackUtil {
	
	@Value("${flex.slack.default.webhook}")
	private String webhook;
	
	@Value("${flex.slack.default.channel}")
	private String channel;
	
	@Value("${flex.slack.default.icon_emoji}")
	private String iconEmoji;
	
	@Value("${flex.slack.default.username}")
	private String userName;
	
	@Value("${flex.slack.error.webhook}")
	private String errorWebhook;
	
	@Value("${flex.slack.error.channel}")
	private String errorChannel;
	
	
	public void sendMessage(String message){
//		String url = "https://hooks.slack.com/services/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";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(this.webhook);
		JSONObject json = new JSONObject();
		try {
			json.put("channel", "#" + this.channel);
			json.put("text", message);
			json.put("icon_emoji", String.format(":%s:", this.iconEmoji));	//커스터마이징으로 아이콘 만들수도 있다!
			json.put("username", this.userName);
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
	
	public void sendErrorMessage(String message){
//		String url = "https://hooks.slack.com/services/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";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(this.errorWebhook);
		JSONObject json = new JSONObject();
		try {
			json.put("channel", "#" + this.errorChannel);
			json.put("text", message);
			json.put("icon_emoji", String.format(":%s:", this.iconEmoji));	//커스터마이징으로 아이콘 만들수도 있다!
			json.put("username", this.userName);
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
