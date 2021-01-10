package com.example.humanbenchmark.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class GetData {
    static String sessionID = null;
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000;//超时时间
    private static final String CHARSET = "utf-8";//设置编码

    public static String getFormbodyPostData(String url, HashMap<String, String> paramsMap, boolean first) {
        try {
            URL myUrl = new URL(url);

            //connect
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

            //setting
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
//            connection.setRequestMethod("POST");
            if (sessionID != null) {
                connection.setRequestProperty("cookie", sessionID);
            }

            connection.setDoOutput(true);
            connection.setUseCaches(false);
            //begin connet
            connection.connect();

            StringBuffer params = new StringBuffer();

            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    params.append("&");
                }
                Log.e("KEY", "Key: " + key + " " + paramsMap.get(key));
                params.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
            outputStreamWriter.write(params.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            if (first) {
                //get set-cookie
                String cookieval = connection.getHeaderField("set-cookie");
                if (cookieval != null) {
                    //get sessionId
                    sessionID = cookieval.substring(0, cookieval.indexOf(";"));
                }
            }

            int resultCode = connection.getResponseCode();
            Log.e("GetData", "getFormbodyPostData: rescode = " + resultCode);
            if (resultCode == HttpURLConnection.HTTP_OK) {
                //Success
                StringBuffer buffer = new StringBuffer();
                String line;
                //Get the response
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((line = responseReader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                responseReader.close();
                String result = buffer.toString();
                System.out.print("Get response : " + result);
                System.out.print("Get response : " + buffer.toString());

                return result;
            } else {
                System.out.println("Nothing");
            }
        } catch (Exception e) {
            Log.e("GetData", "getFormbodyPostData: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }


    public static String getFormbodyPostData(String url, HashMap<String, String> paramsMap) {
        return getFormbodyPostData(url, paramsMap, false);
    }

}
