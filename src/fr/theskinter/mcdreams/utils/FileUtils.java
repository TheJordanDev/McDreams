package fr.theskinter.mcdreams.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

	public static void save(File file,String text) {
		final FileWriter writer;
		try {
			file.createNewFile();
			writer = new FileWriter(file);
			writer.write(text);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String load(File file) {
		if (file.exists()) {
			try {
				final BufferedReader reader = new BufferedReader(new FileReader(file));
				final StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				reader.close();
				return builder.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
}
