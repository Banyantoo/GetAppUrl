package com.banyan.parseelf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.banyan.getappurl.Util;
import com.banyan.parseelf.ElfFile.ElfHead;
import com.banyan.parseelf.ElfFile.SectionHeaderTable;

public class ParseElf {

	ElfFile elfFile= new ElfFile();//
	ElfHead elfHeader = elfFile.new ElfHead();
	SectionHeaderTable shTable = elfFile.new SectionHeaderTable();
	List<byte[]> elfHeadList;
	List<byte[]> shTableList;
	List<List<Integer>> SectionList = new ArrayList<>();
	RandomAccessFile raFile;
	
	public ParseElf(String path) throws IOException {
		File file = new File(path);
		raFile = new RandomAccessFile(file, "r");
		parseElfHeader();
		parseShTable();
		
	}
	
	/**解析elf文件头
	 * @param file elf文件
	 * @throws IOException
	 */
	public void parseElfHeader() throws IOException {
		elfHeadList = new ArrayList<>();
		elfHeadList.add(elfHeader.ident);
		elfHeadList.add(elfHeader.type);
		elfHeadList.add(elfHeader.machine);
		elfHeadList.add(elfHeader.version);
		elfHeadList.add(elfHeader.entry);
		elfHeadList.add(elfHeader.phoff);
		elfHeadList.add(elfHeader.shoff);
		elfHeadList.add(elfHeader.flags);
		elfHeadList.add(elfHeader.ehsize);
		elfHeadList.add(elfHeader.phentsize);
		elfHeadList.add(elfHeader.phnum);
		elfHeadList.add(elfHeader.shentsize);
		elfHeadList.add(elfHeader.shnum);
		elfHeadList.add(elfHeader.shstrndx);	
		
		for(int i=0; i<elfHeadList.size(); i++) {
			raFile.read(elfHeadList.get(i));
	
		}
	}
	
	
	/**解析 ection header table
	 * @throws IOException
	 */
	public void parseShTable() throws IOException {
		shTableList = new ArrayList<>();
		shTableList.add(shTable.nameOff);
		shTableList.add(shTable.type);
		shTableList.add(shTable.flags);
		shTableList.add(shTable.addr);
		shTableList.add(shTable.offset);
		shTableList.add(shTable.size);
		shTableList.add(shTable.link);
		shTableList.add(shTable.info);
		shTableList.add(shTable.addralign);
		shTableList.add(shTable.entsize);
		raFile.seek(Util.bytesToInt(elfHeader.shoff));
		for(int i =0; i<Util.bytesToInt(elfHeader.shnum); i++) {
			for(int j=0; j<shTableList.size(); j++) {
				raFile.read(shTableList.get(j));
			}
			List<Integer> test = new ArrayList<>();
			test.add(Util.bytesToInt(shTable.nameOff));
			test.add(Util.bytesToInt(shTable.offset));
			test.add(Util.bytesToInt(shTable.size));
			SectionList.add(test);
			
			
		}
		
	}
	
	public void getStrings() throws IOException {
		List<Integer> shstrtab = SectionList.get(Util.bytesToInt(elfHeader.shstrndx));
		String[] names = new String[Util.bytesToInt(elfHeader.shnum)];
		Collections.sort(SectionList, new Comparator<List<Integer>>() {

			@Override
			public int compare(List<Integer> o1, List<Integer> o2) {
				
				return o1.get(0) - o2.get(0);
			}
		});
		int j=0;
		for(List<Integer> list:SectionList) {
			raFile.seek(shstrtab.get(1) + list.get(0));
			ArrayList<Byte> temp = new ArrayList<>();
			byte test;
			while((test = raFile.readByte())!= 0){
				temp.add(test);
			}
			StringBuffer str = new StringBuffer();
			for(int i=0; i<temp.size(); i++) {
				char ttt = Util.byteToChar(temp.get(i));
				str.append(ttt);
			}
			names[j] = str.toString();
			j++;
		}
		//获取.rodata && .data && .rodata1中所有字符串
		StringBuilder strs = new StringBuilder();
		for(int i=0; i<SectionList.size(); i++) {
			if(names[i].equals(".rodata")||names[i].equals(".data")||names[i].equals(".rodata1")) {
				//System.out.println("name为：" + names[i] + "改段偏移为：" + SectionList.get(i).get(1) + "该段的大小为：" + SectionList.get(i).get(2));
				raFile.seek(SectionList.get(i).get(1));
				byte[] data = new byte[SectionList.get(i).get(2)];
				raFile.read(data);
				String[] temp = (new String(data)).split("\0");
				for(String str:temp) {
					strs.append(str + "\r\n");
				}
			}
		}
		
		//写出符合条件的url到output目录
		File dexStr = new File("output/SoStr.txt");
		dexStr.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(dexStr, true));
		pw.println(strs.toString());
		pw.flush();
		pw.close();
		raFile.close();
		
	}


}
