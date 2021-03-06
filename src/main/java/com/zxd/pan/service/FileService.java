package com.zxd.pan.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	/**
	 * 验证文件是否允许上传
	 * <p>验证上传请求是否合法</p>
	 * @param request HttpServletRequest 对象
	 * @return String 结果代码：
	 * <ul>
	 * <li>noAccount 无用户信息，上传失败</li>
	 * <li>errorParameter 传参错误，上传失败</li>
	 * <li>noAuthorized 用户未被授权此操作，上传失败</li>
	 * <li>duplicationFileName 文件名重复，上传失败</li>
	 * <li>permitUpload 允许上传</li>
	 * </ul>
	 * */
	String checkUploadFile(HttpServletRequest request);
	
	/**
	 * 进行文件上传
	 * <p>将文件上传至服务器block区，并写入node信息</p>
	 * @param request HttpServletRequest 请求对象
	 * @param file MultipartFile 上传的文件，注意必须使用commons定义的格式
	 * @return String 操作结果代码
	 * <ul>
	 * <li>uploaderror 上传失败</li>
	 * <li>uploadsuccess 上传成功</li>
	 * </ul>
	 * */
	String doUploadFile(HttpServletRequest request,MultipartFile file);
	
	/**
	 * 删除文件
	 * <p>删除服务器内指定文件</p>
	 * @param request HttpServletRequest 对象
	 * @return String 结果代码：
	 * <ul>
	 * <li>noAccount 无用户信息，删除失败</li>
	 * <li>errorParameter 传参错误，删除失败</li>
	 * <li>noAuthorized 用户未被授权此操作，删除失败</li>
	 * <li>cannotDeleteFile 无法删除文件信息，删除失败</li>
	 * <li>deleteFileSuccess 允许上传</li>
	 * </ul>
	 * */
	String deleteFile(HttpServletRequest request);
	
	/**
	 * 执行下载文件
	 * <p>从服务器中通过node信息得到block并解析为原文件信息，提供用户下载</p>
	 * @param request HttpServletRequest 请求对象
	 * @return ResponseEntity
	 * */
	void doDownloadFile(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 重命名文件
	 * <p>重命名服务器内指定文件</p>
	 * @param request HttpServletRequest 对象
	 * @return String 结果代码：
	 * <ul>
	 * <li>noAccount 无用户信息，重命名失败</li>
	 * <li>errorParameter 传参错误，重命名失败</li>
	 * <li>noAuthorized 用户未被授权此操作，重命名失败</li>
	 * <li>cannotRenameFile 无法重写文件信息，重命名失败</li>
	 * <li>renameFileSuccess 重命名成功</li>
	 * </ul>
	 * */
	String doRenameFile(HttpServletRequest request);
	
	/**
	 * 删除被选中的文件
	 * <p>删除服务器内所有被选中的文件，其过程与删除单个文件类似，只是批量操作了</p>
	 * @param request HttpServletRequest 对象
	 * @return String 结果代码：
	 * <ul>
	 * <li>noAccount 无用户信息，删除失败</li>
	 * <li>errorParameter 传参错误，删除失败</li>
	 * <li>noAuthorized 用户未被授权此操作，删除失败</li>
	 * <li>cannotDeleteFile 无法删除文件信息，删除失败</li>
	 * <li>deleteFileSuccess 允许上传</li>
	 * </ul>
	 * */
	String deleteCheckedFiles(HttpServletRequest request);
	
	/**
	 * 压缩被选中的文件
	 * <p>与普通下载类似，但是这种是把全部选中文件以zip形式压缩后再提供用户下载，这里提供压缩过程</p>
	 * @param request HttpServletRequest 请求对象
	 * @return ResponseEntity
	 * */
	String downloadCheckedFiles(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 下载被选中的文件压缩包
	 * <p>与普通下载类似，但是这种是把全部选中文件以zip形式压缩后再提供用户下载，这里提供下载过程</p>
	 * @param request HttpServletRequest 请求对象
	 * @return ResponseEntity
	 * @throws Exception 
	 * */
	void downloadCheckedFilesZip(HttpServletRequest request,HttpServletResponse response) throws Exception;

}
