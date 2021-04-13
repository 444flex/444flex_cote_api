package com.flex.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.flex.api.model.Result;
import com.flex.api.util.CmdUtil;
import com.flex.api.util.FileUtil;
import com.flex.api.util.ReflectionUtil;

@Service
public class CodingTestService {
	
	@Value("${save.file.dir}")
	private String fileDir;
	
	@Value("${save.file.name}")
	private String fileName;
	
	@Value("${save.file.extension}")
	private String fileExtension;

	
	public List<Result> getScoreCode(String code) {
		String answer = null;
		String url = fileDir + fileName + fileExtension;
		try {
			FileUtil.saveFile(url, code);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			CmdUtil.exec2(fileDir, fileName);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<int[]> param1List = new ArrayList<int[]>();
		List<int[]> param2List = new ArrayList<int[]>();
		List<int[]> result1List = new ArrayList<int[]>();
		
		int[] param1 = null;
		int[] param2 = null;
		int[] result1Array = null;
		
		
		param1 = new int[]{2,3,4,5,7,8};
		param1List.add(param1);
		param1 = new int[]{0,0,3,4,5,6};
		param1List.add(param1);
		param1 = new int[]{0,0,0,0,0,0};
		param1List.add(param1);
		
		param2 = new int[]{31,10,45,1,6,19};
		param2List.add(param1);
		param2 = new int[]{7,8,9,10,11,12};
		param2List.add(param1);
		param2 = new int[]{23,1,5,22,12,31};
		param2List.add(param1);
		
		result1Array = new int[] {6,6};
		result1List.add(result1Array);
		result1Array = new int[] {5,6};
		result1List.add(result1Array);
		result1Array = new int[] {1,6};
		result1List.add(result1Array);
		
		
		
		
		ReflectionUtil reflection = new ReflectionUtil();
		reflection.loadClass(fileDir, fileName);
		reflection.loadMethod(param1.getClass(), param2.getClass());
		
		List<Result> list = new ArrayList<Result>();
		
		for (int i=0; i<param1List.size(); i++) {
			int[] paramA = param1List.get(i);
			int[] paramB = param2List.get(i);
			long s = System.currentTimeMillis();
			int[] ans = (int[]) reflection.execMethod(paramA, paramB);
			long e = System.currentTimeMillis();
			Result result = new Result();
			
			if (ans[0] == result1List.get(i)[0] && ans[1] == result1List.get(i)[1]) {
				result.setCorrect(true);
			} else {
				result.setCorrect(false);
			}
			result.setAnswer(ans);
			result.setTimemils(e-s);
			list.add(result);
		}
		
		return list;
	}
}
