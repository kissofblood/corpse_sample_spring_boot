package com.september.fuelup.confuration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CachingRequestWrapper extends HttpServletRequestWrapper {
	
	private final String body;

	public CachingRequestWrapper(HttpServletRequest request) {
		super(request);

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		
		try {
			InputStream inputStream = request.getInputStream();

			if(inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				
				while((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			}
			else {
				stringBuilder.append("");
			}
		}
		catch(IOException e) {
			log.error("error read from inputStream", e);
		}
		finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch(IOException e) {
					log.error("error close buffer", e);
				}
			}
		}

		body = stringBuilder.toString();
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream buffer = new ByteArrayInputStream(body.getBytes());
		
		ServletInputStream inputStream = new ServletInputStream() {
			
			@Override
			public int read() throws IOException {
				return buffer.read();
			}

	        @Override
	        public boolean isFinished() {
	            return buffer.available() == 0;
	        }

	        @Override
	        public boolean isReady() {
	            return true;
	        }

	        @Override
	        public void setReadListener(ReadListener listener) {
	            throw new RuntimeException("Not implemented");
	        }
		};
		return inputStream;
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}
}
