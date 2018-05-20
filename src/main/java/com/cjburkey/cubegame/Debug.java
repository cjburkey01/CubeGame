package com.cjburkey.cubegame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Handles printing into the console (and possibly into a separate log file)
public final class Debug {
	
	public static final Logger dlogger = LogManager.getLogger("cubegame");
	
	public static void log(Object msg) {
		log(msg, new Object[0]);
	}
	
	public static void log(Object msg, Object... param) {
		dlogger.info(sanitize(msg), param);
	}
	
	public static void warn(Object msg) {
		warn(msg, new Object[0]);
	}
	
	public static void warn(Object msg, Object... param) {
		dlogger.warn(sanitize(msg), param);
	}
	
	public static void error(Object msg) {
		error(msg, new Object[0]);
	}
	
	public static void error(Object msg, Object... param) {
		dlogger.error(sanitize(msg), param);
	}
	
	public static void exception(Throwable e) {
		error(" -- BEGIN EXCEPTION --");
		error("    Exception: \"{}\"", e.getMessage());
		for (StackTraceElement stackTrace : e.getStackTrace()) {
			error("        {}", stackTrace.toString());
		}
		error(" -- END EXCEPTION --");
	}
	
	public static String formatDecimal(double decimal, int places) {
		return String.format("%." + places + "f", decimal);
	}
	
	public static String formatDecimal1(double decimal) {
		return formatDecimal(decimal, 1);
	}
	
	public static String formatDecimal2(double decimal) {
		return formatDecimal(decimal, 2);
	}
	
	public static String formatDecimal3(double decimal) {
		return formatDecimal(decimal, 3);
	}
	
	public static String formatDecimal4(double decimal) {
		return formatDecimal(decimal, 4);
	}
	
	public static void setDefaultThreadError() {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> exception(e));
	}
	
	private static String sanitize(Object msg) {
		return (msg == null) ? "null" : (msg.toString() == null ? "null" : msg.toString());
	}
	
}