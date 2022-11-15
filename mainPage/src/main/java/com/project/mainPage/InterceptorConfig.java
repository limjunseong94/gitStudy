package com.project.mainPage; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.project.mainPage.interceptor.AdminInterceptor;
import com.project.mainPage.interceptor.LoginCheckInterceptor;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	@Autowired
	LoginCheckInterceptor loginCheckInterceptor;
	
	@Autowired
	AdminInterceptor adminInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(loginCheckInterceptor)
			.addPathPatterns("/board/insert/**")
			.addPathPatterns("/board/delete/**")
			.addPathPatterns("/board/update/**")
			.addPathPatterns("/board/prefer/**")
			.addPathPatterns("/reply/insert/**")
			.addPathPatterns("/reply/delete/**")
			.addPathPatterns("/reply/update/**")
			.addPathPatterns("/reply/prefer/**")
			.addPathPatterns("/qaboard/insert/**")
			.addPathPatterns("/qaboard/delete/**")
			.addPathPatterns("/qaboard/update/**")
			.addPathPatterns("/user/list/**")
			.addPathPatterns("/user/search/**")
			.addPathPatterns("/user/detail/**")
			.addPathPatterns("/user/update/**")
			.addPathPatterns("/user/delete/**")
			.addPathPatterns("/**/insert.do")
			.addPathPatterns("/**/update.do")
			.addPathPatterns("/recommendation")
			.addPathPatterns("/ /**")		
			// 추가하거나 예외처리할 주소
			.excludePathPatterns("/user/signup.do")
			.excludePathPatterns("/user/login.do")
			.excludePathPatterns("/user/privacy")
			.excludePathPatterns("/user/emailRejection")
			.excludePathPatterns("/user/agreement")
			.excludePathPatterns("/ / ")
			.excludePathPatterns("/ / ");
			
			registry.addInterceptor(adminInterceptor)
			.addPathPatterns("/user/list/**")
			.addPathPatterns("/user/search/**")
			.addPathPatterns("/notice/update/**")
			.addPathPatterns("/notice/delete/**")
			.addPathPatterns("/notice/insert.do")
			.addPathPatterns("/notice/update.do")
			.addPathPatterns("/qaboard/replyInsert.do")
			.addPathPatterns("/qaboard/replyUpdate/**")
			.addPathPatterns("/qaboard/replyUpdate.do")
			.addPathPatterns("/qaboard/replyDelete/**")
			.addPathPatterns("/top/tour/insert.do")
			.addPathPatterns("/top/tour/update/**")
			.addPathPatterns("/top/tour/delete/**")
			.addPathPatterns("/top/acco/insert.do")
			.addPathPatterns("/top/acco/update/**")
			.addPathPatterns("/top/acco/delete/**")
			.addPathPatterns("/top/rest/insert.do")
			.addPathPatterns("/top/rest/update/**")
			.addPathPatterns("/top/rest/delete/**");
	}
}