package com.zxd.pan.mapper;

import java.util.List;
import java.util.Map;

import com.zxd.pan.model.File;

public interface FileMapper {
	
	List<File> queryByParentFolderId(String pfid);
	
	int insert(File f);
	
	int deleteByParentFolderId(String pfid);
	
	int deleteById(String fileId);
	
	File queryById(String fileId);
	
	int updateFileNameById(Map<String, String> map);
	
	List<File> queryAll();
	
	File queryByPath(String path);

}
