package com.example.AAR;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class myController {
	@Autowired
	Repo repo;
	AAR aar;
	File f = new File("temp.temp");

	@GetMapping("/aa")
	public ResponseEntity<List<AA>> getAAall(){
		return ResponseEntity.status(HttpStatus.OK).body((List<AA>)repo.findAll());
	}
	@GetMapping("/aa/{id}")
	public ResponseEntity<AA> getAA(@PathVariable("id") String id) {
		Optional<AA> a= repo.findById(id);
		if(a.isPresent()) {
			return ResponseEntity.status(HttpStatus.FOUND).body(a.get());
		}
		return ResponseEntity.notFound().build();
	}
	@PostMapping("/aa")
	public ResponseEntity<AA> addAA(@RequestBody AA a){
		if(a.getUrl() == "" || a.getUrl() == null) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			try(CloseableHttpResponse response = httpClient.execute(new HttpGet(a.getUrl()))){
				HttpEntity entity = response.getEntity();
				a.setStatus(entity.getContentType().toString()+" : fetched");
				entity.writeTo(new FileOutputStream(f));
			}
			if(a.getX()!=0 && a.getY()!=0) {
				aar = new AAR(f, a.getX(), a.getY());
			} else {
				aar = new AAR(f);
			}
			a.setAa(aar.GenerateAscii());
			if(a.getId() != "" && a.getId() != null) {
				Optional<AA> existing = repo.findById(a.getId());
				a.setStatus("ID already in the database");
				if(existing.isEmpty()) {
					a.setStatus("ID created successfully");
					repo.save(a);
				}
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(a);
		} catch(Exception e) {
			e.printStackTrace();
			a.setStatus(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@PutMapping("/aa/{id}")
	public ResponseEntity<AA> updAA(@RequestBody AA a, @PathVariable("id") String id){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			try(CloseableHttpResponse response = httpClient.execute(new HttpGet(a.getUrl()))){
				HttpEntity entity = response.getEntity();
				a.setStatus(entity.getContentType().toString()+" : fetched");
				entity.writeTo(new FileOutputStream(f));
			}
			if(a.getX()!=0 && a.getY()!=0) {
				aar = new AAR(f, a.getX(), a.getY());
			} else {
				aar = new AAR(f);
			}
			a.setAa(aar.GenerateAscii());
			if(a.getId() != "" && a.getId() != null) {
				Optional<AA> existing = repo.findById(a.getId());
				a.setStatus("ID already in the database");
				if(existing.isEmpty()) {
					a.setStatus("ID created successfully");
					repo.save(a);
				}
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(a);
		} catch(Exception e) {
			e.printStackTrace();
			a.setStatus(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
}
