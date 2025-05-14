package koolsoft.server;

import com.googlecode.objectify.ObjectifyFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

public class MyObjectifyFilter extends ObjectifyFilter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
	    System.out.println("✅ MyObjectifyFilter is working!");
	    super.doFilter(request, response, chain);
	}
	
}