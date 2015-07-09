package org.jboss.brms.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.jboss.brms.common.FileResource;
import org.jboss.brms.exception.BrmsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(IoUtils.class);
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	/**
	 * Creates a temporary directory using the system property
	 * java.io.tmpdir and returns the {@link Path}.
	 * @return
	 * @throws BrmsException
	 */
	public static Path createTempDirectory() throws BrmsException {

		Path tmpPath = null;
		String ioTmpPath = System.getProperty("java.io.tmpdir");

		if(ioTmpPath == null) {
			throw new IllegalArgumentException("Missing System Property for java.io.tmpdir");
		} else {
			LOGGER.debug("Found java.io.tmpdir '" + ioTmpPath + "'");
			try {
				tmpPath = Files.createTempDirectory("tmp_");
			} catch(IOException ioe) {
				throw new BrmsException("Failed to create temporary directory.", ioe);
			}
		}

		return tmpPath;

	}

	public static void deleteTemporaryKieArtifacts(String artifactId, String version) throws BrmsException {

		String ioTmpPath = System.getProperty("java.io.tmpdir");
		String artifactPrefix = ioTmpPath + artifactId + "-" + version + ".";

		try {
			Files.deleteIfExists(Paths.get(artifactPrefix+"pom"));
		} catch (IOException e) {
			throw new BrmsException("Failed to delete tmp file '" + artifactPrefix+"pom'", e);
		}
		try {
			Files.deleteIfExists(Paths.get(artifactPrefix+"jar"));
		} catch (IOException e) {
			throw new BrmsException("Failed to delete tmp file '" + artifactPrefix+"jar'", e);
		}

	}

	/**
	 * Recursively deletes all files and directories for the 
	 * given {@link Path}.
	 * @param directory
	 * @throws IOException
	 */
	public static void deleteDirectory(Path directory) throws IOException {
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}

		});

	}

	/**
	 * Returns a {@link List} of {@link FileResource} objects that contain
	 * all files and their contents from the given path.
	 * @param path
	 * @param fromDir
	 * @return
	 * @throws Exception
	 */
	public static List<FileResource> loadResource(String path, boolean fromDir) throws Exception {

		List<FileResource> output = new ArrayList<FileResource>();
		URL url = IoUtils.class.getClassLoader().getResource(path);
		if(fromDir) {
			if(url != null) {
				File folder = new File(url.getPath());
				if(folder.exists()) {
					for(File folderFile : folder.listFiles()) {
						if(folderFile.isDirectory()) {
							continue;
						}
						String content = convertFileToString(new FileInputStream(folderFile));
						output.add(new FileResource(folderFile.getName(), content));
					}
				}
			} else {
				throw new IllegalArgumentException("Directory '" + path + "' does not exist.");
			}
		} else {
			InputStream is = ClassLoader.getSystemResourceAsStream(path);
			if(is == null) {
				is = new FileInputStream(new File(path));
			}
			String content = convertFileToString(is);
			String name = path.substring(path.lastIndexOf("/") + 1);
			output.add(new FileResource(name, content));
		}

		return output;

	}

	/**
	 * Converts the contents of the {@link InputStream} into
	 * a {@link String}.
	 * @param in
	 * @return
	 */
	public static String convertFileToString(InputStream in) {
		InputStreamReader input = new InputStreamReader(in,
				IoUtils.UTF8_CHARSET);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter output = new OutputStreamWriter(baos,
				IoUtils.UTF8_CHARSET);
		char[] buffer = new char[4096];
		int n = 0;
		try {
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(baos.toByteArray(), IoUtils.UTF8_CHARSET);
	}

}
