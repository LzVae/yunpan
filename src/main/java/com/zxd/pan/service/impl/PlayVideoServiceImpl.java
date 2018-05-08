package com.zxd.pan.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.zxd.pan.enumeration.AccountAuth;
import com.zxd.pan.mapper.FileMapper;
import com.zxd.pan.model.File;
import com.zxd.pan.service.PlayVideoService;
import com.zxd.pan.util.ConfigureReader;

@Service
public class PlayVideoServiceImpl implements PlayVideoService {
	
	@Resource
	private FileMapper fm;

	@Override
	public File foundVideo(HttpServletRequest request) {
		// TODO 自动生成的方法存根
		String fileId=request.getParameter("fileId");
		if(fileId!=null&&fileId.length()>0) {
			File f=fm.queryById(fileId);
			if(f!=null) {
				String account=(String) request.getSession().getAttribute("ACCOUNT");
				if(ConfigureReader.instance(request).authorized(account, AccountAuth.DOWNLOAD_FILES)) {
					String fileName=f.getFileName();
					String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					if(suffix.equals("mp4")||suffix.equals("webm")) {
						return f;
					}
				}
			}
		}
		return null;
	}

}
