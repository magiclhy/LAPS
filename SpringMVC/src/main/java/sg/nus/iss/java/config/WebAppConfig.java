package sg.nus.iss.java.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import sg.nus.iss.java.interceptor.AdminLoggingInterceptor;
import sg.nus.iss.java.interceptor.AllEmpLoggingInterceptor;
import sg.nus.iss.java.interceptor.EmpManLoggingInterceptor;
import sg.nus.iss.java.interceptor.ManCeoLoggingInterceptor;

@Component
public class WebAppConfig implements WebMvcConfigurer{
	
	@Autowired
	AdminLoggingInterceptor adminLog;
	@Autowired
	AllEmpLoggingInterceptor allEmpLog;
	@Autowired
	EmpManLoggingInterceptor empManLog;
	@Autowired
	ManCeoLoggingInterceptor manCeoLog;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	// This interceptor only takes care of the URL paths stated below
		registry.addInterceptor(adminLog).addPathPatterns(
				"/leaveQuota/*", 
				"/ph/*", 
				"users/*");
		registry.addInterceptor(allEmpLog).addPathPatterns(
				"/leaves/create", 
				"/leaves/new", 
				"/leaves/view", 
				"/leaves/edit/*", 
				"/leaves/save", 
				"/leaves/delete/*", 
				"/leaves/cancel/*", 
				"/leaves/confirmCancel/*");
		registry.addInterceptor(empManLog).addPathPatterns(
				"/compensation/create", 
				"/compensation/view",
				"/compensation/details/*", 
				"/compensation/delete/*");
		registry.addInterceptor(manCeoLog).addPathPatterns(
				"/leaves/viewLeaveForApproval", 
				"/leaves/viewDetails/*", 
				"/leaves/confirmOutcome/*", 
				"/leaves/viewSubLeaves", 
				"/leaves/viewDetailsSub/*", 
				"/compensation/viewClaimsForApproval", 
				"/compensation/viewDetails/*", 
				"/compensation/confirmOutcome/*", 
				"/report/*");
	}
}