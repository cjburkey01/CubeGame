package com.cjburkey.cubegame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil {
	
	// Reads an entire file's lines and returns them in an array of strings
	public static String[] readFileLines(String path) {
		List<String> out = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(FileUtil.class.getResourceAsStream(path)));
			String l;
			while ((l = reader.readLine()) != null) {
				out.add(l);
			}
		} catch (Exception e) {
			Debug.error("Failed to read file: {}", path);
			Debug.exception(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e1) {
					Debug.error("Failed to close buffered reader");
					Debug.exception(e1);
				}
			}
		}
		return out.toArray(new String[0]);
	}
	
	// Reads an entire file's text (including line breaks) and returns it in a single string
	public static String readFileText(String path) {
		String[] lines = readFileLines(path);
		StringBuilder out = new StringBuilder();
		for (String line : lines) {
			out.append(line);
			out.append('\n');
		}
		return out.toString();
	}
	
}