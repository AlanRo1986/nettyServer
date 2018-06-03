package com.lanxinbase.system.provider.handler;

import com.lanxinbase.system.provider.basic.HttpRequestData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan.luo on 2017/7/27.
 */
public abstract class HttpConnector {

    public static final String GET = "GET";   //读
    public static final String POST = "POST"; //创建
    public static final String PUT = "PUT";  //更新
    public static final String DELETE = "DELETE"; //删除

    public static final String RESPONSE_CODE = "responseCode";
    public static final String RESPONSE_ERROR = "responseError";
    public static final String RESPONSE_DATA = "responseData";

    public static final int RESPONSE_SUCCESS = 1;
    public static final int RESPONSE_FAILED = 0;

    private static final int TIME_OUT = 10000;
    private static final String VERSION = "1.1";

    protected static final String TAG = "HTTP CONNECTOR";
    //protected static Logger logger = Logger.getLogger(HttpConnector.class.getName());

    public HttpConnector(){
        super();
    }

    protected final Map<String, Object> curl(String uri, HttpRequestData data){

        Map<String,Object> response = new HashMap<>();

        InputStreamReader in = null;
        BufferedReader buff = null;
        HttpURLConnection conn = null;

        response.put(RESPONSE_ERROR,"网络繁忙!");
        response.put(RESPONSE_CODE,RESPONSE_FAILED);
        response.put(RESPONSE_DATA,null);

        try {
            if(!checkParam(uri,data.getMethod())){
                throw new Exception("Request data isn't standard.");
            }
            System.out.println("uri:"+uri);

            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();

            setCookies(data.getCookie(),conn);

            conn.setUseCaches(false);
            if(!data.getMethod().equals(GET)){
                conn.setDoInput(true);
                conn.setDoOutput(true);
            }

            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setRequestMethod(data.getMethod());
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection","Keep-Alive");
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Cache-Control","no-cache");
            conn.setRequestProperty("X-Powered-By",VERSION);

            //header
            if (data.getHeader() != null && !data.getHeader().isEmpty()){
                for (String key:data.getHeader().keySet()){
                    conn.setRequestProperty(key, data.getHeader().get(key));
                }
            }

            String param = getParams(data.getParam());
            if(param != null && !param.trim().equals("")){
                PrintWriter pw = new PrintWriter(conn.getOutputStream());
                pw.print(param);
                pw.flush();
            }

            if (data.getBody() != null && data.getBody().length() > 1){
                byte[] outputInBytes = data.getBody().getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write( outputInBytes );
                os.close();
            }

            conn.connect();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                in = new InputStreamReader(conn.getInputStream(),"utf-8");
                buff = new BufferedReader(in);

                String str = "";
                String res = "";
                while ((str = buff.readLine()) != null){
                    res += str;
                }
                response.put(RESPONSE_ERROR,"ok");
                response.put(RESPONSE_CODE,RESPONSE_SUCCESS);
                response.put(RESPONSE_DATA, res);

            }else {
                throw new RuntimeException("HTTP ERROR CODE: "+conn.getResponseCode()+" "+conn.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put(RESPONSE_ERROR, e.getMessage());
        }finally {
            try {

                if(buff != null){
                    buff.close();
                }
                if(in != null){
                    in.close();
                }
                if(conn != null){
                    conn.disconnect();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return response;
    }

    /**
     * process the request params.
     * @param param
     * @return String like a=1&b=2
     */
    protected static String getParams(Map<String, Object> param) {
        String res = "";
        if (param != null && !param.isEmpty()){
            for (String key:param.keySet()){
                res += key + "=" + param.get(key)+"&";
            }
            res = res.substring(0,res.length()-1);
        }
        return res;
    }

    /**
     * SET COOKIES
     * @param cookies
     * @param conn
     */
    protected static final void setCookies(Map<String,Object> cookies, URLConnection conn){
        if (cookies != null && !cookies.isEmpty()){
            for (String key:cookies.keySet()){
                conn.addRequestProperty(key, String.valueOf(cookies.get(key)));
            }
        }
    }


    /**
     * 检查参数是否正确
     * @param url
     * @param method
     * @return
     */
    protected static final boolean checkParam(String url,String method){
        if (url.length() < 10){
            return false;
        }

        return  method == POST || method == GET || method == PUT || method == DELETE;
    }



}
