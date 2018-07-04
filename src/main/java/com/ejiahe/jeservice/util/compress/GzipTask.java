package com.ejiahe.jeservice.util.compress;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 实现将单一文件压缩成gz文件,gzip操作
 * @author MaiLingFeng
 *
 */
public class GzipTask {

	private File sourceFile = null;
	private File storeFile = null;
	
	public GzipTask(File sourceFile , File storeFile){
		this.sourceFile = sourceFile;
		this.storeFile = storeFile;
	}
	
	public void gzip() throws IOException {
		if(!sourceFile.exists())throw new FileNotFoundException();
		if(!storeFile.getParentFile().exists())storeFile.getParentFile().mkdirs();
		
		BufferedInputStream input = null;
		GZIPOutputStream gzipOutput = null;
		try{
			//System.out.println("Gzip begining...");
			input = new BufferedInputStream(new FileInputStream(sourceFile));
			gzipOutput = new GZIPOutputStream(new FileOutputStream(storeFile));
			int len = 0;
			byte[] buffer = new byte[2048];
			int i = 1;
			for(len = input.read(buffer); len != -1 ; i++){
				gzipOutput.write(buffer, 0, len);
				len = input.read(buffer);
			}
			
			gzipOutput.flush();
			//System.out.println("Gzip finished...");
		}finally{
			try{
				if(input != null){
					input.close();
				}
				
				if(gzipOutput != null){
					gzipOutput.close();
				}
			}catch(Exception e){
				//ignore;
			}
		}
	}
	
	
	public void unGzip() throws IOException {
		if(!sourceFile.exists())throw new FileNotFoundException();
		
		if(!storeFile.getParentFile().exists())storeFile.getParentFile().mkdirs();
		
		GZIPInputStream input = null;
		BufferedOutputStream output = null;
		try{
			//System.out.println("Gzip begining...");
			input = new GZIPInputStream(new FileInputStream(sourceFile));
			output = new BufferedOutputStream(new FileOutputStream(storeFile));
			int len = 0;
			byte[] buffer = new byte[2048];
			int i = 1;
			for(len = input.read(buffer); len != -1 ; i++){
				output.write(buffer, 0, len);
				len = input.read(buffer);
			}
			
			output.flush();
			//System.out.println("UnGzip finished...");
		}finally{
			try{
				if(input != null){
					input.close();
				}
				
				if(output != null){
					output.close();
				}
			}catch(Exception e){
				//ignore;
			}
		}
	}
	
	
}
