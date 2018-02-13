package com.banyan.parsedex;

public class DexFile {

	//dex文件头
	class DexHeader {
		byte[] magic = new byte[8]; //dex文件头
		byte[] checksum = new byte[4]; //校验码
		byte[] signature = new byte[20]; //sha-1签名, 去除了magic、checksum和signature字段之外的所有内容的签名
		byte[] fileSize = new byte[4]; //dex文件大小
		byte[] headerSise = new byte[4]; //dex头大小
		byte[] endianTag = new byte[4]; //字节序
		byte[] linkSize = new byte[4]; //链接段的大小, 默认为0表示静态链接
		byte[] linkOff = new byte[4]; //链接段开始偏移
		byte[] mapOff = new byte[4]; //map_item偏移
		byte[] stringIdsSite = new byte[4]; //字符串列表中的字符串个数
		byte[] stringIdsOff = new byte[4]; //	字符串列表偏移
		byte[] typeIdsSize = new byte[4]; //类型列表中的类型个数
		byte[] typeIdsOff = new byte[4]; //类型列表偏移。
		byte[] protoIdsSize = new byte[4]; 	//方法声明列表中的个数
		byte[] protoIdsOff = new byte[4];		//方法声明列表偏移
		byte[] fieldIdsSize = new byte[4]; //字段列表中的个数
		byte[] fieldIdsOff = new byte[4]; //字段列表偏移
		byte[] methodIdsSize = new byte[4]; //方法列表中的个数
		byte[] methodIdsOff = new byte[4]; //方法列表偏移
		byte[] classDefsSize = new byte[4]; //类定义列表中的个数
		byte[] classDefsOff = new byte[4]; //类定义列表偏移
		byte[] dataSize = new byte[4]; //数据段的大小, 4字节对齐
		byte[] dataOff = new byte[4]; //	数据段偏移
	}
	//String索引和数据
	class DexStringIdAndItem{
		byte[] stringDataOff = new byte[4];
		byte[] stringSite = new byte[1];
		
	}
	//Type索引列表
	class DexTypeIds{
		
	}
	//方法原型索引列表
	class DexProtoIds{
		
	}
	//域索引列表
	class DexFields{
		
	}
	//方法索引列表
	class DexMethod{
		
	}

	

}
