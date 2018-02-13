package com.banyan.parseelf;

public class ElfFile {

	class ElfHead{
		byte[] ident = new byte[16];
		byte[] type = new byte[2];
		byte[] machine = new byte[2];
		byte[] version = new byte[4];
		byte[] entry = new byte[4];
		byte[] phoff = new byte[4];
		byte[] shoff = new byte[4];
		byte[] flags = new byte[4];
		byte[] ehsize = new byte[2];
		byte[] phentsize = new byte[2];
		byte[] phnum = new byte[2];
		byte[] shentsize = new byte[2];
		byte[] shnum = new byte[2];
		byte[] shstrndx = new byte[2];
	}
	
	class SectionHeaderTable{
		byte[] nameOff = new byte[4];
		byte[] type = new byte[4];
		byte[] flags = new byte[4];
		byte[] addr = new byte[4];
		byte[] offset = new byte[4];
		byte[] size = new byte[4];
		byte[] link = new byte[4];
		byte[] info = new byte[4];
		byte[] addralign = new byte[4];
		byte[] entsize = new byte[4];
		
	}
	
	

}
