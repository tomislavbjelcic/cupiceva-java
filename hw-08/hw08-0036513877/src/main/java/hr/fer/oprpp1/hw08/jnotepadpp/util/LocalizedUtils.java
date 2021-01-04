package hr.fer.oprpp1.hw08.jnotepadpp.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

public class LocalizedUtils {
	
	private static final ILocalizationProvider provider = LocalizationProvider.getInstance();
	
	private LocalizedUtils() {}
	
	public static void checkFile(Path file) {
		Objects.requireNonNull(file);
		if (!Files.exists(file))
			throw new IllegalArgumentException(provider.getString("err_path_noexist") + ": " + file.toString());
		
		if (!Files.isRegularFile(file))
			throw new IllegalArgumentException(provider.getString("err_file_not_regular") + ": " + file.toString());
	}
	
	public static void checkFileReadable(Path file) {
		checkFile(file);
		if (!Files.isReadable(file))
			throw new IllegalArgumentException(provider.getString("err_file_not_readable") + ": " + file.toString());
	}
	
	public static void checkFileWritable(Path file) {
		checkFile(file);
		if (!Files.isReadable(file))
			throw new IllegalArgumentException(provider.getString("err_file_not_writable") + ": " + file.toString());
	}
	
	public static String readString(Path file) {
		checkFileReadable(file);
		String str = null;
		try {
			str = Files.readString(file);
		} catch (IOException | OutOfMemoryError ex) {
			throw new RuntimeException(provider.getString("err_file_load" + ": " + file.toString()));
		}
		return str;
	}	
	public static void main(String[] args) {
		Path p = Path.of("pom.xml");
		boolean exists = Files.exists(p); System.out.println("Exists: " + exists);
		boolean isRegularFile = Files.isRegularFile(p); System.out.println("Regular file: " + isRegularFile);
		boolean isDir = Files.isDirectory(p); System.out.println("Directory: " + isDir);
		boolean isReadable = Files.isReadable(p); System.out.println("Readable: " + isReadable);
		boolean isWritable = Files.isWritable(p); System.out.println("Writable: " + isWritable);
		System.out.println(provider.getString("err_file_not_regular"));
		String s = readString(p);
		System.out.println(s);
	}
	
}
