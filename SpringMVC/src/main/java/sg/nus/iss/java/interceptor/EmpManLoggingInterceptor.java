package sg.nus.iss.java.interceptor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sg.nus.iss.java.model.User;

@Component
public class EmpManLoggingInterceptor implements HandlerInterceptor{
	private static final Logger LOGGER = LoggerFactory.getLogger(EmpManLoggingInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		LOGGER.info("Intercepting: " + request.getRequestURI());
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String role = (String) session.getAttribute("role");
		
		if (user == null) {
			response.sendRedirect("/login");
			return false;
		}
		else if (!(role.equals("Employee") || role.equals("Manager"))) {
			response.sendRedirect("/home");
			return false;
		}
		return true;
	}
}
