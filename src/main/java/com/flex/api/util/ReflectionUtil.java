package com.flex.api.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ReflectionUtil {
	
	private Class<?> cls;
	private Method m;
	private Object obj;

	public void loadClass(String fileDir, String fileName) {
		try {
			URL[] urls = {new File(fileDir).toURL()};
			URLClassLoader ucl = new URLClassLoader(urls);
			cls = Class.forName(fileName, true, ucl);
			obj = cls.newInstance();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadMethod(Class<?> cls1, Class<?> cls2) {
		try {
			m = cls.getMethod("solution", cls1, cls2);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object execMethod(int[] param1, int[] param2) {
		Object a = null;
		try {
			a = m.invoke(obj, param1, param2);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return a;
	}
}
