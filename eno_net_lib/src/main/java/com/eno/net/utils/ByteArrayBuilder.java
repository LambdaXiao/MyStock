package com.eno.net.utils;


/**
 *	字节流缓冲区，辅助完成对象向字节流的转换。
 * <p>
 * 创建时间：
 * @author xmf
 * @since 1.0
 */
public class ByteArrayBuilder {
	private int count=0;
	private byte [] buf = null;
	public ByteArrayBuilder(){
		buf = new byte[8];
	}
	public ByteArrayBuilder(int capacity){
		buf = new byte[capacity];
	}
	
	void expandCapacity(int minimumCapacity) {
		int newCapacity = (buf.length + 1) * 2;
		if (newCapacity < 0) {
			newCapacity = Integer.MAX_VALUE;
		} else if (minimumCapacity > newCapacity) {
			newCapacity = minimumCapacity;
		}	
		byte newBuff[] = new byte[newCapacity];
		System.arraycopy(buf, 0, newBuff, 0, count);
		buf = newBuff;
	}
	
	public ByteArrayBuilder write(int i) {
		int newcount = count + 4;
		if (newcount > buf.length) {
			expandCapacity(newcount);
		}
		buf[count+3]=(byte)(i&0x000000FF);
		buf[count+2]=(byte)((i&0x0000FF00)>>8);
		buf[count+1]=(byte)((i&0x00FF0000)>>16);
		buf[count] = (byte)((i&0xFF000000)>>24);
		count = newcount;
		return this;
	}
	
	public ByteArrayBuilder write(byte [] b, int off, int len) {
		if ((off < 0) || (off > b.length) || (len < 0) ||
				((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return this;
		}
		int newcount = count + len;
		if (newcount > buf.length) {
			expandCapacity(newcount);
		}
		System.arraycopy(b, off, buf, count, len);
		count = newcount;
		return this;
	}
	
	public ByteArrayBuilder write(byte [] b){
		return write(b,0,b.length);
	}
	
	public byte [] toBytes(){
		if(count==buf.length) return buf;
		byte newbuff [] = new byte[count];
		System.arraycopy(buf, 0, newbuff, 0, count);
		return newbuff;
	}
}