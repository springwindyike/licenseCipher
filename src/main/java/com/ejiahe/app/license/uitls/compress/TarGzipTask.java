package com.ejiahe.app.license.uitls.compress;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;


/**
 * 该类用于实现对文件或者文件夹进行打包操作.
 * <br/>
 * 会同时进行2部分操作,即tar(打包) + gzip(压缩)操作, 会开启双线程.
 * <br/>
 * 暂时只实现打包压缩功能pack, 未实现解压功能.
 * 
 * <br/>
 * 该类可用于持续写的文件的打包,但只处理打包文件前所确定大小的部分数据;
 * 
 * @author MaiLingFeng
 * 
 */
public class TarGzipTask {
	
	private GzipTask gzipTask = null;
	private TarTask tarTask = null;
	
	private static final int DEFAULT_BUFFER_SIZE = 2048;
	private boolean includeRootDir = true;

	private File sourceFile;
	private File storeFile;
	
	/**
	 * 实例化一个Tar+Gzip任务
	 * @param sourceFile 源文件或者目录
	 * @param storeFile 输出的位置,文件名最好为.tar.gz后缀
	 * @throws IOException 当源文件或者目录不存在时,抛出该异常
	 */
	public TarGzipTask(File sourceFile , File storeFile) throws IOException {
		this(sourceFile, storeFile, true, DEFAULT_BUFFER_SIZE);
	}
	
	
	/**
	 * 实例化一个Tar+Gzip任务
	 * @param sourceFile 源文件或者目录
	 * @param storeFile 输出的位置,文件名最好为.tar.gz后缀
	 * @param includeRootDir 打包时,是否同时将源文件的根目录进行打包
	 * @param bufferSize 读写缓冲区,加大该值可加快压缩速度
	 * @throws IOException 当源文件或者目录不存在时,抛出该异常
	 */
	public TarGzipTask(File sourceFile , File storeFile , boolean includeRootDir, int bufferSize) throws IOException {
		if(!sourceFile.exists())throw new FileNotFoundException("the file which ready to pack can not found");
		PipedInputStream tempPipedInput = new PipedInputStream((bufferSize>0? bufferSize : DEFAULT_BUFFER_SIZE)*3);
		
		gzipTask = new GzipTask(storeFile, tempPipedInput);
		tarTask = new TarTask(sourceFile, new PipedOutputStream(tempPipedInput));
		
		this.includeRootDir = includeRootDir;
		this.sourceFile = sourceFile;
		this.storeFile = storeFile;
	}
	
	
	/**
	 * 实例化一个Tar+Gzip任务
	 * @param sourceFile 源文件或者目录
	 * @param storeFile 输出的位置,文件名最好为.tar.gz后缀
	 * @param includeRootDir 打包时,是否同时将源文件的根目录进行打包
	 * @throws IOException 当源文件或者目录不存在时,抛出该异常
	 */
	public TarGzipTask(File sourceFile , File storeFile, boolean includeRootDir) throws IOException {
		this(sourceFile, storeFile, includeRootDir, DEFAULT_BUFFER_SIZE);
	}
	
	
	/**
	 * 执行压缩打包操作,会进行tar和gzip操作
	 * @throws IOException
	 */
	public void tarGzip() throws IOException {
		new Thread(tarTask).start();
		gzipTask.gzip();
	}

	
	/**
	 * 该类必须结合TarGzipTask使用,用于压缩tar打包数据
	 * @author MaiLingFeng
	 *
	 */
	private class GzipTask{
		private File storeFile;
		private InputStream pipedInput;
		
		public GzipTask(File storeFile , InputStream pipedInput ){
			this.storeFile = storeFile;
			this.pipedInput = pipedInput;
		}
		
		/**
		 * 进行gzip压缩操作
		 * @throws IOException
		 */
		public void gzip() throws IOException {
			if(!storeFile.getParentFile().exists())storeFile.getParentFile().mkdirs();
			BufferedInputStream input = null;
			GzipCompressorOutputStream gzipOutput = null;
			try{
				//System.out.println("Gzip begining...");
				input = new BufferedInputStream(pipedInput);
				gzipOutput = new GzipCompressorOutputStream(new FileOutputStream(storeFile));
				int len = 0;
				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int i = 1;
				for(len = input.read(buffer); len != -1 ; i++){
					gzipOutput.write(buffer, 0, len);
					len = input.read(buffer);
				}
				
				gzipOutput.flush();
				//System.out.println("Gzip finished...");
			}
			catch (IOException e){
//				System.out.println("Gzip faild..." + e.getMessage());
				throw new IOException("gZip faild , because of: " +  e.getMessage());
			}finally{
				try{
					if(input != null){
						input.close();
					}
					
					if(gzipOutput != null){
						gzipOutput.close();
					}
				}catch(Exception e){
					//ignore
				}
			}
		}
	}
	
	
	/**
	 * 打包任务
	 * @author MaiJingFeng
	 *
	 */
	private class TarTask implements Runnable {
		private File sourceFile;
		private OutputStream pipedOutput;
		
