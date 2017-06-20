package net.slipp.web;

import javax.servlet.http.HttpSession;

import net.slipp.domain.User;

public class HttpSessionUtils {

	public static final String USER_SESSION_KEY = "sessionedUser";
	
	public static boolean isLoginUser(HttpSession session){	
		User sessionedUser = (User)(session.getAttribute(HttpSessionUtils.USER_SESSION_KEY));		
		if ( sessionedUser == null) {
			return false;			
		}
		return true;
	}
	
	public static User getUserFormSession(HttpSession session){		
		if (!isLoginUser(session)) {
			return null;
		}		
		return (User)(session.getAttribute(HttpSessionUtils.USER_SESSION_KEY));		
	}
}
