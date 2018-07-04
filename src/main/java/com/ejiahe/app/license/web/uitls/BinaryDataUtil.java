
package com.ejiahe.app.license.web.uitls;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 二进制数据存取工具类.
 * 
 * @author liaofei
 * 
 */
public class BinaryDataUtil {
	/**
	 * 读取文件内容.
	 * 
	 * @param file
	 *            文件
	 */
	public static byte[] readFileData(final File file) {
		byte[] ret = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ret = new byte[fis.available()];
			int count = fis.read(ret);
			if (count != ret.length) {
				throw new RuntimeException("读取文件内容不完整");
			}
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 把文件中的数据写入到Http响应.
	 * 
	 * @param file 文件
	 * @param fileName 文件名
	 * @param request http请求
	 * @param response httpResponse
	 */
	public static void fileToResponse(final File file, final String fileName, final HttpServletRequest request,
			final HttpServletResponse response) {
		if (!file.exists()) {
			return;
		}
		String sfileName;
		try {
			//sfileName = encodeFilename(request, file.getName());
			System.out.println("fileName="+fileName);
			sfileName = encodeFilename(request, fileName);
			//attachment：点击会提供对话框选择另存为
			//inline：通过IE浏览器直接选择相关应用程序插件打开
			response.setHeader("Content-Disposition", "attachment;filename=\"" + sfileName);// attachment;inline
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
//		System.out.println("request.getServletContext()="+request.getServletContext());
//		//application/msword
//		System.out.println("doc="+request.getServletContext().getMimeType("aa.doc"));
//		//text/plain
//		System.out.println("txt="+request.getServletContext().getMimeType("bb.txt"));
		response.setContentType(request.getServletContext().getMimeType(fileName));
		FileInputStream fis = null;
		ServletOutputStream sos = null;
		try {
			fis = new FileInputStream(file);
			sos = response.getOutputStream();
			final byte[] buffer = new byte[1024];
			int count = 0;
			int readCount = 0;
			while ((readCount = fis.read(buffer)) > 0) {
				sos.write(buffer, 0, readCount);
				count += readCount;
			}
			response.setContentLength(count);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
			}
			try {
				if (sos != null) {
					sos.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 把二进数据写入到Http响应流.
	 *
	 * @param data
	 *            大字段
	 * @param fileName
	 *            数据类型，即文件名称后缀
	 * @param request
	 *            http请求
	 * @param response
	 *            数据流
	 */
	public static void blobToResponse(final byte[] data, final String fileName, final HttpServletRequest request,
			final HttpServletResponse response) {
		if (data == null || data.length == 0) {
			return;
		}
		try {
			String sfileName = encodeFilename(request, fileName);
			response.setHeader("Content-Disposition", "attachment;filename=\"" + sfileName);// attachment;inline
		} catch (UnsupportedEncodingException e1) {
			throw new RuntimeException(e1);
		}
		response.setContentType(request.getServletContext().getMimeType(fileName));
		response.setContentLength(data.length);
		ServletOutputStream sos = null;
		try {
			sos = response.getOutputStream();
			sos.write(data);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (sos != null) {
					sos.close();
				}
			} catch (Exception e) {
			}
		}
	}

	// IE与firefox, google浏览器表现不同，分开处理
	public static String encodeFilename(HttpServletRequest req, String filename) throws UnsupportedEncodingException {
		String stmp = filename;
		if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
			stmp = URLEncoder.encode(filename, "UTF-8");
		} else {
			stmp = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		}
		return stmp;
	}
}
