package com.ivo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyUtils {
	static String data = "";
	static String sendurl(String target, String mtd, String input){
	try {
		    

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

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }
	return data;
	}
}
