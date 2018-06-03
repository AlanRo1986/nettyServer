package com.lanxinbase;


import java.io.*;
import java.net.Socket;

public class WorkRun implements Runnable {

    private Long intval;
    private String imei;
//    private String msg = "<{\"imei\":\"%s\",\"index\":0,\"cmd\":\"RcdData\",\"params\":{\"ccid\":\"CC8000075\",\"state\":true,\"bvol\":24.33,\"mvol\":12.3,\"len\":12,\"ti\":5,\"vavg\":12.1,\"vmax\":13.5,\"vmin\":12.0,\"savg\":30.2,\"smax\":60.3,\"smin\":1.2,\"st\":1402512331,\"datas\":[ {\"g\":106.2123,\"a\":31.20315,\"s\":12.2,\"v\":12.5},{\"g\":106.21623,\"a\":31.20415,\"s\":16.2,\"v\":12.0},{\"g\":106.21263,\"a\":31.20015,\"s\":22.2,\"v\":12.7},{\"g\":106.21823,\"a\":31.26315,\"s\":32.2,\"v\":12.2}]}}>";
    private String msg = "imei:%s";
    private Socket socket;

    public WorkRun(String i,Long sleep){
        this.imei = i;
        this.intval = sleep;
        this.msg = String.format(msg,imei);
        this.connect();
    }

    public static WorkRun newInstance(String i){
        return new WorkRun(i,5000L);
    }

    @Override
    public void run() {
        try {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String readline;
                char[] chars = new char[1024];
                int len = 0;
               // readline = ;
                while (true){
                    chars = new char[1024];
                    this.connect();

                    writer.write(msg+'\n');
                    writer.flush();

                   // reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //System.out.println(DateUtil.getFullDateTime(null) +" -- Sever msg:("+this.imei+")"+reader.read());
                    Thread.sleep(intval);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void connect(){
        if(socket==null || (socket!=null && socket.isClosed())){
            try {
                socket = new Socket("127.0.0.1",8888);
                socket.setKeepAlive(true);
                System.out.println("客户端连接成功");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Long getIntval() {
        return intval;
    }

    public void setIntval(Long intval) {
        this.intval = intval;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
