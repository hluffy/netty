package com.dkvan.netty.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("server channelRead..");
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        
//        StringBuilder stringBuilder = new StringBuilder("");   
//        for (int i = 0; i < req.length; i++) {   
//            int v = req[i] & 0xFF;   
//            String hv = Integer.toHexString(v);   
//            if (hv.length() < 2) {   
//                stringBuilder.append(0);   
//            }   
//            stringBuilder.append(hv);   
//        }  
//        System.out.println(stringBuilder.toString());
        
        List<String> strs = new ArrayList<String>();
        for(int i = 0;i<req.length;i++){
        	int v = req[i] & 0xFF;
        	String hv = Integer.toHexString(v).toUpperCase();
        	if(hv.length() < 2){
        		strs.add("0"+hv);
        	}
        	else{
        		strs.add(hv);
        	}
        }
        String[] datas = (String[])strs.toArray(new String[strs.size()]);
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i<datas.length;i++){
        	sb.append(datas[i].toUpperCase());
        }
        System.out.println(sb.toString());
        
       String hexString =  StringAnalysis.stringAnalysis(datas);
       System.out.println(hexString);
        
        
//        00  21  41  4c  54  5f  4c  4f  52  41  30  30  31
//        String hexString = "7B0021414c545f4c4f52413030318500840901017D";
//        String hexString = "7B0033414C545F4C4F52413030318100030901017D";
        hexString = hexString.toUpperCase();   
        int length = hexString.length() / 2;   
        char[] hexChars = hexString.toCharArray();   
        byte[] d = new byte[length];   
        for (int i = 0; i < length; i++) {   
            int pos = i * 2;   
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
        } 
        ByteBuf resp = Unpooled.copiedBuffer(d);
        ctx.writeAndFlush(resp);
//        String body = new String(req, "UTF-8");
//        if(body.length()>1){
//        	body = body.substring(0, body.length()-1);
//        }
//        System.out.println("The time server receive order:" + body);
//        System.out.println("body:"+body);
//        String theOrder = "QUERY TIME ORDER";
//        System.out.println("The time server the     Order:" + theOrder+"-");
//        String ret = theOrder.equalsIgnoreCase(body) ? new Date(
//                System.currentTimeMillis()).toString() : "BAD ORDER" + body+"-";
//        String ret = body;
//        ByteBuf resp = Unpooled.copiedBuffer(ret.getBytes());
//        ctx.writeAndFlush(resp);
    }
    
    private byte charToByte(char c) {   
        return (byte) "0123456789ABCDEF".indexOf(c);   
    } 

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channelReadComplete..");
        ctx.flush();//刷新后才将数据发出到SocketChannel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("server exceptionCaught..");
        ctx.close();
    }

}