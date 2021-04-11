package com.flex.api.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.flex.api.util.CmdUtil;
import com.flex.api.util.FileUtil;

@Service
public class CodingTestService {
	
	@Value("${save.file.dir}")
	private String fileDir;
	
	@Value("${save.file.name}")
	private String fileName;
	
	@Value("${save.file.extension}")
	private String fileExtension;

	
	public String getScoreCode(String code) {
		String answer = null;
		String url = fileDir + fileName + fileExtension;
		StringBuilder sb = new StringBuilder();
		String publicClass = "public class "+ fileName+" { public static void main(String[] args){System.out.println(new Solution().solution());} }";
		sb.append(code);
		sb.append(publicClass);
		try {
			FileUtil.saveFile(url, sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			answer = CmdUtil.exec(fileDir, fileName);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return answer;
	}
}
