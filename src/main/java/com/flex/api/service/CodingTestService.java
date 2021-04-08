package com.flex.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import com.flex.api.util.CmdUtil;
import com.flex.api.util.FileUtil;

@Service
public class CodingTestService {

	public String getTest(String text) {
		return "1";
	}
	
	public String getScoreCode(String code) {
		String answer = null;
		String url = "/Users/jb/git/flex-api/CodingTest.java";
		StringBuilder sb = new StringBuilder();
		String publicClass = "public class CodingTest { public static void main(String[] args){System.out.println(new Solution().solution());} }";
		sb.append(code);
		sb.append(publicClass);
		try {
			FileUtil.saveFile(url, sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String filePath = "/Users/jb/git/flex-api";
		String fileName = "CodingTest";
		try {
			answer = CmdUtil.exec(filePath, fileName);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return answer;
	}
}
