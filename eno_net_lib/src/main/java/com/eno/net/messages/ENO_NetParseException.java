package com.eno.net.messages;

/**
 *	解析数据包产生异常时抛出。
 * <p>
 * 创建时间：2009-10-28 下午02:42:16
 * @author xmf
 * @since 1.0
 */
public class ENO_NetParseException extends Exception {

	private static final long serialVersionUID = 1L;
	public ENO_NetParseException() {
	}

	public ENO_NetParseException(String message) {
		super(message);
	}

	public ENO_NetParseException(Throwable cause) {
		super(cause);
	}

	public ENO_NetParseException(String message, Throwable cause) {
		super(message, cause);
	}

}