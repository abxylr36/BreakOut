package org.masters.breakout.log;

public class Log {

	public static void info(String msg) {
		System.out.println(msg);
	}
	
	public static void info(String format, Object...args) {
		info(String.format(format, args));
	}
	
	public static void error(String msg) {
		System.err.println(msg);
	}
	
	public static void error(String format, Object...args) {
		error(String.format(format, args));
	}
}
