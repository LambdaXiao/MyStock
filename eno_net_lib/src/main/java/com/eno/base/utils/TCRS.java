package com.eno.base.utils;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * <p>Title: TCRS类</p>
 * <p>Description: ENO TCRS</p>
 * <p>Copyright: Copyright ENO(c) 2014</p>
 * <p>Company: ENO</p>
 * @author 曾景照
 * @version 2.0
 */
public class TCRS {
	protected Vector<TCRSField> fdsBuff_; // 字段描述信息缓冲
	protected Vector<Byte> recsBuff_; // 固定长度的记录内容缓冲
	protected Vector<Byte> dataBuff_; // 动态内容缓冲

	protected int nAllFields_; // 总字段数 = fdsBuff_.size()
	protected int nRecordSize_; // 单条记录的固定长度
	protected int nAllRecords_; // 总记录数 = recsBuff_.size() / nRecodeSize_
	protected int nCurrRecord_; // 当前记录内容指针

	protected String szResInfo_ = ""; // 可对结果集内容加一些特定的标注说明
	private boolean isUnistringInfo_;
	private boolean isDirty_;



	private static final int MAX_FIELDNAME_LENGTH = 64;
	private static final int MAX_FIELDDESC_LENGTH = 128;
	private static final int MAX_RESINFO_LENGTH = 128;

	private static final String RS_ERROR_INFO = "error";
	private static final String RS_OK_INFO = "ok";
	private static final String RS_ETC_INFO = "etc";

	private static final int ENOREC_DONE = 0;
	private static final int ENOREC_FORMAT_ERR = 1;
	private static final int ENOREC_RECORD_ERR = 2;

	private static final int ENOREC_FIELD_ERR = 3;
	private static final int ENOREC_UPDATE_ERR = 4;

	/** 8 位 UCS 转换格式 */
	public static final String UTF_8 = "UTF-8";

	/** 中文超大字符集 */
	public static final String GBK = "GBK";


	//使用的中文编码方式
	public static int isCode = 1;
	//具体的值
	public static  int eno_gbk_encoding = 0;
	public static  int eno_unicode_encoding = 1;
	public static  int eno_utf8_encoding = 2;
	// =============================================================
	// 一些简单结果集的构造实现
	// ============================================================
	// 单个字段 + 单条记录
	public TCRS(int nData) {
		init();
		TCRSField fdsc = new TCRSField("ER_Int", TCRSField.fdtype_int4);
		insertField(fdsc, -1);
		insertRecord(-1);
		UpdateRecord(0, nData);
	}

	public TCRS(byte[] nData) {
		init();
		this.importData(nData, 0);
	}

	public TCRS(double nData) {
		init();
		TCRSField fdsc = new TCRSField("ER_Double", TCRSField.fdtype_real8);
		insertField(fdsc, -1);
		insertRecord(-1);
		UpdateRecord(0, nData);
	}

	public TCRS(String pszData, boolean convToUnicode) {
		init();
		TCRSField fdsc = new TCRSField("ER_String",
				convToUnicode ? TCRSField.fdtype_unistring
						: TCRSField.fdtype_string);
		insertField(fdsc, -1);
		insertRecord(-1);
		UpdateRecord(1, pszData);
	}

	// 两个字段（Int + String） + 单条记录 ----- 用于返回字符串信息类的结果
	public TCRS(int nRet, String pszResult, boolean convToUnicode) {
		init();
		TCRSField fdsc1 = new TCRSField("ER_Int", TCRSField.fdtype_int4);
		TCRSField fdsc2 = new TCRSField("ER_String",
				convToUnicode ? TCRSField.fdtype_unistring
						: TCRSField.fdtype_string);
		insertField(fdsc1, -1);
		insertField(fdsc2, -1);
		insertRecord(-1);
		UpdateRecord(0, nRet);
		UpdateRecord(1, pszResult);
	}

	// =============================================================
	// 结果集构造方法
	// =============================================================
	// 使用一个完整的结果集数据块进行构造，正确返回0，错误返回>0；
	// 注意：一个结果集构造后，参数nLength会自动被减去已构造内容的长度。
	// 此参数可被用来判断是否有多个结果集数据，或者一个结果集的数据是否合法（即构造后nLength应该正好返回零）。
	// public int ImportData(byte[] pRecvData, int nLength){
	// return 0;
	// }
	// 用另外一个结果集进行复制
	public void importData(TCRS pRes) {
		if (pRes == null)
			return;

		this.nAllFields_ = pRes.nAllFields_; // 总字段数 = fdsBuff_.size()
		this.nRecordSize_ = pRes.nRecordSize_; // 单条记录的固定长度
		this.nAllRecords_ = pRes.nAllRecords_; // 总记录数 = recsBuff_.size() /
		// nRecodeSize_
		this.nCurrRecord_ = -1; // 当前记录内容指针（BOF状态）

		this.fdsBuff_.clear(); // 字段描述信息缓冲
		this.fdsBuff_.addAll(pRes.fdsBuff_);

		this.recsBuff_.clear();// 固定记录数据
		this.recsBuff_ = pRes.recsBuff_;

		this.dataBuff_.clear(); // 动态内容缓冲
		this.dataBuff_ = pRes.dataBuff_;

		// 可对结果集内容加一些特定的标注说明，最长16个字节的字符串
		// byte[] b = new byte[RECORDSET_INFO_LENGTH+1];
		this.isUnistringInfo_ = pRes.isUnistringInfo_;
		this.szResInfo_ = pRes.szResInfo_;
	}

	public TCRS() {
		init();
	}

	private void init() {
		fdsBuff_ = new Vector<TCRSField>();
		recsBuff_ = new Vector<Byte>();
		dataBuff_ = new Vector<Byte>();
		szResInfo_ = "";
	}

	public static TCRS[] buildMRS(byte[] bts, int nOffset) {
		Vector<TCRS> mrs = new Vector<TCRS>();
		byte[] pData = bts;
		int nDataSize = pData.length;
		int offset = nOffset;
		while (offset < nDataSize) {
			// int nLastSize = nDataSize;
			TCRS rs = new TCRS();
			if ((offset = rs.importData(pData, offset)) != ENOREC_FORMAT_ERR) {
				mrs.add(rs);
				// nDataSize =nLastSize - (nLastSize - nDataSize);
			} else {
				rs = null;
				break;
			}
		}

		// return (TCRS [] ) mrs.toArray();
		TCRS[] trs = new TCRS[mrs.size()];
		for (int i = 0; i < mrs.size(); i++) {
			trs[i] = mrs.elementAt(i);
		}
		return trs;
	}

