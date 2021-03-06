package com.zxd.pan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.zxd.pan.enumeration.AccountAuth;
import com.zxd.pan.enumeration.LogLevel;

/**
 * 配置文件解析类
 * <p>
 * 该类中封装了对configure文件的解析操作，其实例由instance方法提供
 * </p>
 * 配置格式： account.list = user1,user2,user3... pwd.user1 = 123456 ... auth.admin =
 * cud ... authOverall = l buff.size=1048576
 */
public class ConfigureReader {

	private static ConfigureReader cr;

	private Properties confp;

	// 初始化配置文件并将其装载
	private ConfigureReader(HttpServletRequest request) {
//		String realPath = request.getServletContext().getRealPath("etc/");
//		File conf = new File(realPath + "configure.properties");
		InputStream is = null;
		
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("configure.properties");
//			FileInputStream fis = new FileInputStream(conf);
			if (is == null) {
				throw new FileNotFoundException("config file is not found");	
			}
			
			confp = new Properties();
			confp.load(is);
			
			System.out.println("config file load success!");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			System.out.println("KohgylwIFT:[Configure]cannot read file:etc/configure.properties");
		}
		
	}

	/**
	 * 得到实例
	 * <p>
	 * 本类的实例为单例，请使用本方法获取唯一实例
	 * </p>
	 * 
	 * @return ConfigureReader Object
	 */
	public static ConfigureReader instance(HttpServletRequest request) {
		if (cr == null) {
			cr = new ConfigureReader(request);
		}
		return cr;
	}

	/**
	 * 判断用户是否在配置中声明
	 * <p>
	 * 将传入用户与配置文件中的用户列表进行比对，如果找到该用户，则返回true，否则返回false
	 * </p>
	 * 
	 * @param account
	 *            String 要查找的账户
	 */
	public boolean foundAccount(String account) {
		if (confp != null) {
			String[] accountList = confp.getProperty("accounts.list").split(",");
			boolean index = false;
			for (String s : accountList) {
				if (s.equals(account)) {
					index = true;
				}
			}
			return index;
		} else {
			return false;
		}
	}

	/**
	 * 检查账户与密码是否匹配
	 * <p>
	 * 对传入的账户及密码与配置文件中的信息进行比对，如果匹配则返回true，否则返回false
	 * </p>
	 * 
	 * @param account
	 *            String 需要检查的账户
	 * @param pwd
	 *            String 需要检查的密码
	 */
	public boolean checkAccountPwd(String account, String pwd) {
		if (confp != null) {
			String apwd = confp.getProperty("pwd." + account);
			return apwd == null ? false : apwd.equals(pwd);
		} else {
			return false;
		}
	}

	/**
	 * 判断用户是否具备某权限
	 * <p>
	 * 检查配置中用户是否具备对传入的权限
	 * </p>
	 * 
	 * @see AccountAuth
	 * @param account
	 *            String 需要检查的用户
	 * @param auth
	 *            AccountAuth 需要检查的权限
	 */
	public boolean authorized(String account, AccountAuth auth) {
		if (confp != null) {
			if (account != null && account.length() > 0) {
				String auths = "";
				String accauth = confp.getProperty("auth." + account);
				String overall = confp.getProperty("authOverall");
				if (accauth != null) {
					auths = accauth;
				}
				if (overall != null) {
					auths = auths + overall;
				}
				switch (auth) {
				case CREATE_NEW_FOLDER:
					return auths.indexOf("c") == -1 ? false : true;
				case UPLOAD_FILES:
					return auths.indexOf("u") == -1 ? false : true;
				case DELETE_FILE_OR_FOLDER:
					return auths.indexOf("d") == -1 ? false : true;
				case RENAME_FILE_OR_FOLDER:
					return auths.indexOf("r") == -1 ? false : true;
				case DOWNLOAD_FILES:
					return auths.indexOf("l") == -1 ? false : true;
				default:
					return false;
				}
			} else {
				String overall = confp.getProperty("authOverall");
				if (overall != null) {
					switch (auth) {
					case CREATE_NEW_FOLDER:
						return overall.indexOf("c") == -1 ? false : true;
					case UPLOAD_FILES:
						return overall.indexOf("u") == -1 ? false : true;
					case DELETE_FILE_OR_FOLDER:
						return overall.indexOf("d") == -1 ? false : true;
					case RENAME_FILE_OR_FOLDER:
						return overall.indexOf("r") == -1 ? false : true;
					case DOWNLOAD_FILES:
						return overall.indexOf("l") == -1 ? false : true;
					default:
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * 得到用户设置的下载缓存大小
	 * <p>
	 * 该值越大则能提供的下载速度越快，但是内存占用也越高，如果设置无效或者未设置，则默认为1048576
	 * </p>
	 * 
	 * @return int buffer size
	 */
	public int getBuffSize() {
		if (confp != null) {
			String s = confp.getProperty("buff.size");
			if (s != null && s.length() > 0) {
				try {
					return Integer.parseInt(s);
				} catch (Exception e) {
					// TODO: handle exception
					return 1048576;
				}
			} else {
				return 1048576;
			}
		} else {
			return 1048576;
		}
	}

	/**
	 * 检查传入的日志等级是否符合设置
	 * <p>日志等级层次为：None小于Runtime_Exception小于Event，传入低等级时返回false，传入相同等级或更高等级时返回true。<p>
	 * @param l LogLevel 日志等级，传入为null时则永远返回false
	 * @return boolean 是否具备该等级
	 * */
	public boolean inspectLogLevel(LogLevel l) {
		int o=0;
		int m=0;
		if(l==null) {
			return false;
		}
		switch (l) {
		case None:
			m=0;
			break;
		case Runtime_Exception:
			m=1;
		case Event:
			m=2;
			break;
		default:
			m=0;
			break;
		}
		if(confp!=null) {
			String ll = confp.getProperty("log");
			if(ll==null) {
				ll="";
			}
			switch (ll) {
			case "N":
				o=0;
				break;
			case "R":
				o=1;
				break;
			case "E":
				o=2;
				break;
			default:
				o=1;
				break;
			}
		}else {
			o=1;
		}
		if(o>=m) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 
	 * 判断是否开启了“必须登录”设置
	 * <p>该设置如果开启，则任何操作必须在登录的前提下进行，包括进入应用主页查看文件列表。默认为关闭。</p>
	 * @author 青阳龙野(kohgylw)
	 * @return boolean 是否开启
	 */
	public boolean mustLogin() {
		if(confp!=null) {
			String s=confp.getProperty("mustLogin");
			if(s!=null) {
				if(s.equals("N")) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

}
