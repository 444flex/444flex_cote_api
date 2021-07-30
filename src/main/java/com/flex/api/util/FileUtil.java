package com.flex.api.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.flex.api.exception.DirectoryCreateFailedException;
import com.flex.api.exception.FileCreateFailedException;

public class FileUtil {
	
	public static void saveFile(String path, String file, String code) {
		createDirectory(path);
		String fullPath = path + file;
		try {
			createFile(fullPath, code);
		} catch (IOException e) {
			throw new FileCreateFailedException("File", "File path:" + fullPath, null);
		}
	}
	

	private static void createFile(String fileName, String code) throws IOException {
		
		BufferedOutputStream bs = null;
		try {
			bs = new BufferedOutputStream(new FileOutputStream(fileName));
			bs.write(code.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bs.close();
		}
	}
	
	private static void createDirectory(String path) {
		try {
			File folder = new File(path);
			if (!folder.exists())
				folder.mkdirs();
		} catch (Exception e) {
			throw new DirectoryCreateFailedException("Directory", "Directory path:" + path, null);
		}
	}
}
