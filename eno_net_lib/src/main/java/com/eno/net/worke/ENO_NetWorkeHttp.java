package com.eno.net.worke;

import com.eno.net.channel.ENO_Channel;
import com.eno.net.codec.ENO_Decoder;
import com.eno.net.codec.ENO_Encoder;
import com.eno.net.enotc.ENO_HttpRequestEncoder;
import com.eno.net.enotc.ENO_HttpResponseDecoder;
import com.eno.net.messages.ENO_NetMessage;
import com.eno.net.messages.ENO_NetMessageHttp;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @author xmf
 * @version 1.0
 * @see 2013-7-18 下午5:19:51 包名com.eno.net.worke 工程名称EnoNet_lxzq
 */
public class ENO_NetWorkeHttp extends ENO_NetWorke {
    //private Object lock = new Object();

    /**
     * 工作线程
     *
     * @author xmf
     * @version 1.0
     * @see 2013-7-18 下午5:22:03 包名com.eno.net.worke 工程名称EnoNet_lxzq ENO_NetWorkeHttp
     */
    class NetThreadWorke implements Runnable {

        private ENO_NetMessageHttp message;
        private ENO_Channel channel;
        private ENO_Encoder encoder;// 发送请求编码器
        private ENO_Decoder decoder;// 接收数据解码器

        public NetThreadWorke(ENO_NetMessageHttp message, ENO_Channel channel, ENO_Encoder encoder, ENO_Decoder decoder) {

            this.message = message;
            this.channel = channel;
            this.encoder = encoder;
            this.decoder = decoder;
        }

        @Override
        public void run() {

            synchronized (channel) {
                if (!channel.isConnect()) {
                    // channel.close();
                    channel.connect();
                }
                byte[] bytes = null;

                try {

                    bytes = encoder.encode(message.netSend());
                } catch (Exception e) {
                    e.printStackTrace();
                    message.timeOutNotify();
                    message.netNotifier();
                }

                try {
                    channel.sendRequest(bytes);


                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    message.timeOutNotify();
                    message.netNotifier();

                } catch (SocketException e) {
                    e.printStackTrace();
                    message.netError(0, "网络请求失败，请稍后重试！");
                    message.netNotifier();


                } catch (Exception e) {

                    e.printStackTrace();
                    message.netError(0, "网络请求失败，请稍后重试！");
                    message.netNotifier();

                }


                byte[] decodeBytes = null;
                try {

                    bytes = decoder.decode(ENO_NetWorkeHttp.this, channel);

                } catch (IOException e) {
                    //e.printStackTrace();
                    //Logger.v("监听关闭");
                    message.netError(0, "数据解码异常！");
                    channel.close();

                } catch (Exception e) {
                    message.netError(0, "数据解码异常，请稍后重试！");
                    e.printStackTrace();

                }

                if (bytes != null) {

                    message.netParse(encoder, decoder, bytes);
                }

                message.netNotifier();

            }
        }
    }


    public void sendRequest(ENO_NetMessage message, ENO_Channel channel) {

        // procThread = new Thread(threadWorke);
        // procThread.setPriority(Thread.MAX_PRIORITY);
        // procThread.start();

        if (channel != null) {
            ENO_NetThreadPool.getInstance().execute(new NetThreadWorke((ENO_NetMessageHttp) message, channel, new ENO_HttpRequestEncoder(), new ENO_HttpResponseDecoder()));
            enoChannel = channel;
        } else//如果channel为空我们就运行同步机制请求
        {

            ENO_NetThreadPool.getInstance().execute(new NetThreadWorke((ENO_NetMessageHttp) message, enoChannel, encoder, decoder));

        }
    }


    @Override
    public void setParseResponse(ENO_NetMessage message) {
        // TODO Auto-generated method stub

    }

}