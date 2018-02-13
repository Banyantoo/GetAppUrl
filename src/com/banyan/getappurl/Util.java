package com.banyan.getappurl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Util {

	/**
	 * 字节转char
	 * 
	 * @param bytes
	 * @return
	 */
	public static char byteToChar(byte bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(1);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		char[] tmp = cb.array();
		return tmp[0];
	}

	/**
	 * byte数组转char数组
	 * 
	 * @param bytes
	 * @return
	 */
	public static char[] byteToChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}

	/**
	 * 小端byte[]转int
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		int value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value |= (bytes[i] & 0xFF) << (8 * i);
		}
		return value;
	}

	/**
	 * 正则匹配所有符合条件的url
	 * 
	 * @param regex
	 * @param source
	 * @return
	 */
	public static String regexUrl(String source) {
		String regex = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		StringBuilder urlList = new StringBuilder();
		while (matcher.find()) {
			urlList.append(matcher.group() + "\n");
			System.out.println(matcher.group());
		}
		return urlList.toString();
	}

	/**
	 * 正则匹配需要解析得的文件的路径
	 * 
	 * @param 路径
	 * @return
	 */
	public static String regexFile(String path) {
		String[] regexList = { "(classes){1,1}[a-z,A-z,0-9]*(.dex){1,1}", "(resources){1,1}[a-z,A-z,0-9]*(.arsc){1,1}",
				"(lib){1,1}[a-z,A-z,0-9]*(.so){1,1}" };
		String[] fileType = { "dex", "arsc", "so" };
		for (int i = 0; i < regexList.length; i++) {
			Pattern pattern = Pattern.compile(regexList[i]);
			Matcher matcher = pattern.matcher(path);
			if (matcher.find()) {
				System.out.println("正在解压：" + matcher.group());
				return fileType[i];
			}
		}
		return null;

	}

	/**
	 * 删除文件下的所有文件
	 * 
	 * @param dir
	 *            需要删除文件夹下所有内容的文件名
	 */
	public static void deleteDir(File dir) {
		File[] files = dir.listFiles();
		for (File f : files) {

			// 3.判断是否有目录，如果有，继续使用该功能遍历，如果不是文件夹，直接删除
			if (f.isDirectory()) {
				deleteDir(f);
			} else {
				f.delete();// 文件删除
			}
		}
		dir.delete();// 最后删除文件夹

	}

	/**
	 * 创建新建的文件夹，如果存在则删除文件夹里所有内容
	 * 
	 * @param destDirName
	 */
	public static void createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.isDirectory()) {
			deleteDir(dir);
			dir.mkdirs();
		} else {
			dir.mkdirs();
		}
	}

	/**
	 * 解压文件到temp目录
	 * 
	 * @param zeEntry
	 * @param zipFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void unZipToDir(ZipEntry zeEntry, ZipFile zipFile) throws FileNotFoundException, IOException {
		String fileName = zeEntry.getName();
		File file = new File("temp/" + fileName);
		if (!file.exists()) {
			File rootDirectoryFile = new File(file.getParent());
			// 创建目录
			if (!rootDirectoryFile.exists()) {
				boolean ifSuccess = rootDirectoryFile.mkdirs();
			}
			file.createNewFile();
		}
		// 写入文件
		BufferedOutputStream write = new BufferedOutputStream(new FileOutputStream(file));
		InputStream read = zipFile.getInputStream(zeEntry);
		int cha = 0;
		while ((cha = read.read()) != -1) {
			write.write(cha);
		}
		write.flush();
		write.close();
		read.close();
	}

}
