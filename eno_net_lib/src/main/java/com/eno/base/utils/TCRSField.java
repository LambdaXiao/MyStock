package com.eno.base.utils;
/**
 * <p>Title: TCRSField��</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author cheng-dn
 * @version 1.0
 */

//2010-05-24 更新说明：
//1）结果集数据块结构修改，把内容标注说明和字段描述块中的文字部分内容放入到动态缓存数据块中去；
//2）取消内容标注说明和字段描述块中的文字部分内容的长度限制，改为特殊分隔符号限制；
//3）增加动态内容的内存双字节对齐处理，以保证在Unicode环境下取值正确；
//4）所有的文字信息内容修改为动态长度；
//

//isUnistringDesc 真正的代表的意思变为  是否中文字段,如果是中文字段，将会在
//中按StringEncodeing处理
public class TCRSField {

	public final static int sizeof = 12;
	public String fieldName;
	public String FieldDesc;
	public int fieldType;
	public int dataLength;
	public int dataOffset;
	
	public boolean isUnistringDesc;
	
	public TCRSField() {
	}

	/** ************************************* */
	/** ************字段类型******************** */
	/** *********************************** */
	public static final int fdtype_unknow = 0;
	public static final int fdtype_bool = 1;
	public static final int fdtype_int1 = 2;
	public static final int fdtype_int2 = 3;
	public static final int fdtype_int4 = 4;
	public static final int fdtype_int8 = 5;
	public static final int fdtype_real4 = 6;
	public static final int fdtype_real8 = 7;
	public static final int fdtype_string = 9; /* string */
	public static final int fdtype_array = 14; /* bytearray */
	public static final int fdtype_unistring = 101;
	public static final int fdtype_enofloat = 102;
	public static final int fdtype_enomoney = 103;

	public TCRSField(String fdName, int fdType) {
		if (fdName != null) {
			fieldName = fdName;
		} else
			fieldName = "";
		FieldDesc = "";
		fieldType = fdType;
		dataLength = 0;
		dataOffset = 0;
	}

	public TCRSField(String fdName, int fdType, String fdDesc) {
		this(fdName, fdType, fdDesc, false);
	}

	public TCRSField(String fdName, int fdType, String fdDesc,
			boolean convToUnicode) {
		if (fdName != null) {
			fieldName = fdName;
		} else
			fieldName = "";
		fieldType = fdType;
		if (fdDesc != null)
			FieldDesc = fdDesc;
		else
			FieldDesc = "";
		dataLength = dataOffset = 0;
	}


	boolean IsUniFieldDesc()
	{
		return isUnistringDesc;
	}

}