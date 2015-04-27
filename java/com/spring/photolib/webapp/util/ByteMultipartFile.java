package com.spring.photolib.webapp.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class ByteMultipartFile implements MultipartFile{

	private String fileName;
	
	private byte [] content;
	
	private String contentType;
	
	private InputStream inputStream;
	
	public ByteMultipartFile(String fileName, String contentType, byte [] content) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.content = content;
		inputStream = new ByteArrayInputStream(content);
	}
	
	public String getName() {

		return fileName;
	}

	public String getOriginalFilename() {
	
		return fileName;
	}

	public String getContentType() {
	
		return contentType;
	}

	@Deprecated
	public boolean isEmpty() {
		return false;
	}

	@Deprecated
	public long getSize() {
		return content.length;
	}

	public byte[] getBytes() throws IOException {

		return content;
	}

	public InputStream getInputStream() throws IOException {
		return inputStream;
	}

	@Deprecated
	public void transferTo(File dest) throws IOException, IllegalStateException {
		// TODO Auto-generated method stub
		
	}

}
