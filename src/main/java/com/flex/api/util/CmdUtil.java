package com.flex.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.flex.api.exception.CompileErrorException;

public class CmdUtil {

	public static String exe(String filePath, String fileName) throws IOException, InterruptedException {
		Process process = null;
		BufferedReader br = null;
		StringBuffer buffer = null;
		try {
			process = Runtime.getRuntime().exec("javac " + filePath + "/" + fileName + ".java");
			// stream 버퍼를 비워줌으로써 waitFor가 정상적으로 작동 
			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
			process.waitFor();	// 앞에 process 가 끝날때까지 대기,
			
			process = Runtime.getRuntime().exec("java -cp " + filePath + " " + fileName);
			
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line = null;
			buffer = new StringBuffer();
			
			while((line = br.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			process.destroy();
			br.close();
		}
		return buffer.toString();
	}
	
	public static void compile(String filePath, String fileName) {
		Process process = null;
		StringBuffer readBuffer = null;
		try {
			process = Runtime.getRuntime().exec("javac " + filePath + fileName + ".java");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line = null;
			readBuffer = new StringBuffer();
			while((line = br.readLine()) != null) {
				readBuffer.append(line);
				readBuffer.append("\n");
			}
//			System.out.println(readBuffer.toString().replaceAll(filePath, ""));
			br.close();
			
			// stream 버퍼를 비워줌으로써 waitFor가 정상적으로 작동 
			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
			process.waitFor();	// 앞에 process 가 끝날때까지 대기,
		} catch (Exception e) {
			throw new CompileErrorException("Compile", "Compile is failed. path:" + filePath + fileName + ".java", e);
		} finally {
			process.destroy();
		}
		
		if (process != null && process.exitValue() != 0) {
			throw new CompileErrorException("Compile", readBuffer.toString().replaceAll(filePath, ""), null);
		}
	}
}
