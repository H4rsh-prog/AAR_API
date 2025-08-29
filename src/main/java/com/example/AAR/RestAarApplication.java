package com.example.AAR;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.RequestEntity;

import com.sun.net.httpserver.Headers;


@SpringBootApplication
public class RestAarApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAarApplication.class, args);
		try {
			CloseableHttpClient chc = HttpClients.createDefault();
			String url = "http://localhost:8080/aa";
		    String rawData = "{ \"url\": \"https://images.pexels.com/photos/32878855/pexels-photo-32878855.jpeg\" }";
		    HttpPost hp = new HttpPost(url);
		    hp.addHeader("Content-Type", "application/json");
		    hp.setEntity(new StringEntity(rawData));

			try(CloseableHttpResponse response = chc.execute(hp)){
				Object e =  new JSONParser((EntityUtils.toString(response.getEntity()))).object().get("aa");
				System.out.println(e);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (org.apache.tomcat.util.json.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
