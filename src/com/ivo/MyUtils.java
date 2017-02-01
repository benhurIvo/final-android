package com.ivo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class MyUtils {
	static String data = "";
	static String sendurl(String target, String mtd, String input){
	try {
		    
	    System.out.println("util "+target); 

			URL targetUrl = new URL(target);

			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod(mtd);
			
			if(mtd.equals("POST")||mtd.equals("PUT")){
			httpConnection.setRequestProperty("Content-Type", "application/json");
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			}
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ httpConnection.getResponseCode());
			}

			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
					(httpConnection.getInputStream())));

			String output="";
			System.out.println("Output from Server:\n");
			
			while ((output = responseBuffer.readLine()) != null) {
				data = output;
			}
	    System.out.println("data "+data);
			httpConnection.disconnect();
			

		  } catch (MalformedURLException e) {
	    System.out.println("mal");
			e.printStackTrace();

		  } catch (IOException e) {
		      System.out.println("io");
			e.printStackTrace();

		 }
	return data;
	}
	
	  public String makeGetRequest(String urli) {
       System.out.println("makeGetRequest");
       data="";
   try {
       
       URL url = new URL(urli);
       HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
       try {
	  urlConnection = (HttpURLConnection) url
                .openConnection();

        InputStream in = urlConnection.getInputStream();

        InputStreamReader isw = new InputStreamReader(in);

        int dat = isw.read();
	     while (dat != -1) {
            char current = (char) dat;
            dat = isw.read();
            data+=current;
        }
       } finally {
	   urlConnection.disconnect();
       }
       
       
   }	    catch (Exception ex) {
		Logger.getLogger(MyUtils.class.getName()).log(Level.SEVERE, null, ex);
	    }

 return data;
    }

 
}