	private int importData(byte[] pRecvData, int nOffset) {

		int nLength = pRecvData.length - nOffset;

		// 1、字段数量
		if (nLength < 4)
			return ENOREC_FORMAT_ERR;
		nAllFields_ = ENOUtils.bytes2Integer(pRecvData, nOffset);//获取当前结果集字段个数
		nOffset += 4;
		nLength -= 4;

		int nFDSize = nAllFields_ * TCRSField.sizeof;// 描述数据的长度
		if (nAllFields_ <= 0 || nLength < nFDSize)
			return ENOREC_FORMAT_ERR;

		// 2、字段描述结构
		if (nAllFields_ > 0) {
			try {
				TCRSField tcfield;
				int nPos = nOffset;
				for (int i = 0; i < nAllFields_; i++) {
					tcfield = new TCRSField();
					tcfield.fieldName = "";
					tcfield.fieldType = ENOUtils.bytes2Integer(pRecvData, nPos);// 前面4个字节是类型
					nPos += 4;
					tcfield.dataLength = ENOUtils
							.bytes2Integer(pRecvData, nPos);// 数据长度
					nPos += 4;
					tcfield.dataOffset = ENOUtils
							.bytes2Integer(pRecvData, nPos);// 数据的偏移位置
					nPos += 4;
					tcfield.FieldDesc = "";
					fdsBuff_.addElement(tcfield);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			nOffset += nFDSize;
			nLength -= nFDSize;
		}

		// 3、计算固定记录的长度（字节数）
		calculateRecordSize(0);

		// 4、记录数量
		if (nLength < 4)
			return ENOREC_FORMAT_ERR;
		nAllRecords_ = ENOUtils.bytes2Integer(pRecvData, nOffset);
		nOffset += 4;
		nLength -= 4;

		int nRecsSize = nAllRecords_ * nRecordSize_;
		if (nRecsSize < 0 || nLength < nRecsSize)
			return ENOREC_FORMAT_ERR;

		// 5、固定长度的记录内容
		if (nRecsSize > 0) {
			// recsBuff_ = new byte[nRecsSize];
			// System.arraycopy(pRecvData, nOffset, recsBuff_, 0, nRecsSize);
			recsBuff_.clear();
			for (int i = 0; i < nRecsSize; i++) {
				recsBuff_.add(pRecvData[nOffset + i]);
			}
			nOffset += nRecsSize;
			nLength -= nRecsSize;
		}

		// 6、结果集描述信息块：信息块总长度（4Bytes） + 描述信息内容块
		if (nLength < 4)
			return ENOREC_FORMAT_ERR;
		int nInfoBlockSize = ENOUtils.bytes2Integer(pRecvData, nOffset);

		nOffset += 4;
		nLength -= 4;

		if (nInfoBlockSize < 0 || nLength < nInfoBlockSize)
			return ENOREC_FORMAT_ERR;

		if (nInfoBlockSize > 0) {
			// 描述信息内容块：是否unicode（1Byte）+结果集内容标注 + 0x0000 + M *
			// {字段名称|是否unicode（1Byte）+字段详细描述}
			// 说明：多个描述之间采用两个连续的零字符分隔---ZZ=0x0000
			// 例子：0zxhqZZname|0名称ZZcode|0代码ZZzjcj|0最近ZZcjje|0成交金额
			int pStartPos = nOffset;
			int fdIndex = -1;
			boolean isUnistring; // 是否unicode字符串
			int pEnd = nOffset;
			while (pEnd < (nInfoBlockSize + nOffset)) {
				if (fdIndex > -1) {
					pStartPos = pEnd;
					// 取字段名称部分一直到0x0000或者EOF或者'|'
					while (pEnd < (nInfoBlockSize + nOffset)) {
						if (pRecvData[pEnd] == '|')
							break;
						if (pRecvData[pEnd] == '\0'
								&& pRecvData[pEnd + 1] == '\0')
							break;
						pEnd++;
					}
					// 写字段名称 //name|0名称
					TCRSField pFD = fdsBuff_.elementAt(fdIndex);
					if (pFD == null)
						return ENOREC_FORMAT_ERR;
					int nSize = pEnd - pStartPos;
					if (nSize >= MAX_FIELDNAME_LENGTH)
						nSize = MAX_FIELDNAME_LENGTH - 1;
					if (nSize > 0) {
						try {
							pFD.fieldName = new String(pRecvData,pStartPos,nSize, UTF_8);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							pFD.fieldName = new String(pRecvData,pStartPos,nSize);
						}
					}

					/*
					 * if (pEnd >= (nInfoBlockSize+nOffset) ) break; // 跳过
					 * '|'或者0x0000 if (pRecvData[pEnd] == '|') pEnd++; else pEnd
					 * += 2;
					 */

					if (pEnd >= (nInfoBlockSize + nOffset))
						break;
					// 跳过 '|'或者0x0000
					if (pRecvData[pEnd] == '|')
						pEnd++;
					else {
						pFD.isUnistringDesc = false;
						fdIndex++;
						pEnd += 2;
						continue;
					}

				}

				isUnistring = !(pRecvData[pEnd] == '0' || pRecvData[pEnd] == '\0');
				pStartPos = ++pEnd;
				// 取描述内容一直到0x0000或者EOF
				while (pEnd < (nInfoBlockSize + nOffset)) {
					if (pRecvData[pEnd] == '\0' && pRecvData[pEnd + 1] == '\0'&& pRecvData[pEnd + 2] != '\0')
						break;
					pEnd += 1;
				}

				if (fdIndex == -1) {
					// 之前的串是结果集的内容标注说明
					isUnistringInfo_ = isUnistring;
					int nSize = pEnd - pStartPos;
					if (nSize >= MAX_RESINFO_LENGTH)
						nSize = MAX_RESINFO_LENGTH - 1
								- (isUnistring == true ? 1 : 0);
					if (nSize > 0) {
						if (isUnistring == false)
							szResInfo_ = ENOUtils.bytes2Ascstr(pRecvData,
									pStartPos, nSize);
						else
							szResInfo_ = bytesString(pRecvData,
									pStartPos, nSize);
					}
					// 后面就是具体字段的描述内容了
					fdIndex = 0;
				} else {
					// 字段描述信息
					TCRSField pFD = fdsBuff_.elementAt(fdIndex++);
					if (pFD == null)
						return ENOREC_FORMAT_ERR;
					pFD.isUnistringDesc = isUnistring;
					int nSize = pEnd - pStartPos;
					if (nSize >= MAX_FIELDDESC_LENGTH)
						nSize = MAX_FIELDDESC_LENGTH - 1
								- (isUnistring == true ? 1 : 0);
					if (nSize > 0) {
						if (pFD.isUnistringDesc == false)
							pFD.FieldDesc = ENOUtils.bytes2Ascstr(pRecvData,
									pStartPos, nSize);
						else
							pFD.FieldDesc = bytesString(pRecvData,
									pStartPos, nSize);
					}
				}
				// 跳过0x0000
				pEnd += 2;
			}
		}
		nOffset += nInfoBlockSize;
		nLength -= nInfoBlockSize;

		// 6、动态长度的记录内容（长度4Bytes + 内容）
		if (nLength < 4)
			return ENOREC_FORMAT_ERR;
		int nDynDataSize = ENOUtils.bytes2Integer(pRecvData, nOffset);
		nOffset += 4;
		nLength -= 4;

		if (nDynDataSize < 0 || nLength < nDynDataSize)
			return ENOREC_FORMAT_ERR;

		if (nDynDataSize > 0) {
			// dataBuff_ = new byte[nRecsSize];
			// System.arraycopy(pRecvData, nOffset, dataBuff_, 0, nDynDataSize);
			dataBuff_.clear();
			for (int i = 0; i < nDynDataSize; i++) {
				dataBuff_.add(pRecvData[nOffset + i]);
			}
			nOffset += nDynDataSize;
			nLength -= nDynDataSize;
		}

		return nOffset;
	}

	/**
	 * 快速排序 Quick Sort
	 * <p>原理：
	 * 选择数组中的一个元素作为标准，将所有比标准小的元素放到左边，
	 * 所有比标准大的元素放到右边。
	 * 并对左边和右边的元素做一样的快速排序过程。
	 * @return
	 */
	private int[] quickSort(int fieldNo) {
		int[] result =  new int[nAllRecords_];
		for(int i = 0; i < nAllRecords_;i++)
			result[i] = i;
		quickSort(result, 0, result.length - 1,fieldNo);
		return result;
	}

	/**快速排序方法*/
	//从小往大排序
	private void  quickSort(int[] array, int startIndex, int endIndex,int fieldNo) {
		int lo = startIndex;    //相当于i，左
		int hi = endIndex;    //相当于j,  右

		if (lo >= hi)   // 判断是否到中间了
			return;

		TCRSField pFds = this.getField(fieldNo);
		if(pFds == null || pFds.fieldType == TCRSField.fdtype_unknow)
			return;

		//确定指针方向的逻辑变量，也就是从左搜索还是向右搜索
		boolean transfer=true;
		while (lo != hi) {
			switch(pFds.fieldType)
			{
				case TCRSField.fdtype_bool:
				case TCRSField.fdtype_int1:
				case TCRSField.fdtype_int2:
				case TCRSField.fdtype_int4:
				case TCRSField.fdtype_enofloat:
				case TCRSField.fdtype_enomoney:
				{
					this.moveTo(array[lo]);
					int p1 = this.getInt(fieldNo);
					this.moveTo(array[hi]);
					int p2 = this.getInt(fieldNo);
					if (p1 > p2) {
						//交换数字
						int temp = array[lo];
						array[lo] = array[hi];
						array[hi] = temp;
						//决定下标移动，还是上标移动
						transfer = (transfer == true) ? false : true;
					}
					break;
				}
				case TCRSField.fdtype_string:
				case TCRSField.fdtype_unistring:
				case TCRSField.fdtype_array:
				{
					this.moveTo(array[lo]);
					String p1 = this.toString(fieldNo);
					this.moveTo(array[hi]);
					String p2 = this.toString(fieldNo);
					if (p1.compareToIgnoreCase(p2) > 0) {
						//交换数字
						int temp = array[lo];
						array[lo] = array[hi];
						array[hi] = temp;
						//决定下标移动，还是上标移动
						transfer = (transfer == true) ? false : true;
					}
					break;
				}
				case TCRSField.fdtype_real4:
				case TCRSField.fdtype_real8:
				{
					this.moveTo(array[lo]);
					double p1 = this.getDouble(fieldNo);
					this.moveTo(array[hi]);
					double p2 = this.getDouble(fieldNo);
					if (p1 > p2) {
						//交换数字
						int temp = array[lo];
						array[lo] = array[hi];
						array[hi] = temp;
						//决定下标移动，还是上标移动
						transfer = (transfer == true) ? false : true;
					}
					break;
				}
			}
			//将指针向前或者向后移动
			if(transfer)
				hi--;
			else
				lo++;
		}
		//将数组分开两半，确定每个数字的正确位置
		lo--;
		hi++;
		quickSort(array, startIndex, lo,fieldNo);
		quickSort(array, hi, endIndex,fieldNo);
	}

	// ============================================================================
	// 内容标注说明：对结果集内容所加的一些特定的标注说明，最长16个字节的字符串
	// ============================================================================
	public String getRecordInfo() {
		return szResInfo_;
	}

	public void setRecordsetInfo(String pszInfo) {
		if (pszInfo != null) {
			szResInfo_ = pszInfo;
		}
		isUnistringInfo_ = false;
	}

	public void setRecordsetInfo(String pszInfo,boolean converToUnicode) {
		if (pszInfo != null) {
			szResInfo_ = pszInfo;
		}
		isUnistringInfo_ = converToUnicode;
	}

	// 一些特定的标注说明
	public void setErrorInfo() {
		setRecordsetInfo(RS_ERROR_INFO);
	}

	/**
	 * 是否有错误返回
	 *
	 * @return
	 */
	public boolean IsError() {
		// System.out.println(szResInfo_+"%%%%%%%%%");
		// return RS_ERROR_INFO.equals(szResInfo_)?true:false;
		return (RS_ERROR_INFO.compareTo(szResInfo_) == 0);
	}

	public void setOkInfo() {
		setRecordsetInfo(RS_OK_INFO);
	}

	public boolean isOkInfo() {
		return (RS_OK_INFO.compareTo(szResInfo_) == 0);
	}

	public void setETCInfo() {
		setRecordsetInfo(RS_ETC_INFO);
	}

	public boolean isETCInfo() {
		return (RS_ETC_INFO.compareTo(szResInfo_) == 0);
	}

	// =============================================================
	// 结果集访问方法
	// =============================================================
	/**
	 * 返回字段数量
	 */
	public int getFields() {
		return nAllFields_;
	}

	/**
	 * 返回记录数量
	 *
	 * @return
	 */
	public int getRecords() {
		return nAllRecords_;
	}

	/**
	 *向下移动光标
	 *
	 * @return
	 */
	public boolean moveNext() {
		if (!IsEof()) {
			nCurrRecord_++;
			return (!IsEof());
		} else {
			return false;
		}
	}

	/**
	 * 向上移动光标
	 *
	 * @return
	 */
	public boolean movePrev() {
		if (!IsBof()) {
			nCurrRecord_--;
			return (!IsBof());
		} else {
			return false;
		}
	}

	/**
	 * 当前游标
	 *
	 * @return
	 */
	public int getCurrent() {
		return nCurrRecord_;
	}

	/**
	 * 移动游标到指定的行
	 *
	 * @param nRecord
	 * @return
	 */
	public boolean moveTo(int nRecord) {
		if (nAllRecords_ == 0) {
			nCurrRecord_ = -1;
			return false;
		}

		nCurrRecord_ = nRecord;
		if (nCurrRecord_ >= nAllRecords_) {
			nCurrRecord_ = nAllRecords_;
			return false;
		} else if (nCurrRecord_ < 0) {
			nCurrRecord_ = -1;
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 移动到首条记录位置或者最后一条记录位置
	 *
	 * @return
	 */
	public boolean moveFirst() {
		if (nAllRecords_ > 0) {
			nCurrRecord_ =0;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 移动游标到最后一行
	 *
	 * @return
	 */
	public boolean moveLast() {
		if (nAllRecords_ > 0) {
			nCurrRecord_ =nAllRecords_ - 1;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否到记录的头部
	 *
	 * @return
	 */
	public boolean IsBof() {
		return (nCurrRecord_ == -1 || nAllRecords_ == 0);
	}

	/**
	 * 判断是否到记录的尾部
	 *
	 * @return
	 */
	public boolean IsEof() {
		return (nCurrRecord_ == nAllRecords_ || nAllRecords_ == 0);
	}

	/**
	 * 根据指定的字段位置，取该字段的描述信息内容
	 *
	 * @param nIndex
	 * @return
	 */
	public TCRSField getField(int nIndex) {
		if (nIndex < 0 || nIndex >= nAllFields_)
			return null;
		else
			return fdsBuff_.elementAt(nIndex);
	}

	/**
	 * 根据指定的字段名称，取该字段的描述信息内容
	 *
	 * @param pszFieldName
	 * @return
	 */
	public TCRSField getField(String pszFieldName) {
		int nIndex = getFieldsIndex(pszFieldName);
		if (nIndex > -1)
			return fdsBuff_.elementAt(nIndex);
		else
			return null;
	}

	/**
	 * 根据指定的字段名称，取该字段的列名
	 *
	 * @param fieldName
	 * @return
	 */
	public String getFieldName(String fieldName) {
		TCRSField field = getField(fieldName);
		if (field != null) {
			return field.fieldName;
		} else {
			return "";
		}

	}

	/**
	 * 根据指定的字段索引，取该字段的列名
	 *
	 * @param fieldName
	 * @return
	 */
	public String getFieldName(int fieldName) {
		TCRSField field = getField(fieldName);
		if (field != null) {
			return field.fieldName;
		} else {
			return "";
		}

	}

	/**
	 * 根据指定的字段名称，取该字段的列名
	 *
	 * @param fieldName
	 * @return
	 */
	public String getFieldDesc(String fieldName) {
		TCRSField field = getField(fieldName);
		if (field != null) {
			return field.FieldDesc;
		} else {
			return "";
		}

	}

	/**
	 * 根据指定的字段索引，取该字段的列名
	 *
	 * @param fieldName
	 * @return
	 */
	public String getFieldDesc(int fieldName) {
		TCRSField field = getField(fieldName);
		if (field != null) {
			return field.FieldDesc;
		} else {
			return "";
		}

	}

	/**
	 * 返回指定字段类型
	 *
	 * @param index
	 * @return
	 */
	public int getFieldType(int index) {
		TCRSField field = getField(index);
		return field.fieldType;
	}

	/**
	 * 返回指定字段类型
	 *
	 * @param fieldName
	 * @return
	 */
	public int getFieldType(String fieldName) {
		TCRSField field = getField(fieldName);
		return field.fieldType;
	}

	/**
	 * 返回当前字段的索引号
	 *
	 * @param pszFieldName
	 * @return
	 */
	public int getFieldsIndex(String pszFieldName) {
		if (pszFieldName == null)
			return -1;
		for (int i = 0; i < nAllFields_; i++) {
			// if (pszFieldName.compareTo(fdsBuff_.elementAt(i).fieldName) == 0)
			if (pszFieldName.equals(fdsBuff_.elementAt(i).fieldName))
				return i;
		}
		return -1;
	}

	/**
	 * 根据字段的数据类型，返回其对应的长度
	 *
	 * @param nType
	 * @return
	 */
	protected int getDataTypeLength(int nType) {
		switch (nType) {
			case TCRSField.fdtype_bool:
			case TCRSField.fdtype_int1:
				return 1;
			case TCRSField.fdtype_int2:
				return 2;
			case TCRSField.fdtype_real4:
			case TCRSField.fdtype_int4:
				return 4;
			case TCRSField.fdtype_int8:
			case TCRSField.fdtype_real8:
				return 8;
			case TCRSField.fdtype_string:
			case TCRSField.fdtype_unistring:
			case TCRSField.fdtype_array:
				return 8; // 8个字节，前4个字节存放位置指针，后4个字节存放长度。（字符串必须是0字节结尾，UnicodeString则为两个0字节结尾）
			case TCRSField.fdtype_enofloat:
			case TCRSField.fdtype_enomoney:
				return 4;
			case TCRSField.fdtype_unknow:
			default:
				return 0;
		}
	}

	// 计算单条记录的固定内容长度，并设置每个字段描述中的长度和相对偏移位置
	// 参数：nOff开始计算的字段位置，-1表示不做计算，直接赋值长度
	protected void calculateRecordSize(int nOff) {
		if (nOff < 0) {
			if (nAllFields_ > 0)
				nRecordSize_ = fdsBuff_.elementAt(nAllFields_ - 1).dataOffset
						+ fdsBuff_.elementAt(nAllFields_ - 1).dataLength;
			else
				nRecordSize_ = 0;
			return;
		}
		if (nOff < nAllFields_) {
			if (nOff > 0)
				nRecordSize_ = fdsBuff_.elementAt(nOff - 1).dataOffset
						+ fdsBuff_.elementAt(nOff - 1).dataLength;
			else
				nRecordSize_ = 0;

			for (int i = nOff; i < nAllFields_; i++) {
				fdsBuff_.elementAt(i).dataOffset = nRecordSize_;
				nRecordSize_ += fdsBuff_.elementAt(i).dataLength;
			}
		} else {
			if (nAllFields_ > 0)
				nRecordSize_ = fdsBuff_.elementAt(nAllFields_ - 1).dataOffset
						+ fdsBuff_.elementAt(nAllFields_ - 1).dataLength;
			else
				nRecordSize_ = 0;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////
	// 根据指定的字段位置或字段名称，取当前记录中对应字段的内容指针，
	// 指针类型由字段的数据类型决定，参数nOutSize中返回该数据的长度。

	public byte[] FieldValue(String pszFieldName) {
		int nIndex = getFieldsIndex(pszFieldName);
		return fieldValue(nIndex);
	}

	/**
	 * 取字段内容
	 */
	public byte[] fieldValue(int nIndex) {
		if (IsBof() || IsEof())
			return null;

		TCRSField pFDesc = getField(nIndex);
		if (pFDesc == null)
			return null;
		int index = nCurrRecord_;
		int pRec =index* nRecordSize_;
		pRec += pFDesc.dataOffset;

		byte[] pRecData = null;

		if (pFDesc.fieldType == TCRSField.fdtype_string
				|| pFDesc.fieldType == TCRSField.fdtype_unistring
				|| pFDesc.fieldType == TCRSField.fdtype_array) {
			// 动态内容相对偏移位置
			byte[] b = new byte[4];
			for (int i = 0; i < 4; i++)
				b[i] = recsBuff_.elementAt(pRec + i);
			int nOffset = ENOUtils.bytes2Integer(b, 0);
			// int nOffset = ENOUtils.bytes2Integer(recsBuff_, pRec );
			// 动态内容长度
			for (int i = 0; i < 4; i++)
				b[i] = recsBuff_.elementAt(pRec + 4 + i);
			// int nOutSize = ENOUtils.bytes2Integer(recsBuff_,(pRec +4));
			int nOutSize = ENOUtils.bytes2Integer(b, 0);
			// int nOffset = pFDesc.dataOffset;
			// int nOutSize = pFDesc.dataLength;
			pRecData = new byte[nOutSize];
			if (nOutSize < 1)
				return null;
			else {
				// System.arraycopy(dataBuff_, nOffset, pRecData, 0, nOutSize);
				for (int i = 0; i < nOutSize; i++) {
					pRecData[i] = dataBuff_.elementAt(nOffset + i);
				}
			}
		} else {
			int nOutSize = pFDesc.dataLength;
			pRecData = new byte[nOutSize];
			// System.arraycopy(recsBuff_, pRec, pRecData, 0, nOutSize );
			for (int i = 0; i < nOutSize; i++) {
				pRecData[i] = recsBuff_.elementAt(pRec + i);
			}
		}
		return pRecData;
	}

	/**
	 * 获取错误信息
	 *
	 * @return
	 */
	public String getErrMsg() {
		this.moveFirst();
		return this.toString(0);
	}

	/**
	 * 获取第一行的第一列数据
	 *
	 * @return
	 */
	public String getStrRes() {
		this.moveFirst();
		return this.toString(0);
	}

	/**
	 * 如果确定是数字字符串的话
	 *
	 * @param nIndex
	 * @return
	 */
	public String getNubmerString(int nIndex) {
		byte[] felidData = fieldValue(nIndex);
		if (felidData == null)
			return "0";
		else {
			return ENOUtils.bytes2Ascstr(felidData, 0, felidData.length);

		}
	}

	/**
	 * 如果确定是数字字符串的话
	 *
	 * @param pszFieldName
	 * @return
	 */
	public String getNubmerString(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);
		return getNubmerString(index);
	}

	/**
	 * 如果确定是数字字符串的话
	 *
	 * @param nIndex
	 * @return
	 */
	public String getNubmerFloat(int nIndex) {
		byte[] felidData = fieldValue(nIndex);
		if (felidData == null)
			return "0";
		else {
			return ENOUtils.bytes2Ascstr(felidData, 0, felidData.length);

		}
	}

	/**
	 * 取当前记录中，某一字段的值
	 *
	 * @param nIndex
	 * @return
	 */
	public String getString(int nIndex) {
		byte[] felidData = fieldValue(nIndex);
		if (felidData == null)
			return "-";
		else {
			TCRSField pFDesc = getField(nIndex);
			if (pFDesc.fieldType == TCRSField.fdtype_unistring) {

				return bytesString(felidData, 0, felidData.length);
			} else {

				int nLen = ENOUtils.bytes2AscstrLen(felidData, 0) - 1;
				try {
					return new String(felidData, 0, nLen, "gb2312");
				} catch (UnsupportedEncodingException e) {
					return new String(felidData, 0, nLen);
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据列名取当前记录中，某一字段的值
	 *
	 * @param pszFieldName
	 * @return
	 */
	public String getString(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);
		if (index != -1) {
			return getString(index);
		}
		return "";
	}

	public int getShortInt(int nIndex) {
		byte[] felidData = fieldValue(nIndex);
		return ENOUtils.bytes2Short(felidData, 0);
	}

	public int getShortInt(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);
		if (index != -1) {
			return getShortInt(index);
		}
		return -1;
	}

	public int getInt(int nIndex) {

		byte[] felidData = fieldValue(nIndex);
		return ENOUtils.bytes2Integer(felidData, 0);
	}

	public int getInt(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);
		if (index != -1) {
			return getInt(index);
		}
		return -1;
	}

	public long getLong(int nIndex) {
		byte[] felidData = fieldValue(nIndex);
		return ENOUtils.bytes2Long(felidData, 0);
	}

	public long getLong(String fieldName) {
		int index = getFieldsIndex(fieldName);
		if (index > 0) {
			byte[] felidData = fieldValue(index);

			return ENOUtils.bytes2Long(felidData, 0);
		} else {
			return 0;
		}

	}

	/**
	 * 按索引取int类型的数据
	 *
	 * @param nIndex
	 * @param defaultValue
	 *            当值为空时默认取值
	 * @return
	 */
	// public int getInt(int nIndex) {
	// int temp = 0;
	// String tempStr = this.getString(nIndex);
	// try {
	// temp = Integer.parseInt(tempStr);
	// } catch (Exception e) {
	//
	// }
	//
	// return temp;
	// }

	/**
	 * 按索引取int类型的数据
	 *
	 * @param nIndex
	 * @param defaultValue
	 *            当值为空时默认取值
	 * @return
	 */
	public int getInt(int nIndex, int defaultValue) {
		int temp = 0;
		String tempStr = this.toString(nIndex);

		if (tempStr == null || tempStr == "") {
			temp = defaultValue;
		} else {
			try {
				temp = Integer.parseInt(tempStr);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		return temp;
	}

//	/**
//	 * 根据字段取int数据
//	 *
//	 * @param pszFieldName
//	 * @return
//	 */
	// public int getInt(String pszFieldName) {
	// int temp = 0;
	// int index = getFieldsIndex(pszFieldName);
	// String tempStr = this.getString(index);
	// try {
	// temp = Integer.parseInt(tempStr);
	// } catch (Exception e) {
	//
	// System.err.println(e);
	// }
	// return temp;
	// }

	public double getFloat(int nIndex) {
		byte[] felidData = fieldValue(nIndex);
		return ENOUtils.bytes2Long(felidData, 0);// ENOUtils.byteToDouble(felidData);

	}


	public float getFloat2(int nIndex) {
		byte[] felidData = fieldValue(nIndex);
		return ENOUtils.byteToFloat(felidData);// ENOUtils.byteToDouble(felidData);

	}

	public float getFloat2(String pszFieldName)
	{
		int index = getFieldsIndex(pszFieldName);
		if (index != -1) {
			return getFloat2(index);
		}
		return 0.0f;
	}



	public double getFloat(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);
		if (index != -1) {
			return getFloat(index);
		}
		return -1;
	}

	public byte getByte(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);
		return getByte(index);
	}

	public byte getByte(int index) {
		byte[] felidData = fieldValue(index);
		return felidData[0];
	}

	/**
	 * 根据列索引取Boolean值
	 *
	 * @param nIndex
	 *            第几列
	 * @return boolean
	 */
	public boolean getBoolean(int nIndex) {
		boolean ifboolean = false;
		String temp = this.toString(nIndex).toLowerCase();

		if (temp.equals("1")) {
			ifboolean = true;
		} else {
			ifboolean = false;
		}

		return ifboolean;
	}

	/**
	 * 根据列名取Boolean值
	 *
	 * @param pszFieldName
	 *            列名
	 * @return boolean
	 */
	public boolean getBoolean(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);
		boolean ifboolean = false;
		String temp = this.getString(index).toLowerCase();
		if (temp.equals("1")) {
			ifboolean = true;
		} else {
			ifboolean = false;
		}

		return ifboolean;
	}

	public ENOFloat getENOFloat(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);

		return getENOFloat(index);
	}

	public ENOFloat getENOFloat(int nIndex) {
		TCRSField pFDesc = getField(nIndex);
		if (pFDesc == null)
			return null;
		byte[] felidData = fieldValue(nIndex);
		if (felidData == null)
			return null;

		return new ENOFloat(ENOUtils.bytes2Integer(felidData, 0));
	}

	public double getDouble(int nIndex) {
		byte[] felidData = fieldValue(nIndex);
		return ENOUtils.byteToDouble(felidData);
	}

	public double getDouble(String pszFieldName) {
		int index = getFieldsIndex(pszFieldName);
		byte[] felidData = fieldValue(index);
		return ENOUtils.byteToDouble(felidData);
	}

	public byte[] getBytes(String pszFieldName)
	{
		int index = getFieldsIndex(pszFieldName);
		return getBytes(index);
	}

	public byte[] getBytes(int nIndex)
	{
		byte[] felidData = fieldValue(nIndex);
		return  felidData;
	}

	/**
	 * 强制转换后输出当前记录中，某一字段的值
	 *
	 * @param nIndex
	 * @return
	 */
	public String toString(int nIndex) {
		TCRSField pFDesc = getField(nIndex);
		if (pFDesc == null)
			return null;
		byte[] felidData = fieldValue(nIndex);
		if (felidData == null)
			return null;
		int nLen;
		// System.out.println("wd======" + pFDesc.fieldType);
		switch (pFDesc.fieldType) {
			case TCRSField.fdtype_string:
				nLen = ENOUtils.bytes2AscstrLen(felidData, 0) - 1;
				try {
					return new String(felidData, 0, nLen, "gb2312");
				} catch (UnsupportedEncodingException e) {
					return new String(felidData, 0, nLen);
					// e.printStackTrace();
				}
				// return ENOUtils.bytes2Ascstr(felidData);
			case TCRSField.fdtype_unistring:
				return bytesString(felidData);
			case TCRSField.fdtype_real4:
				// System.out.println("ssssssssssss" +
				// ENOUtils.byteToFloat(felidData));
				return String.valueOf(ENOUtils.byteToFloat(felidData));
			case TCRSField.fdtype_int4:
				return String.valueOf(ENOUtils.bytes2Integer(felidData, 0));
			case TCRSField.fdtype_real8:
				return String.valueOf(ENOUtils.byteToDouble(felidData));
			case TCRSField.fdtype_int8:
				// return String.valueOf(ENOUtils.bytes2Integer(felidData, 0));
				return String.valueOf(ENOUtils.bytes2Long(felidData, 0));

			case TCRSField.fdtype_bool:
			case TCRSField.fdtype_int1:
				return String.valueOf(felidData[0]);
			case TCRSField.fdtype_int2:
				return String.valueOf(ENOUtils.bytes2Short(felidData, 0));
			case TCRSField.fdtype_enofloat:
				if (ENOUtils.bytes2Integer(felidData, 0) == 999999999)
					return "999999999";
				else
					return new ENOFloat(ENOUtils.bytes2Integer(felidData, 0))
							.toString();
			case TCRSField.fdtype_enomoney:
				// System.out.println("wd======" + ENOUtils.bytes2Integer(felidData,
				// 0));
				if (ENOUtils.bytes2Integer(felidData, 0) == 999999999)
					return "999999999";
				else
					return new ENOFloat(ENOUtils.bytes2Integer(felidData, 0))
							.toMoney();
			case TCRSField.fdtype_array:
				return felidData.toString();
			default:
				return "";
		}
	}



	/**
	 * 强制转换后输出当前记录中，某一字段的值
	 * @param nIndex
	 * @return
	 */
	public double toFloat(int nIndex) {
		TCRSField pFDesc = getField(nIndex);
		if (pFDesc == null)
			return 0;
		byte[] felidData = fieldValue(nIndex);
		if (felidData == null)
			return 0;
		int nLen;
		String tmp = "0";
		// System.out.println("wd======" + pFDesc.fieldType);
		switch (pFDesc.fieldType) {
			case TCRSField.fdtype_string:
				nLen = ENOUtils.bytes2AscstrLen(felidData, 0) - 1;
				try {
					tmp = new String(felidData, 0, nLen, GBK);
				} catch (UnsupportedEncodingException e) {
					tmp = new String(felidData, 0, nLen);
					// e.printStackTrace();
				}
				return Double.valueOf(tmp).doubleValue();
			case TCRSField.fdtype_unistring:
				tmp = bytesString(felidData);
				return Double.valueOf(tmp).doubleValue();
			case TCRSField.fdtype_real4:
				return ENOUtils.byteToFloat(felidData);
			case TCRSField.fdtype_int4:
				return ENOUtils.bytes2Integer(felidData, 0);
			case TCRSField.fdtype_real8:
				return ENOUtils.byteToDouble(felidData);
			case TCRSField.fdtype_int8:
				return ENOUtils.bytes2Long(felidData, 0);
			case TCRSField.fdtype_bool:
			case TCRSField.fdtype_int1:
				return felidData[0];
			case TCRSField.fdtype_int2:
				return ENOUtils.bytes2Short(felidData, 0);
			case TCRSField.fdtype_enofloat:
			case TCRSField.fdtype_enomoney:
				int nTmp = ENOUtils.bytes2Integer(felidData, 0);
				if ( nTmp == 999999999 ) return nTmp;
				else {
					ENOFloat efloat = new ENOFloat(ENOUtils.bytes2Integer(felidData, 0));
					double dRtn = (double)efloat.m_nValue / efloat.getUnit();
					if (pFDesc.fieldType == TCRSField.fdtype_enomoney)
					{
						if (efloat.m_nUnit == 1) dRtn *= 10000; //万
						else if (efloat.m_nUnit == 2) dRtn *= 100000000; //亿
					}
					return dRtn;
				}
			case TCRSField.fdtype_array:
			default:
				return 0;
		}
	}

	/**
	 * 不管任何类型都按浮点数返回
	 *
	 * @param pszFieldName
	 * @return
	 */
	public double toFloat(String pszFieldName) {

		int index = getFieldsIndex(pszFieldName);
		if (index < 0) {
			return 0;
		}
		return toFloat(index);

	}



	/**
	 * 不管任何类型都按字符串返回
	 *
	 * @param pszFieldName
	 * @return
	 */
	public String toString(String pszFieldName) {

		int index = getFieldsIndex(pszFieldName);
		if (index < 0) {
			return "";
		}
		return toString(index);
	}

	public String getUniString(int index) {
		return toString(index);
	}

	public String getUniString(String pszFieldName) {
		return toString(pszFieldName);
	}

//	/**
//	 * 按索引取int类型的数据
//	 *
//	 * @param nIndex
//	 * @param defaultValue
//	 *            当值为空时默认取值
//	 * @return
//	 */
	// public int toInt(int nIndex) {
	// return Integer.parseInt(this.toString(nIndex));
	// }
	//
	// /**
	// * 按索引取int类型的数据
	// *
	// * @param nIndex
	// * @param defaultValue
	// * 当值为空时默认取值
	// * @return
	// */
	// public int toInt(int nIndex, int defaultValue) {
	// int temp = 0;
	// String tempStr = this.getString(nIndex);
	//
	// if (tempStr == null || tempStr == "") {
	// temp = defaultValue;
	// } else {
	// try {
	// temp = Integer.parseInt(tempStr);
	// } catch (Exception e) {
	// System.err.println(e);
	// }
	// }
	// return temp;
	// }
	//
	// /**
	// * 根据字段取int数据
	// *
	// * @param pszFieldName
	// * @return
	// */
	// public int toInt(String pszFieldName) {
	// int temp = 0;
	// int index = getFieldsIndex(pszFieldName);
	// String tempStr = this.getString(index);
	// try {
	// temp = Integer.parseInt(tempStr);
	// } catch (Exception e) {
	//
	// System.err.println(e);
	// }
	// return temp;
	// }
	//
	// public float toFloat(int nIndex) {
	// return Float.parseFloat(this.toString(nIndex));
	// }
	//
	// public double toDouble(int nIndex) {
	// return Double.parseDouble(this.toString(nIndex));
	// }

	// /////////////////结果集操作////////////////////////////////

	// 增加一项字段，正确返回0，错误返回>0。参数nOff表示在第几个字段前插入该字段，默认-1表示添加到末尾
	public int insertField(TCRSField pFdDesc, int nOff) {
		// 1、先计算下该字段的长度
		pFdDesc.dataLength = getDataTypeLength(pFdDesc.fieldType);

		// 2、添加到字段描述信息队列中
		// 根据插入位置，我们需要做些调整，这里先保存下旧的信息
		int nOldRecSize = nRecordSize_;
		if (nOff > -1 && nOff < nAllFields_) {
			fdsBuff_.insertElementAt(pFdDesc, nOff);
			nAllFields_++;
			// 重新计算下位置顺序
			calculateRecordSize(nOff);
			pFdDesc.dataOffset = fdsBuff_.get(nOff).dataOffset;
		} else {
			fdsBuff_.add(pFdDesc);
			// 被添加到了末尾
			nAllFields_++;
			// 重新计算下位置顺序
			calculateRecordSize(nAllFields_ - 1);
			pFdDesc.dataOffset = fdsBuff_.get(nAllFields_ - 1).dataOffset;
		}

		// 3、调整下已有的固定记录内容
		if (nAllRecords_ > 0) {
			int pszNew = pFdDesc.dataLength;
			int offset = 0;
			for (int i = 0; i < nAllRecords_; i++) {
				offset = (i * nOldRecSize) + (i * pszNew) + pFdDesc.dataOffset;
				for (int j = 0; j < pszNew; j++)
					recsBuff_.insertElementAt((byte) '\0', offset);
			}
		}
		return ENOREC_DONE;
	}

	// 删除一项字段，正确返回0，错误返回>0。参数nFDNo为要删除字段的序号（0-Base）
	public int DeleteField(int nFDNo) {
		TCRSField pFdDesc = getField(nFDNo);
		if (pFdDesc == null)
			return ENOREC_FIELD_ERR;

		// 1、先删除记录中对应的字段内容
		if (nAllRecords_ > 0) {
			Vector<Byte> tempBuff = new Vector<Byte>();
			int pszOrg = 0;
			int nFrontRemainSize = pFdDesc.dataOffset; //字段前保留数据
			//字段后保留数据
			int nRemainSize = nRecordSize_ - pFdDesc.dataOffset - pFdDesc.dataLength;
			for (int i=0; i<nAllRecords_; i++) {
				if (nFrontRemainSize > 0)
				{
					for(int j = 0 ; j < nFrontRemainSize; j++)
					{
						tempBuff.add(recsBuff_.elementAt(pszOrg+j));
					}
				}
				if (nRemainSize > 0)
				{
					int pos = pszOrg + pFdDesc.dataOffset + pFdDesc.dataLength;
					for(int j = 0 ; j < nRemainSize; j++)
					{
						tempBuff.add(recsBuff_.elementAt(pos+j));
					}
				}
				pszOrg += nRecordSize_;
			}
			// 复制到记录缓冲
			recsBuff_ = tempBuff;

			if ((pFdDesc.fieldType == TCRSField.fdtype_string) || (pFdDesc.fieldType == TCRSField.fdtype_unistring) || (pFdDesc.fieldType == TCRSField.fdtype_array)) {
				isDirty_ = true;
			}
		}
		// 2、删除字段内容
		nAllFields_--;
		if (nAllFields_ > nFDNo) {
			fdsBuff_.remove(nFDNo);
		}

		// 3、重新计算下位置顺序
		calculateRecordSize(nFDNo);
		return ENOREC_DONE;
	}

	public int DeleteField(String pszFieldName) {
		int nIndex = getFieldsIndex(pszFieldName);
		if (nIndex > -1)
			return DeleteField(nIndex);
		else
			return ENOREC_FIELD_ERR;
	}

	/**
	 * 增加一条空记录，正确返回0，错误返回>0。
	 *
	 * @param nOff
	 * 参数nOff表示在第几条记录前插入该记录
	 *            ，默认-1表示添加到末尾
	 * @return
	 */
	public int insertRecord(int nOff) {
		if (nOff > -1 && nOff < nAllRecords_) {
			nAllRecords_++;
			// recsBuff_.reset(nAllRecords_ * nRecordSize_);
			int beginIndex = nOff * nRecordSize_;
			for (int j = 0; j < nRecordSize_; j++)
				recsBuff_.insertElementAt((byte) '\0', beginIndex);
			nCurrRecord_ = nOff;
		} else {
			nAllRecords_++;
			for (int j = 0; j < nRecordSize_; j++)
				recsBuff_.add((byte) '\0');
			nCurrRecord_ = nAllRecords_ - 1;
		}

		return ENOREC_DONE;
	}

	// 删除一条记录，正确返回0，错误返回>0。参数nRecordNo表示要删除的记录序号（0-Base），默认-1表示当前记录
	public int DeleteRecord(int nRecordNo) {
		if (nRecordNo == -1)
			nRecordNo = nCurrRecord_;

		if (nRecordNo < 0 || nRecordNo >= nAllRecords_)
			return ENOREC_RECORD_ERR;

		// 在删除该记录前，先判断下该记录是否存在动态数据
		int pRecData = nRecordNo * nRecordSize_;
		if (dataBuff_.size() > 0) {
			TCRSField pFDs;
			for (int i=0; i<nAllFields_; i++) {
				pFDs = fdsBuff_.elementAt(i);
				if (pFDs.fieldType == TCRSField.fdtype_string ||
						pFDs.fieldType == TCRSField.fdtype_unistring ||
						pFDs.fieldType == TCRSField.fdtype_array) {

					byte[] b = new byte[4];
					for (int pos = 0; pos < 4; pos++)
						b[pos] = recsBuff_.elementAt(pRecData + pFDs.dataOffset + 4 + pos);
					int nDynDataLength = ENOUtils.bytes2Integer(b, 0);
					if (nDynDataLength > 0) {
						isDirty_ = true;
						break;
					}
				}
			}
		}

		// 清除记录内容
		if (nRecordNo < nAllRecords_) {
			for (int i = 0; i < nRecordSize_; i++) {
				recsBuff_.removeElementAt(pRecData);
			}
		}
		nAllRecords_--;

		// 看看当前记录号是否需要修改
		if (nAllRecords_ == 0) {
			nCurrRecord_ = -1;
		} else if (nAllRecords_ < nCurrRecord_) {
			nCurrRecord_ = nAllRecords_;
		}

		return ENOREC_DONE;
	}

	/**
	 * 更新记录内容，
	 *
	 * @param nFDNo
	 *            为要更新字段的序号（0-Base）
	 * @param pData
	 * @param nLength
	 * @param nRecordNo
	 *            表示要更新的记录序号（0-Base）默认-1表示当前记录
	 * @return
	 */
	public int UpdateRecord(int nFDNo, byte[] pData, int nLength, int nRecordNo) {
		if (nRecordNo == -1)
			nRecordNo = nCurrRecord_;

		if (nRecordNo < 0 || nRecordNo >= nAllRecords_)
			return ENOREC_RECORD_ERR;

		TCRSField pFDesc = getField(nFDNo);
		if (pFDesc == null || pFDesc.fieldType == TCRSField.fdtype_unknow)
			return ENOREC_FIELD_ERR;

		// 开始更新该记录的字段内容
		// 注意：有三种类型的数据内容是存放在动态缓冲中的：fdtype_string、fdtype_unistring、fdtype_array
		int pRecData = nRecordNo * nRecordSize_;
		if (pFDesc.fieldType == TCRSField.fdtype_string
				|| pFDesc.fieldType == TCRSField.fdtype_unistring
				|| pFDesc.fieldType == TCRSField.fdtype_array) {

			//先检查是否有旧数据
			byte[] b = new byte[4];
			for (int i = 0; i < 4; i++)
				b[i] = recsBuff_.elementAt(pRecData + pFDesc.dataOffset + 4 + i);
			int nDynDataLength = ENOUtils.bytes2Integer(b, 0);
			if (nDynDataLength > 0) {
				isDirty_ = true;
			}

			// 先保存动态内容偏移位置
			int nOffset = dataBuff_.size();
			ENOUtils.integer2Bytes(b, 0, nOffset);
			int begin = pRecData + pFDesc.dataOffset;
			for (int i = 0; i < 4; i++) {
				recsBuff_.set(begin + i, b[i]);
			}
			// 保存动态内容
			for (int i = 0; i < nLength; i++) {
				dataBuff_.add(pData[i]);
			}
			if(nLength%2 != 0)
			{
				dataBuff_.add((byte)'\0');
			}

			// 保存动态内容长度
			ENOUtils.integer2Bytes(b, 0, nLength);
			for (int i = 0; i < 4; i++) {
				recsBuff_.set(begin + 4 + i, b[i]);
			}
		} else {
			if (pFDesc.dataLength != nLength)
				return ENOREC_UPDATE_ERR;
			for (int i = 0; i < nLength; i++) {
				recsBuff_.set(pRecData + pFDesc.dataOffset + i, pData[i]);
			}
		}
		return ENOREC_DONE;
	}

	// 更新当前记录的内容,数字型数据
	public int UpdateRecord(int nFDNo, Number nData) {
		if (nCurrRecord_ < 0 || nCurrRecord_ >= nAllRecords_)
			return ENOREC_RECORD_ERR;

		TCRSField pFDesc = getField(nFDNo);
		if (pFDesc == null)
			return ENOREC_FIELD_ERR;

		byte[] b = null;
		int offset = nCurrRecord_ * nRecordSize_ + pFDesc.dataOffset;
		switch (pFDesc.fieldType) {
			case TCRSField.fdtype_bool:
				recsBuff_
						.set(offset, (nData.intValue() != 0) ? (byte) 1 : (byte) 0);
				break;
			case TCRSField.fdtype_int1:
				recsBuff_.set(offset, nData.byteValue());
				break;
			case TCRSField.fdtype_real4:// float
				b = ENOUtils.floatToByte(nData.floatValue());
				for (int i = 0; i < b.length; i++) {
					recsBuff_.set(offset + i, b[i]);
				}
				break;
			case TCRSField.fdtype_real8:// double
				b = ENOUtils.doubleToByte(nData.doubleValue());
				for (int i = 0; i < b.length; i++) {
					recsBuff_.set(offset + i, b[i]);
				}
				break;
			case TCRSField.fdtype_int8:
				b = ENOUtils.LongToByte(nData.longValue(), 0);
				for (int i = 0; i < b.length; i++) {
					recsBuff_.set(offset + i, b[i]);
				}
				break;
			case TCRSField.fdtype_int2:
				b = new byte[2];
				ENOUtils.short2Bytes(b, 0, nData.shortValue());
				recsBuff_.set(offset, b[0]);
				recsBuff_.set(offset + 1, b[1]);
				break;
			case TCRSField.fdtype_int4:
			case TCRSField.fdtype_enofloat:
			case TCRSField.fdtype_enomoney:
				b = new byte[4];
				ENOUtils.integer2Bytes(b, 0, nData.intValue());
				recsBuff_.set(offset, b[0]);
				recsBuff_.set(offset + 1, b[1]);
				recsBuff_.set(offset + 2, b[2]);
				recsBuff_.set(offset + 3, b[3]);
				break;
			default:
				return ENOREC_FIELD_ERR;
		}

		return ENOREC_DONE;
	}

	// 更新当前记录的内容,字符型数据
	public int UpdateRecord(int nFDNo, String pszData) {
		byte[] data = null;
		TCRSField pFDesc = getField(nFDNo);
		if(pFDesc.fieldType == TCRSField.fdtype_unistring)
		{
			if(isCode == eno_utf8_encoding)
			{
				try {
					data = pszData.getBytes(UTF_8);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(isCode == eno_unicode_encoding)
			{
				data = ENOUtils.unistr2Bytes(pszData);
			}
			else
			{
				data = pszData.getBytes();
			}
		}
		else
		{
			data = pszData.getBytes();
		}
		return UpdateRecord(nFDNo, data, data.length, nCurrRecord_);
	}

	//=============================================================
	// // 一些维护方法：
	// //=============================================================
	// // 把记录从头至尾倒转一遍，即第一条记录变为最后一条记录，而最后一条变为第一条记录，依此类推
	public boolean ReverseData(){
		if (nAllRecords_ > 0) {
			if (nAllRecords_ * nRecordSize_ != recsBuff_.size())
				return false;
			Vector<Byte> tempBuff = new Vector<Byte>();
			int pRecsData = recsBuff_.size();
			for (int i=nAllRecords_-1; i>-1; i--) {
				pRecsData -= nRecordSize_;
				for(int j = 0 ; j < nRecordSize_;j++)
				{
					tempBuff.add(recsBuff_.elementAt(pRecsData + j));
				}
			}
			recsBuff_ = tempBuff;
		}
		return true;
	}

	// 清空当前结果集内容，默认清空包括字段描述内容的所有内容
	private void RemoveAll(boolean withFields) {
		if (withFields) {
			nAllFields_ = 0; // 总字段数 = fdsBuff_.size()
			nRecordSize_ = 0; // 单条记录的固定长度
			fdsBuff_.clear();
		}

		nAllRecords_ = 0; // 总记录数 = recsBuff_.size() / nRecordSize_
		nCurrRecord_ = -1; // 当前记录内容指针

		recsBuff_.clear(); // 固定长度的记录内容缓冲
		dataBuff_.clear(); // 动态内容缓冲
		isUnistringInfo_ = false;
		szResInfo_ = "";
		isDirty_ = false;
	}

	// 为了加快删除的速度，在删除一些记录后可能会多出些空余的数据缓冲，通过该方法可以减少些数据缓冲的内存开销
	public boolean ShrinkData(){
		if (isDirty_) {
			// 先把旧的动态数据存放到一个新的临时缓冲中
			Vector<Byte> tempBuff = new Vector<Byte>();
			int pos = 0;
			byte[] num = new byte[4];
			for (int n=0; n<nAllFields_; n++) {
				TCRSField pFDesc = fdsBuff_.elementAt(n);
				if (pFDesc.fieldType == TCRSField.fdtype_string ||
						pFDesc.fieldType == TCRSField.fdtype_unistring ||
						pFDesc.fieldType == TCRSField.fdtype_array) {
					for (int i=0; i<nAllRecords_; i++) {
						pos = i * nRecordSize_;
						for(int j = 0; j < 4;j++)
						{
							num[j] = recsBuff_.elementAt(pos + pFDesc.dataOffset + j);
						}
						int nOffset = ENOUtils.bytes2Integer(num, 0);
						for(int j = 0; j < 4;j++)
						{
							num[j] = recsBuff_.elementAt(pos + pFDesc.dataOffset + 4 + j);
						}
						int nLength = ENOUtils.bytes2Integer(num, 0);
						if (nLength > 0) {
							int nNewOff = tempBuff.size();
							for(int s = 0; s < nLength ; s++)
							{
								tempBuff.add(dataBuff_.elementAt(nOffset + s));
							}
							// 保存动态内容新位置
							ENOUtils.integer2Bytes(num, 0, nNewOff);
							for (int j = 0; j < 4; j++) {
								recsBuff_.set(pos + pFDesc.dataOffset + j, num[j]);
							}
							//这里处理双字节对齐模式：如果是单数字节，后面添加一个零字符进行对齐
							if (nLength%2 != 0)
							{
								tempBuff.add((byte)'\0');
							}
						}
					}
				}
			}

			// 再把新整理后的数据存回到动态数据缓冲中
			dataBuff_ = tempBuff;
			isDirty_ = false;
			return true;
		}
		return false;
	}



	//
	// //=============================================================
	// // 其它的方法
	// //=============================================================
	// // 对某个字段进行排序
	public boolean Sort(int nFDNo, boolean ascOrder){

		int[] resault =  this.quickSort(nFDNo);
		if(resault == null || resault.length == 0)
			return false;
		Vector<Byte> tempBuff = new Vector<Byte>();
		if(ascOrder)
		{
			int offset = 0;
			for(int i = 0 ; i < resault.length; i++)
			{
				offset = resault[i] * nRecordSize_;
				for(int j = 0;j< nRecordSize_;j++)
				{
					tempBuff.add(recsBuff_.elementAt(offset+j));
				}
			}
		}
		else
		{
			int offset = 0;
			for(int i = resault.length ; i >0; i--)
			{
				offset = resault[i-1] * nRecordSize_;
				for(int j = 0;j< nRecordSize_;j++)
				{
					tempBuff.add(recsBuff_.elementAt(offset+j));
				}
			}
		}
		recsBuff_ = tempBuff;
		return true;
	}

	public boolean Sort(String fieldName,boolean ascOrder)
	{
		int nFno = this.getFieldsIndex(fieldName);
		if(nFno > -1)
		{
			return Sort(nFno,ascOrder);
		}
		return false;
	}


	private byte[] chineseStringToByte(String pszStr)
	{
		byte[] data = pszStr.getBytes();
		if(isCode == eno_utf8_encoding)
		{
			try {
				data = pszStr.getBytes(UTF_8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(isCode == eno_unicode_encoding)
		{
			data = ENOUtils.unistr2Bytes(pszStr);
		}
		return data;
	}

	private int ExportSize() {
		//清除一下没用的数据
		ShrinkData();

		int nTotalSize = 0; // 结果集的标注说明

		nTotalSize += 4; // 字段描述块
		nTotalSize += nAllFields_ * TCRSField.sizeof;

		nTotalSize += 4; // 记录数量（4Bytes）
		nTotalSize += recsBuff_.size(); // 记录的固定长度内容 nAllRecords_ *
		// nRecordSize_;

		nTotalSize += 4; // 描述信息块长度（4Bytes）

		// 结果集内容标注
		nTotalSize += 1; // 是否unicode（1Byte）
		if (isUnistringInfo_ == false)
			nTotalSize += szResInfo_.getBytes().length;
		else
		{
			byte[] data = chineseStringToByte(szResInfo_);
			nTotalSize += data.length;
		}


		// 字段文字信息块
		for (int i = 0; i < nAllFields_; i++) {
			// 2010-06-02 修改分隔符号为两个连续的'\0'
			nTotalSize += 2;
			TCRSField pFD = getField(i);
			nTotalSize += pFD.fieldName.getBytes().length;


			if (pFD.FieldDesc.length() > 0) { // Bug修正：增加Unicode编码的判定
				nTotalSize += 1; // '|'
				nTotalSize += 1; // 是否unicode（1Byte）
				if (pFD.isUnistringDesc)
				{
					byte[] data = chineseStringToByte(pFD.FieldDesc);
					nTotalSize += data.length;
				}
				else
					nTotalSize += pFD.FieldDesc.getBytes().length;
			}
		}



		nTotalSize += 4; // 动态内容长度（4Bytes）
		nTotalSize += dataBuff_.size(); // 记录的动态长度内容
		return nTotalSize;
	}

	/**
	 * 导出结果集byte数组
	 *
	 * @return
	 */
	public byte[] getData() {
		return ExportData();
	}

	/**
	 * 导出结果集byte数组
	 *
	 * @return
	 */
	public byte[] ExportData() {
		int nExportSize = ExportSize();

		int offset = 0;
		byte[] pRet = new byte[nExportSize];
		byte[] tmp = null;

		// 字段描述块
		ENOUtils.integer2Bytes(pRet, offset, nAllFields_);
		offset += 4;
		if (nAllFields_ > 0) {
			// 整个字段说明长度
			tmp = new byte[nAllFields_ * TCRSField.sizeof];
			TCRSField tcfield;
			int nPos = 0;
			for (int i = 0; i < nAllFields_; i++) {
				tcfield = fdsBuff_.elementAt(i);
				ENOUtils.integer2Bytes(tmp, nPos, tcfield.fieldType);
				nPos += 4;
				ENOUtils.integer2Bytes(tmp, nPos, tcfield.dataLength);
				nPos += 4;
				ENOUtils.integer2Bytes(tmp, nPos, tcfield.dataOffset);
				nPos += 4;
			}
			System.arraycopy(tmp, 0, pRet, offset, nAllFields_
					* TCRSField.sizeof);
			offset += nAllFields_ * TCRSField.sizeof;
		}

		// 固定长度的记录内容
		ENOUtils.integer2Bytes(pRet, offset, nAllRecords_);
		offset += 4;
		if (nAllRecords_ > 0) {
			int size = recsBuff_.size();
			for (int i = 0; i < size; i++) {
				pRet[offset + i] = recsBuff_.elementAt(i);
			}
			offset += recsBuff_.size();
		}

		// 描述信息块的长度内容
		int pLengthPos = offset;
		offset += 4;
		// 结果集内容标注
		byte[] infoData = szResInfo_.getBytes();
		int nTempLen = infoData.length;
		if (isUnistringInfo_ == false)
			pRet[offset] = '0';
		else {
			infoData = chineseStringToByte(szResInfo_);
			nTempLen = infoData.length;
			pRet[offset] = '1';
		}
		offset++;
		ENOUtils.integer2Bytes(pRet, pLengthPos, nTempLen);
		System.arraycopy(infoData, 0, pRet, offset, nTempLen);
		offset += nTempLen;

		// 字段文字信息块
		for (int j = 0; j < nAllFields_; j++) {
			// //////////////////////////////////////////////////////////////////////////////////
			// 2010-06-02 修改分隔符号为两个连续的'\0'
			pRet[offset] = '\0';
			pRet[offset + 1] = '\0';
			offset += 2;
			// //////////////////////////////////////////////////////////////////////////////////
			TCRSField pFD = getField(j);
			nTempLen = pFD.fieldName.getBytes().length;
			ENOUtils.ascstr2Bytes(pFD.fieldName, pRet, offset, nTempLen);
			offset += nTempLen;

			if (pFD.FieldDesc.length() > 0) { // Bug修正：增加Unicode编码的判定
				pRet[offset] = '|';
				offset++;
				pRet[offset] = (byte) (pFD.isUnistringDesc == false ? '0' : '1');
				offset++;

				if (pFD.isUnistringDesc) {
					byte[] descData = chineseStringToByte( pFD.FieldDesc);
					nTempLen = descData.length;
					System.arraycopy(descData, 0, pRet, offset, nTempLen);
				} else {
					nTempLen = pFD.FieldDesc.getBytes().length;
					ENOUtils
							.ascstr2Bytes(pFD.FieldDesc, pRet, offset, nTempLen);
				}
				offset += nTempLen;
			}
		}

		// 更新描述信息块的长度
		nTempLen = offset - pLengthPos - 4;
		ENOUtils.integer2Bytes(pRet, pLengthPos, nTempLen);

		// 动态长度的记录内容
		int nDynDataSize = dataBuff_.size();
		ENOUtils.integer2Bytes(pRet, offset, nDynDataSize);
		offset += 4;
		if (nDynDataSize > 0) {
			for (int i = 0; i < nDynDataSize; i++) {
				pRet[offset + i] = dataBuff_.elementAt(i);
			}
			offset += dataBuff_.size();
		}

		return pRet;
	}

	/**
	 * 合并结果集
	 *
	 * @param rs 被合并的结果集。字段必须都相同。
	 * @param atTop 放在顶部，否则放在末尾
	 *
	 */
	public boolean mergeTCRS(TCRS rs,boolean atTop)
	{
		// 需要先判断是否具有相同的字段描述内容
		// 这里我们简化判断条件：1）记录长度相同；2）字段数相同；3）第一个字段名称和类型相同
		if (rs == null)
			return false;

		if (rs.nRecordSize_ != nRecordSize_ ||
				rs.nAllFields_ != nAllFields_ ||
				rs.nAllFields_ == 0 ||
				rs.getField(0).fieldType != getField(0).fieldType ||
				!rs.getFieldName(0).equals(this.getFieldName(0)))
			return false;

		if (rs.nAllRecords_ > 0) {
			TCRS pRS1;
			TCRS pRS2;
			if (atTop) {
				pRS1 = rs;
				pRS2 = this;
			}
			else {
				pRS2 = rs;
				pRS1 = this;
			}
			Vector<Byte> tmpResBuf = new Vector<Byte>();// 存放固定长度记录内容的缓冲
			for(int i = 0; i < pRS1.recsBuff_.size();i++)
			{
				tmpResBuf.add(pRS1.recsBuff_.elementAt(i));
			}
			for(int i = 0; i < pRS2.recsBuff_.size();i++)
			{
				tmpResBuf.add(pRS2.recsBuff_.elementAt(i));
			}

			Vector<Byte> tmpDynBuf = new Vector<Byte>();// 存放动态内容的缓冲
			for (int n=0; n<nAllFields_; n++) {
				TCRSField pFDesc = this.getField(n);
				if (pFDesc.fieldType == TCRSField.fdtype_string ||
						pFDesc.fieldType == TCRSField.fdtype_unistring ||
						pFDesc.fieldType == TCRSField.fdtype_array) {
					byte[] p = new byte[4];
					for (int i=0; i< pRS1.nAllRecords_; i++) {
						int pRecData =  i * nRecordSize_ + pFDesc.dataOffset;
						for(int j = 0; i < 4;j++)
						{
							p[j] = tmpResBuf.elementAt(pRecData+j);
						}
						int nOffset = ENOUtils.byteArrayToInt(p, 0);
						for(int j = 0; i < 4;j++)
						{
							p[j] = tmpResBuf.elementAt(pRecData+j + 4);
						}
						int nLength = ENOUtils.byteArrayToInt(p, 0);

						if (nLength > 0) {
							int nNewOff = tmpDynBuf.size();
							for(int j = 0; i < nLength;j++)
							{
								tmpDynBuf.add(pRS1.dataBuff_.elementAt(nOffset+j));
							}
							//把固定长度记录中的位置偏移作相应的修改
							ENOUtils.intToByteArray(nNewOff, p, 0);
							for(int j = 0; i < 4;j++)
							{
								tmpResBuf.set(pRecData+j, p[j]);
							}

							//这里处理双字节对齐模式：如果是单数字节，后面添加一个零字符进行对齐
							if (nLength%2 != 0)
							{
								tmpDynBuf.add((byte)'\0');
							}
						}
					}

					for (int i=0; i<pRS2.nAllRecords_; i++) {
						int pRecData =  (i+pRS1.nAllRecords_) * nRecordSize_ + pFDesc.dataOffset;

						for(int j = 0; i < 4;j++)
						{
							p[j] = tmpResBuf.elementAt(pRecData+j);
						}
						int nOffset = ENOUtils.byteArrayToInt(p, 0);
						for(int j = 0; i < 4;j++)
						{
							p[j] = tmpResBuf.elementAt(pRecData+j + 4);
						}
						int nLength = ENOUtils.byteArrayToInt(p, 0);

						if (nLength > 0) {
							int nNewOff = tmpDynBuf.size();
							for(int j = 0; i < nLength;j++)
							{
								tmpDynBuf.add(pRS2.dataBuff_.elementAt(nOffset+j));
							}
							//把固定长度记录中的位置偏移作相应的修改
							ENOUtils.intToByteArray(nNewOff, p, 0);
							for(int j = 0; i < 4;j++)
							{
								tmpResBuf.set(pRecData+j, p[j]);
							}
							//这里处理双字节对齐模式：如果是单数字节，后面添加一个零字符进行对齐
							if (nLength%2 != 0)
							{
								tmpDynBuf.add((byte)'\0');
							}
						}
					}
				}
			}

			// Ok，更新当前结果集的所有内容
			recsBuff_ = tmpResBuf;
			dataBuff_ = tmpDynBuf;

			nAllRecords_ += rs.nAllRecords_;	// 总记录数 = recsBuff_.size() / nRecodeSize_
			nCurrRecord_ = -1;		    // 当前记录内容指针（BOF状态）
			isDirty_ = false;             // 动态内容缓冲是否存在无用数据（记录已经被删除的情况）
		}
		return true;
	}

	/**
	 * 截取结果集
	 *
	 * @param location 开始位置。
	 * @param len 长度
	 *
	 */
	public TCRS subTCRS(int location , int len)
	{
		int max = location + len;
		if(max > nAllRecords_)
		{
			max = nAllRecords_;
		}
		if(location < max)
		{
			int realLen = max - location;
			TCRS rs = new TCRS();
			rs.setRecordsetInfo(szResInfo_, isUnistringInfo_);
			for(int i = 0 ; i < nAllFields_; i++)
			{
				rs.insertField(getField(i), -1);
			}
			for(int j = 0; j < realLen ; j++)
			{
				moveTo(location + j);
				rs.insertRecord(-1);
				for(int i = 0 ; i < nAllFields_; i++)
				{
					byte[] pData = this.getBytes(i);
					rs.UpdateRecord(i, pData, pData.length, j);
				}
			}
			return rs;
		}
		return null;
	}

	/**
	 * 结果集追加或修改数据(主要用于fs的推送)
	 *
	 * @param m_tcrs
	 * @param s_tcrs
	 *            被合并的结果集的字段必须在都在m_tcrs里有，如果没有的字段会被抛弃。如果m_tcrs里有S_tcrs没有的数据为空。
	 * @param fieldName
	 *            要进行时间的比对字段的名称
	 */
	public static void mergeTCRS(TCRS m_tcrs, TCRS s_tcrs, String fieldName,
								 long maxCJL) {
		TCRS old_tcrs = new TCRS();
		old_tcrs.importData(m_tcrs);
		old_tcrs.moveLast();
		int m_fields = m_tcrs.getFields();
		int s_fields = s_tcrs.getFields();

		// int rows=m_tcrs.getRecords();
		// if(rows>0)
		// {
		// rows=rows-1;
		// }
		//
		// m_tcrs.moveTo(rows);

		m_tcrs.moveLast();
		int m_time = m_tcrs.getShortInt(fieldName);

		s_tcrs.moveLast();
		int s_time = s_tcrs.getShortInt(fieldName);
		// s_time = s_time / 100;

		int differ_time = s_time - m_time;
		int differ_point = 0;

		if (differ_time < 2) {

			for (int m = 0; m < m_fields; m++) {
				String temp1 = m_tcrs.getField(m).fieldName;

				if ("hqtm".equals(temp1)) // 如果是时间没到规定的间隔先不要跟新
				{

					continue;
				}

				if (temp1.equals("avol")) {
					// System.out.println(m_tcrs.getLong("cjsl")+"**********#####*************"+s_tcrs.getLong("zl"));
					long temp = m_tcrs.getLong("cjsl") + s_tcrs.getLong("zl");

					// System.out.println(temp+"**********avol1*************"+maxCJL);
					if (maxCJL < temp) {
						maxCJL = temp;
					}
					if (maxCJL != 0) {
						m_tcrs.UpdateRecord(m, (temp * 1000) / maxCJL);
					}
				}

				for (int s = 0; s < s_fields; s++) {
					String temp2 = s_tcrs.getField(s).fieldName;

					if (temp1.equals(temp2)) {
						// System.out.println("(^"
						// + m_tcrs.getField(m).fieldType);
						// System.out.println("^^" + m_tcrs.toString(m) +
						// "_____"
						// + m + "_____" + s_tcrs.toString(s));

						switch (s_tcrs.getField(s).fieldType) {
							case TCRSField.fdtype_bool:
							case TCRSField.fdtype_int1:
							case TCRSField.fdtype_int4:

								m_tcrs.UpdateRecord(m, s_tcrs.getInt(s));// s_tcrs.getInt(s));//s_tcrs.getInt(s));

								break;
							case TCRSField.fdtype_int2:

								m_tcrs.UpdateRecord(m, s_tcrs.getShortInt(s));

								break;
							case TCRSField.fdtype_enofloat:

								m_tcrs.UpdateRecord(m, s_tcrs.getInt(s));
								break;
							case TCRSField.fdtype_real4:// float

								m_tcrs.UpdateRecord(m, s_tcrs.getFloat(s));
								break;
							case TCRSField.fdtype_real8:// double

								m_tcrs.UpdateRecord(m, s_tcrs.getDouble(s));
								break;
							case TCRSField.fdtype_enomoney:

								m_tcrs.UpdateRecord(m, s_tcrs.getInt(s));
								break;
							case TCRSField.fdtype_int8:
								if (temp1.equals("cjje")) {
									m_tcrs.UpdateRecord(m, m_tcrs.getLong("cjje")
											+ s_tcrs.getLong("ze"));
								} else if (temp1.equals("cjsl")) {
									m_tcrs.UpdateRecord(m, m_tcrs.getLong("cjsl")
											+ s_tcrs.getLong("zl"));
								} else {
									m_tcrs.UpdateRecord(m, s_tcrs.getLong(s));
								}

								break;
							case TCRSField.fdtype_unistring:
							case TCRSField.fdtype_array:
							case TCRSField.fdtype_string:
								m_tcrs.UpdateRecord(m, s_tcrs.toString(s));// s_tcrs.toString(s));
								break;
						}
					}
				}
			}
			// 新增记录
		} else {

			differ_point = differ_time / 2;// 计算间隔的点数
			differ_point = differ_point > 120 ? 120 : differ_point;

			for (int i = 0; i < differ_point; i++) {
				m_tcrs.insertRecord(-1);
				m_tcrs.moveLast();

				for (int m = 0; m < m_fields; m++) {
					String temp1 = m_tcrs.getField(m).fieldName;
					switch (m_tcrs.getField(m).fieldType) {
						case TCRSField.fdtype_bool:
						case TCRSField.fdtype_int1:
						case TCRSField.fdtype_int4:
							if ("hqtm".equals(temp1)) {

								int nTime = old_tcrs.getInt(m);
								nTime = ENOUtils.Time4Min(nTime) + 2 * i;
								nTime = ENOUtils.Min4Time(nTime);

								m_tcrs.UpdateRecord(m, nTime);

							} else {
								m_tcrs.UpdateRecord(m, old_tcrs.getInt(m));// s_tcrs.getInt(s));//s_tcrs.getInt(s));
							}
							break;
						case TCRSField.fdtype_int2:
							m_tcrs.UpdateRecord(m, old_tcrs.getShortInt(m));
							break;
						case TCRSField.fdtype_enofloat:
							m_tcrs.UpdateRecord(m, old_tcrs.getInt(m));
							break;
						case TCRSField.fdtype_real4:// float
							m_tcrs.UpdateRecord(m, old_tcrs.getFloat(m));
							break;
						case TCRSField.fdtype_real8:// double
							m_tcrs.UpdateRecord(m, old_tcrs.getDouble(m));
							break;
						case TCRSField.fdtype_enomoney:
							m_tcrs.UpdateRecord(m, old_tcrs.getInt(m));
							break;
						case TCRSField.fdtype_int8:
							m_tcrs.UpdateRecord(m, old_tcrs.getLong(m));
							break;
						case TCRSField.fdtype_unistring:
						case TCRSField.fdtype_array:
						case TCRSField.fdtype_string:
							m_tcrs.UpdateRecord(m, old_tcrs.toString(m));// s_tcrs.toString(s));
							break;
					}

				}
			}
			m_tcrs.moveLast();
			for (int m = 0; m < m_fields; m++) {
				String temp1 = m_tcrs.getField(m).fieldName;
				if (temp1.equals("avol")) {
					long temp = m_tcrs.getLong("cjsl");// +s_tcrs.getLong("zl");

					// System.out.println(temp+"**********avol2*************"+maxCJL);
					if (maxCJL < temp) {
						maxCJL = temp;
					}
					m_tcrs.UpdateRecord(m, (temp * 1000) / maxCJL);
					// continue;
				}
				for (int s = 0; s < s_fields; s++) {

					String temp2 = s_tcrs.getField(s).fieldName;

					if (temp1.equals(temp2)) {

						switch (s_tcrs.getField(s).fieldType) {
							case TCRSField.fdtype_bool:
							case TCRSField.fdtype_int1:
							case TCRSField.fdtype_int4:

								if ("hqtm".equals(temp1)) {
									// m_tcrs.UpdateRecord(m, s_tcrs.getInt(s) /
									// 100);//不知道为什么除以100
									m_tcrs.UpdateRecord(m, s_tcrs.getInt(s));
								} else {
									m_tcrs.UpdateRecord(m, s_tcrs.getInt(s));
								}

								break;
							case TCRSField.fdtype_int2:
								//
								m_tcrs.UpdateRecord(m, s_tcrs.getShortInt(s));
								break;
							case TCRSField.fdtype_enofloat:

								m_tcrs.UpdateRecord(m, s_tcrs.getInt(s));
								break;
							case TCRSField.fdtype_real4:// float

								m_tcrs.UpdateRecord(m, s_tcrs.getFloat(s));
								break;
							case TCRSField.fdtype_real8:// double

								m_tcrs.UpdateRecord(m, s_tcrs.getDouble(s));
								break;
							case TCRSField.fdtype_enomoney:

								m_tcrs.UpdateRecord(m, s_tcrs.getInt(s));
								break;
							case TCRSField.fdtype_int8:
								// 分时线的成交量是每笔的所有需要做累加
								if (temp1.equals("cjje")) {
									// m_tcrs.UpdateRecord(m,m_tcrs.getLong("cjje")+s_tcrs.getLong("ze"));
									m_tcrs.UpdateRecord(m, s_tcrs.getLong("ze"));
								} else if (temp1.equals("cjsl")) {

									// m_tcrs.UpdateRecord(m,
									// m_tcrs.getLong("cjsl")+s_tcrs.getLong("zl"));
									m_tcrs.UpdateRecord(m, s_tcrs.getLong("zl"));
								} else {
									m_tcrs.UpdateRecord(m, s_tcrs.getLong(s));
								}
								break;
							case TCRSField.fdtype_unistring:
							case TCRSField.fdtype_array:
							case TCRSField.fdtype_string:
								m_tcrs.UpdateRecord(m, s_tcrs.toString(s));// s_tcrs.toString(s));
								break;
						}
					}
				}
			}
		}

	}

	/**
	 * 合并两个结果集
	 *
	 * @param m_tcrs
	 * @param s_tcrs
	 * @param type
	 * @return
	 */
	public static boolean mergeTCRS(TCRS m_tcrs, TCRS s_tcrs, int type) {
		boolean isSuccess = true;

		int m_fields = m_tcrs.getFields();
		// int s_fields = s_tcrs.getFields();

		int s_row = s_tcrs.getRecords();

		for (int i = 0; i < s_row; i++) {
			m_tcrs.insertRecord(-1);
			m_tcrs.moveLast();
			s_tcrs.moveTo(i);
			for (int m = 0; m < m_fields; m++) {
				// String temp1 = m_tcrs.getField(m).fieldName;
				switch (m_tcrs.getField(m).fieldType) {
					case TCRSField.fdtype_bool:
					case TCRSField.fdtype_int1:
					case TCRSField.fdtype_int4:
						m_tcrs.UpdateRecord(m, s_tcrs.getInt(m));
						break;
					case TCRSField.fdtype_int2:
						m_tcrs.UpdateRecord(m, s_tcrs.getShortInt(m));
						break;
					case TCRSField.fdtype_enofloat:
						m_tcrs.UpdateRecord(m, s_tcrs.getInt(m));
						break;
					case TCRSField.fdtype_real4:// float
						m_tcrs.UpdateRecord(m, s_tcrs.getFloat(m));
						break;
					case TCRSField.fdtype_real8:// double
						m_tcrs.UpdateRecord(m, s_tcrs.getDouble(m));
						break;
					case TCRSField.fdtype_enomoney:
						m_tcrs.UpdateRecord(m, s_tcrs.getInt(m));
						break;
					case TCRSField.fdtype_int8:
						m_tcrs.UpdateRecord(m, s_tcrs.getLong(m));
						break;
					case TCRSField.fdtype_unistring:
					case TCRSField.fdtype_array:
					case TCRSField.fdtype_string:
						m_tcrs.UpdateRecord(m, s_tcrs.toString(m));
						break;
				}

			}
		}

		return isSuccess;
	}

	/**
	 * 用于从一个结果集修改本结果集的对应字段的记录（主要用于推送）
	 *
	 * @param m_tcrs
	 *            主结果集
	 * @param s_tcrs
	 *            比对的结果集
	 * @param fieldName
	 *            要比对的标识列名 （比如股票代码列 code）
	 * @return 返回比对替换后的结果集
	 */
	public static int[][] updateTCRS2(TCRS m_tcrs, TCRS s_tcrs, String fieldName) {
		TCRS backTcrs = new TCRS();
		backTcrs.importData(m_tcrs);// 复制出一个结果集以免出错时是回滚
		boolean isError = true;
		int dataLocation[][] = null;

		// try {
		int m_fields = m_tcrs.getFields();
		int s_fields = s_tcrs.getFields();
		// System.out.println("------update----run"+m_fields+"  "+s_fields);
		m_tcrs.moveFirst();
		s_tcrs.moveFirst();
		dataLocation = new int[s_tcrs.getRecords()][2];
		int m_row = 0;
		int s_row = 0;
		while (!m_tcrs.IsEof()) {
			s_tcrs.moveFirst();// 循环一遍后必须把游标调到第一才能重新遍历
			while (!s_tcrs.IsEof()) {

				if (fieldName == null
						| m_tcrs.toString(fieldName).equals(
						s_tcrs.toString(fieldName))) {
					for (int m = 0; m < m_fields; m++) {
						for (int s = 0; s < s_fields; s++) {
							// System.out.println(m+"G  "+m_tcrs.getFieldName(m)+"---"+s_tcrs.getFieldName(s)+" "+s);
							String temp1 = m_tcrs.getField(m).fieldName;
							String temp2 = s_tcrs.getField(s).fieldName;
							// System.out.println(temp1 + " " + temp2);
							if (temp1.equals(fieldName)) {
								dataLocation[0][0] = m_row;
							} else {

								if (temp1.equals(temp2)) {
									dataLocation[0][1] = m;
									// System.out
									// .println(s_tcrs.getField(s).fieldType);
									// System.out.println(m_tcrs.toString(m) +
									// "_____"
									// + s + "_____" + s_tcrs.getInt(s));
									switch (s_tcrs.getField(s).fieldType) {
										case TCRSField.fdtype_bool:
										case TCRSField.fdtype_int1:
										case TCRSField.fdtype_int4:
											m_tcrs
													.UpdateRecord(m, s_tcrs
															.getInt(s));// s_tcrs.getInt(s));//s_tcrs.getInt(s));
											break;
										case TCRSField.fdtype_int2:
											m_tcrs.UpdateRecord(m, s_tcrs
													.getShortInt(s));
											break;
										case TCRSField.fdtype_enofloat:
											m_tcrs
													.UpdateRecord(m, s_tcrs
															.getInt(s));
											break;
										case TCRSField.fdtype_real4:// float
											m_tcrs.UpdateRecord(m, s_tcrs
													.getFloat(s));
											break;
										case TCRSField.fdtype_real8:// double
											m_tcrs.UpdateRecord(m, s_tcrs
													.getDouble(s));
											break;
										case TCRSField.fdtype_enomoney:
											m_tcrs
													.UpdateRecord(m, s_tcrs
															.getInt(s));
											break;
										case TCRSField.fdtype_int8:
											m_tcrs.UpdateRecord(m, s_tcrs
													.getLong(s));
											break;
										case TCRSField.fdtype_unistring:
										case TCRSField.fdtype_array:
										case TCRSField.fdtype_string:
											m_tcrs.UpdateRecord(m, s_tcrs
													.toString(s));// s_tcrs.toString(s));
											break;
									}

								}
							}
						}
					}
				}
				s_row++;
				s_tcrs.moveNext();
			}
			m_row++;
			m_tcrs.moveNext();
		}

		// } catch (Exception e) {
		// isError=false;
		// m_tcrs=backTcrs;
		// }
		return dataLocation;
	}

	public static void main(String[] args) {
		TCRS rs = new TCRS(10, "test", false);
		TCRS rs2 = new TCRS(20, "中国", true);
		byte[] a1 = rs.ExportData();
		byte[] a2 = rs2.ExportData();
		byte[] bts = new byte[a1.length + a2.length];
		System.arraycopy(a1, 0, bts, 0, a1.length);
		System.arraycopy(a2, 0, bts, a1.length, a2.length);

		TCRS[] mrs = TCRS.buildMRS(bts, 0);

		// TCRS.updateTCRS(rs, rs2, "");

		System.out.println("**********************" + mrs.length);
	}



	/**
	 *
	 * @param bytes
	 * @param offset
	 * @param len
	 * @return
	 */
	private String bytesString(byte[] bytes, int offset, int len)
	{

		String str=null;
		if(isCode==eno_unicode_encoding)
		{
			str= ENOUtils.bytes2Unistr(bytes,offset, len);
		}
		else if(isCode==eno_utf8_encoding)
		{
			int myLen = ENOUtils.bytes2AscstrLen(bytes, offset) -1;
			if(myLen > len) myLen = len;
			try {
				str= new String(bytes, offset, myLen, UTF_8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			int myLen = ENOUtils.bytes2AscstrLen(bytes, offset) - 1;
			if(myLen > len) myLen = len;
			try {
				str= new String(bytes, offset, myLen, GBK);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 *
	 * @param bytes
	 * @return
	 */
	private String bytesString(byte[] bytes)
	{

		String str=null;
		if(isCode==eno_unicode_encoding)
		{
			str= ENOUtils.bytes2Unistr(bytes);
		}else
		{
			int len = ENOUtils.bytes2AscstrLen(bytes, 0) - 1;
			try {
				str= new String(bytes,0,len, UTF_8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}
}