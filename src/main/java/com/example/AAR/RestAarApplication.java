package com.example.AAR;

import java.io.IOException;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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
