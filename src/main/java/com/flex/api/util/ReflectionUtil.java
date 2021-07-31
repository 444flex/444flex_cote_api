package com.flex.api.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.flex.api.exception.ServerSideException;

public class ReflectionUtil {
	
	private Class<?> cls;
	private Method m;
	private Object obj;
	
	private ReflectionUtil(Builder builder) {
		this.loadClass(builder.fileDir, builder.fileName);
		this.loadMethod(builder.methodName, builder.classes);
	}
	
	public static class Builder {
		
		private String fileDir;
		private String fileName;
		private String methodName;
		private Class<?>[] classes;
		
		public Builder() {}
		
		public Builder(String fileDir, String fileName, String methodName){
			this.fileDir = fileDir;
			this.fileName = fileName;
			this.methodName = methodName;
		}
		
		public Builder fileDir(String fileDir) {
			this.fileDir = fileDir;
			return this;
		}
		
		public Builder fileName(String fileName) {
			this.fileName = fileName;
			return this;
		}
		
		public Builder methodName(String methodName) {
			this.methodName = methodName;
			return this;
		}
		
		public Builder classes(Class<?>... classes) {
			this.classes = classes;
			return this;
		}
		
		public ReflectionUtil build() {
			return new ReflectionUtil(this);
		}
	}
	
	private void loadClass(String fileDir, String fileName) {
		try {
			URL[] urls = {new File(fileDir).toURL()};
			URLClassLoader ucl = new URLClassLoader(urls);
			cls = Class.forName(fileName, true, ucl);
			obj = cls.newInstance();
		} catch (Exception e) {
			throw new ServerSideException("ClassLoader", "Load Class Failed", e);
		}
	}
	
	private void loadMethod(String methodName, Class<?>... paramCls) {
		try {
			m = cls.getMethod(methodName, paramCls);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object execMethod(Object... param) {
		Object object = null;
		try {
			object = m.invoke(obj, param);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ServerSideException("Method Executor", "Execute Method Failed", e);
		}
		return object;
	}
	
	public static Class<?>[] listToArray(List<Object> list) {
		Class<?>[] classes = new Class<?>[list.size()];
		int size = list.size();
		for(int i=0; i<size; i++) {
			classes[i] = list.get(i).getClass();
		}
		return classes;
	}
}
