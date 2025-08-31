package com.example.AAR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	@GetMapping("/")
	public ResponseEntity<List<AA>> getAAall(){
		return ResponseEntity.status(HttpStatus.OK).body((List<AA>)repo.findAll());
	}
	@GetMapping("/{id}")
	public ResponseEntity<AA> getAA(@PathVariable("id") String id) {
		Optional<AA> a= repo.findById(id);
		if(a.isPresent()) {
			AA aa = a.get();
			try {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(aa.getDir())));
				aa.setAa((String[]) is.readObject());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ResponseEntity.status(HttpStatus.FOUND).body(aa);
		}
		return ResponseEntity.notFound().build();
	}
	@GetMapping("/str/{id}")
	public ResponseEntity<String[]> getAAstr(@PathVariable("id") String id) {
		Optional<AA> a= repo.findById(id);
		if(a.isPresent()) {
			AA aa = a.get();
			try {
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(aa.getDir())));
				aa.setAa((String[]) is.readObject());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ResponseEntity.status(HttpStatus.FOUND).body(aa.getAa());
		}
		return ResponseEntity.notFound().build();
	}
	@PostMapping("/")
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
					a.setDir("./object_dir/"+a.getId());
					ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(a.getDir())));
					os.writeObject(a.getAa());
					a.setStatus("Object created successfully");
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
	@PostMapping("/str")
	public ResponseEntity<String[]> addAAstr(@RequestBody AA a){
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
					a.setDir("./object_dir/"+a.getId());
					ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(a.getDir())));
					os.writeObject(a.getAa());
					a.setStatus("Object created successfully");
					repo.save(a);
				}
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(a.getAa());
		} catch(Exception e) {
			e.printStackTrace();
			a.setStatus(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@PutMapping("/")
	public ResponseEntity<AA> updAA(@RequestBody AA a){
		if(a.getId() == "" || a.getId() == null) {return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();}
		Optional<AA> existing = repo.findById(a.getId());
		if(existing.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
		if(a.getUrl() == "" || a.getUrl() == null) {
			a.setUrl(existing.get().getUrl());
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
			a.setStatus("ID updated successfully");
			a.setDir("./object_dir/"+a.getId());
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(a.getDir())));
			os.writeObject(a.getAa());
			a.setStatus("Object updated successfully");
			repo.save(a);
			return ResponseEntity.status(HttpStatus.CREATED).body(a);
		} catch(Exception e) {
			e.printStackTrace();
			a.setStatus(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@PutMapping("/str")
	public ResponseEntity<String[]> updAAstr(@RequestBody AA a){
		if(a.getId() == "" || a.getId() == null) {return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();}
		Optional<AA> existing = repo.findById(a.getId());
		if(existing.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
		if(a.getUrl() == "" || a.getUrl() == null) {
			a.setUrl(existing.get().getUrl());
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
			a.setStatus("ID updated successfully");
			a.setDir("./object_dir/"+a.getId());
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(a.getDir())));
			os.writeObject(a.getAa());
			a.setStatus("Object updated successfully");
			repo.save(a);
			return ResponseEntity.status(HttpStatus.CREATED).body(a.getAa());
		} catch(Exception e) {
			e.printStackTrace();
			a.setStatus(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