		public TarTask(File sourceFile , OutputStream output){
			this.sourceFile = sourceFile;
			this.pipedOutput = output;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				tar();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		/**
		 * 打包操作, 会进行tar操作
		 * @throws IOException 打包压缩过程中出错会抛出该异常
		 */
		public void tar() throws IOException {
			//System.out.println("Tar begining...");
			TarArchiveOutputStream tempTarOutput = new TarArchiveOutputStream(pipedOutput);
			try {
				tarArchiveEntry(sourceFile , tempTarOutput , "");
				tempTarOutput.finish();
				//System.out.println("Tar finished...");
			} catch (IOException e) {
//				System.out.println("Tar faild..." + e.getMessage());
				throw new IOException("Tar faild , because of: " +  e.getMessage());
			} finally{
				try{
					tempTarOutput.close();
				}catch(Exception e){
					//ignore;
				}
				
			}
		}
		
		
		/**
		 * 该方法用于进行文件项的打包,实现了文件夹打包功能
		 * <br/>
		 * TarArchiveOutputStream的目录结构是:
		 * 1.打包的TarAchiveEntry的name尾部带"/"的,表文件夹;
		 * 2.没有带"/"结尾的Entry代表一个文件;
		 * 3.通过"/"和Entry来决定最后打包的.tar的内部文件结构;
		 * 4.例如: parentDir/ , parentDir/a.exe , otherDir/
		 * 上面这三个文件进行打包之后, 用winrar打开打包后的.tar文件,
		 * 会发现里面的文件结构是2个文件夹,其中一个里面有exe文件.
		 * @param sourceRootFile 可以是文件夹或文件
		 * @param tarOutput
		 * @throws IOException
		 */
		private void tarArchiveEntry(File sourceRootFile, TarArchiveOutputStream tarOutput , String parentPath) throws IOException {
			if(!sourceRootFile.exists())return ;
			TarArchiveEntry archiveEntry = null;
			
			File[] list = null;
			//不包含目标根目录
			if(!includeRootDir && sourceRootFile.isDirectory()){
				list = sourceRootFile.listFiles();
			}else{
				list = new File[]{sourceRootFile};
			}
			
			//遍历目标文件列表,并进行打包
			for(File sourceFile : list){
				if(sourceFile.isFile()){
					
					archiveEntry = new TarArchiveEntry(parentPath + sourceFile.getName());
					BufferedInputStream input = new BufferedInputStream(new FileInputStream(sourceFile));
					long remainLength = sourceFile.length();
					archiveEntry.setSize(remainLength);
					tarOutput.putArchiveEntry(archiveEntry);
					int len = 0;
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					int i = 1;
					for(len = input.read(buffer); len != -1 ; i++){
						
						//为防止对正在持续写的文件进行打包时发生异常,这里我们只打包之前已确定大小的部分文件
						if(remainLength - (long)len >= 0){
							tarOutput.write(buffer, 0, len);
							remainLength = remainLength - (long)len;
						}else{
							//完成指定大小的打包操作
							tarOutput.write(buffer, 0, (int)remainLength);
							break;
						}
						
						len = input.read(buffer);
					}
					input.close();
					tarOutput.closeArchiveEntry();
					
				}else if(sourceFile.isDirectory()){
					String newParentPath = parentPath + sourceFile.getName() + File.separator;
					archiveEntry = new TarArchiveEntry(newParentPath);
					tarOutput.putArchiveEntry(archiveEntry);
					tarOutput.closeArchiveEntry();
					for(File childrenFile : sourceFile.listFiles()){
						tarArchiveEntry(childrenFile,tarOutput,newParentPath);
					}
				}
			}
		}
	}


	public File unTarGzip() throws IOException, ArchiveException {
		ArchiveInputStream in = null;
		BufferedInputStream bufferedInputStream = null;
		String baseDir = "";
		try {
			GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(new FileInputStream(sourceFile)));
			in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
			bufferedInputStream = new BufferedInputStream(in);
			TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
			baseDir = storeFile.getAbsolutePath();
			while (entry != null) {
				String name = entry.getName();
				String[] names = name.split("/");
				String fileName = baseDir;
				for (int i = 0; i < names.length; i++) {
					String str = names[i];
					fileName = fileName + File.separator + str;
				}

				if (name.endsWith("/")) {
					File file = new File(fileName);
					if (!file.exists()) {
						file.mkdirs();
					}
				} else {
					File file = new File(fileName);
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					file.createNewFile();

					BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
					int b;
					int buf_len = 1024;
					byte data[] = new byte[buf_len];
					while ((b = bufferedInputStream.read(data, 0, buf_len)) != -1) {
						bufferedOutputStream.write(data, 0, b);
					}
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
				}
				entry = (TarArchiveEntry) in.getNextEntry();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				//ignore
			}
		}

		return new File(baseDir);
	}

}
