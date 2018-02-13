package com.banyan.parsearsc;

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
import com.banyan.parsearsc.ArscFile.ArscHeader;
import com.banyan.parsearsc.ArscFile.GlobalStringPool;

public class ParseArsc {
	ArscFile arscFile = new ArscFile();
	ArscHeader arscHeader = arscFile.new ArscHeader();
	GlobalStringPool gsPool = arscFile.new GlobalStringPool();
	List<byte[]> arscHeaderList = new ArrayList<>();
	List<byte[]> gsPoolList = new ArrayList<>();
	RandomAccessFile raFile;

	public ParseArsc(String path) throws IOException {
		File file = new File(path);
		raFile = new RandomAccessFile(file, "r");
		parseHeader(file);
		parseGsPool();
	}

	public void parseHeader(File file) throws IOException {
		arscHeaderList.add(arscHeader.headType);
		arscHeaderList.add(arscHeader.headSize);
		arscHeaderList.add(arscHeader.fileSize);
		arscHeaderList.add(arscHeader.packageCount);
		for (int i = 0; i < arscHeaderList.size(); i++) {
			raFile.read(arscHeaderList.get(i));
		}

	}

	public void parseGsPool() throws IOException {
		gsPoolList.add(gsPool.stringsType);
		gsPoolList.add(gsPool.stringsHeadSite);
		gsPoolList.add(gsPool.stringsSize);
		gsPoolList.add(gsPool.stringsCount);
		gsPoolList.add(gsPool.stylesCount);
		gsPoolList.add(gsPool.flag);
		gsPoolList.add(gsPool.stringsOff);
		gsPoolList.add(gsPool.styleOff);
		for (int i = 0; i < gsPoolList.size(); i++) {
			raFile.read(gsPoolList.get(i));
		}
	}

	public void getStrings() throws IOException {
		// 获取arsc文件所有字符串偏移
		List<Integer> stringsOff = new ArrayList<>();
		for (int i = 0; i < Util.bytesToInt(gsPool.stringsCount); i++) {
			byte[] temp = new byte[4];
			raFile.read(temp);
			stringsOff.add(Util.bytesToInt(temp));
		}
		// 根据偏移获取字符串
		int stringOff = Util.bytesToInt(gsPool.stringsOff) + 12;
		String strs = "";
		for (int i = 0; i < stringsOff.size(); i++) {
			raFile.seek(stringOff + stringsOff.get(i));
			byte[] size = new byte[1];
			raFile.read(size);
			raFile.skipBytes(1);
			byte[] str = new byte[Util.bytesToInt(size)];
			raFile.read(str);
			strs = strs.concat(new String(str, Charset.forName("utf-8")) + "\r\n");
		}

		// 写出符合条件的url到output目录
		File dexStr = new File("output/arscStr.txt");
		dexStr.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(dexStr, true));
		pw.println(strs.toString());
		pw.flush();
		pw.close();
		raFile.close();

	}

	public void getUrl() throws IOException {
		// 获取arsc文件所有字符串偏移
		List<Integer> stringsOff = new ArrayList<>();
		for (int i = 0; i < Util.bytesToInt(gsPool.stringsCount); i++) {
			byte[] temp = new byte[4];
			raFile.read(temp);
			stringsOff.add(Util.bytesToInt(temp));
		}
		// 根据偏移获取字符串
		int stringOff = Util.bytesToInt(gsPool.stringsOff) + 12;
		String strs = "";
		for (int i = 0; i < stringsOff.size(); i++) {
			raFile.seek(stringOff + stringsOff.get(i));
			byte[] size = new byte[1];
			raFile.read(size);
			raFile.skipBytes(1);
			byte[] str = new byte[Util.bytesToInt(size)];
			raFile.read(str);
			strs = strs.concat(Util.regexUrl(new String(str, Charset.forName("utf-8")) + "\r\n"));
		}

		// 写出符合条件的url到output目录
		File dexStr = new File("output/arscUrl.txt");
		dexStr.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(dexStr, true));
		System.out.println(Util.regexUrl(strs.toString()));
		pw.println(Util.regexUrl(strs.toString()));
		pw.flush();
		pw.close();
		raFile.close();
	}

}
