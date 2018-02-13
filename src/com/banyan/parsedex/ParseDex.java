package com.banyan.parsedex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.banyan.getappurl.Util;
import com.banyan.parsedex.DexFile.DexHeader;

public class ParseDex {

	DexFile dexFile = new DexFile();
	DexHeader headerType = new DexFile().new DexHeader();
	List<byte[]> dexList;
	RandomAccessFile raFile;

	public ParseDex(String path) throws IOException {
		File file = new File(path);
		raFile = new RandomAccessFile(file, "r");
		parseHeader();
	}

	/**解析dex文件头
	 * @param file
	 * @throws IOException
	 */
	public  void parseHeader() throws IOException {
		dexList = new ArrayList<>();
		dexList.add(headerType.magic);
		dexList.add(headerType.checksum);
		dexList.add(headerType.signature);
		dexList.add(headerType.fileSize);
		dexList.add(headerType.headerSise);
		dexList.add(headerType.endianTag);
		dexList.add(headerType.linkSize);
		dexList.add(headerType.linkOff);
		dexList.add(headerType.mapOff);
		dexList.add(headerType.stringIdsSite);
		dexList.add(headerType.stringIdsOff);
		dexList.add(headerType.typeIdsSize);
		dexList.add(headerType.typeIdsOff);
		dexList.add(headerType.protoIdsSize);
		dexList.add(headerType.protoIdsOff);
		dexList.add(headerType.fieldIdsSize);
		dexList.add(headerType.fieldIdsOff);
		dexList.add(headerType.methodIdsSize);
		dexList.add(headerType.methodIdsOff);
		dexList.add(headerType.classDefsSize);
		dexList.add(headerType.classDefsOff);
		dexList.add(headerType.dataSize);
		dexList.add(headerType.dataOff);
		for (int i = 0; i < dexList.size(); i++) {
			raFile.read(dexList.get(i));
		}

	}

	/**获取全部字符串
	 * @throws IOException
	 */
	public void getStrings() throws IOException {
		// 获取字符偏移列表
		List<byte[]> stringIds = new ArrayList<>();
		for (int i = 0; i < Util.bytesToInt(headerType.stringIdsSite); i++) {
			byte[] temp = new byte[4];
			raFile.read(temp);
			stringIds.add(temp);
		}
		// 获取all字符串
		StringBuilder strs = new StringBuilder();
		for (byte[] stringId : stringIds) {
			byte[] size = new byte[1];
			raFile.seek(Util.bytesToInt(stringId));
			raFile.read(size);
			byte[] str = new byte[Util.bytesToInt(size)];
			raFile.read(str);
			strs.append(new String(str, Charset.forName("utf-8")) + "\r\n");
		}
		
		//写出全部字符串到output目录
		File dexStr = new File("output/DexStr.txt");
		dexStr.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(dexStr));
		pw.println(strs.toString());
		pw.println(strs.toString());
		pw.flush();
		pw.close();
		raFile.close();

	}
	
	/**获取全部url
	 * @throws IOException
	 */
	public void getUrl() throws IOException {
		// 获取字符偏移列表
		List<byte[]> stringIds = new ArrayList<>();
		for (int i = 0; i < Util.bytesToInt(headerType.stringIdsSite); i++) {
			byte[] temp = new byte[4];
			raFile.read(temp);
			stringIds.add(temp);
		}
		// 获取all字符串
		StringBuilder strs = new StringBuilder();
		for (byte[] stringId : stringIds) {
			byte[] size = new byte[1];
			raFile.seek(Util.bytesToInt(stringId));
			raFile.read(size);
			byte[] str = new byte[Util.bytesToInt(size)];
			raFile.read(str);
			strs.append(new String(str, Charset.forName("utf-8")) + "\r\n");
		}
		
		//写出符合条件的url到output目录
		File dexStr = new File("output/DexUrl.txt");
		dexStr.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(dexStr, true));
		System.out.println(Util.regexUrl(strs.toString()));
		pw.println(Util.regexUrl(strs.toString()));
		pw.flush();
		pw.close();
		raFile.close();

	}
	
	

}
