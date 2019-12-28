package com.cocotreedebug.android.uploadUtil;

import android.util.Log;

import com.cocotreedebug.android.BuildConfig;

public class CLog {

	public static boolean VERBOSE = true;
	public static boolean DEBUG = true;
	public static boolean INFO = true;
	public static boolean WARN = true;
	public static boolean ERR = true;

	/**
	 * DEBUG
	 * 
	 * @param desc
	 */
	public static void d(String t, String desc) {
		if (BuildConfig.DEBUG) {
			Log.d(t, desc);
		}
	}

	/**
	 * INFO
	 * 
	 * @param desc
	 */
	public static void i(String t, String desc) {
		if (BuildConfig.DEBUG) {
			Log.i(t, desc);
		}
	}

	/**
	 * VERBOSE
	 * 
	 * @param desc
	 */
	public static void v(String t, String desc) {
		if (BuildConfig.DEBUG) {
			Log.v(t, desc);
		}
	}

	/**
	 * WARN
	 * 
	 * @param desc
	 */
	public static void w(String t, String desc) {
		if (BuildConfig.DEBUG) {
			Log.w(t, desc);
		}
	}

	/**
	 * ERROR
	 * 
	 * @param desc
	 */
	public static void e(String t, String desc) {
		if (BuildConfig.DEBUG) {
			Log.e(t, desc);
		}
	}
}
