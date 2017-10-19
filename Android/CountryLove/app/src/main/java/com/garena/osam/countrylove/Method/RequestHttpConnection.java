package com.garena.osam.countrylove.Method;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017-10-17.
 */

public class RequestHttpConnection {
    public String request(String _url, ContentValues params) {
        HttpURLConnection urlConnection = null;
        StringBuffer stringBufferParams = new StringBuffer();

        if(params == null) {
            stringBufferParams.append("");
        } else {
            // 보낼 데이터가 있을 때
        }

        try {
            URL url = new URL(_url);

            urlConnection = (HttpURLConnection) url.openConnection();

            if(urlConnection != null) {
                urlConnection.connect();
            }

            urlConnection.setRequestMethod("POST");
           /* urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

            String strParams = stringBufferParams.toString();
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(strParams.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close(); */

            if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("서버와 연결을 실패했습니다.");
                return null;
            } else {
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String line = "";
                String page = "";

                while((line = reader.readLine()) != null) {
                    page += line;
                }

                inputStream.close();
                reader.close();
                return page;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
