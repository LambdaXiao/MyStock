package com.android.mystock.net.enoservice;

import android.os.Bundle;

import com.eno.base.utils.TCRS;

/**
 * 发生请求消息接口对象
 */
public abstract class NetClientMessage implements NetClientMessageible {
    private int packageID = 0;

    public int getPackageID() {
        return packageID;
    }

    /**
     * 设置包id
     *
     * @param packageID 包id
     */
    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    /**
     * 实例化对象
     *
     * @param packageID 包id
     */
    public NetClientMessage(int packageID) {
        this.packageID = packageID;
    }

    /**
     * 发送请求
     *
     * @return
     */
    @Override
    public abstract Bundle sendRequest();

    /**
     * 监听返回的请求
     *
     * @param tcrs   eno 结果集
     * @param bundle
     * @return
     */
    @Override
    public abstract boolean netParse(TCRS[] tcrs, Bundle bundle);

    /**
     * 发生请求错误
     *
     * @param msg 错误消息
     * @return
     */
    @Override
    public abstract boolean netError(String msg);


    /**
     * 不管请求是否发生成功都会调用此方法
     */
    @Override
    public abstract void netNotifier();


    public abstract interface I_ENOBodyParser {
        /**
         * 解析具体的请求的数据
         *
         * @return 创建完成的数据内容
         */
        abstract void parseRequestData(int index, byte[] data, boolean bError);
    }

}