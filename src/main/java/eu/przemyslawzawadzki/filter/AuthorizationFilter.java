package eu.przemyslawzawadzki.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter {

	public AuthorizationFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		

			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);

			String reqURI = reqt.getRequestURI();
			if (reqURI.indexOf("/public/") >= 0
					|| reqURI.contains("javax.faces.resource")){
                            System.out.println("firstfilter");
				chain.doFilter(request, response);
                        }else if ((ses != null && ses.getAttribute("login") != null
					&& reqURI.indexOf("/private/") >= 0)
					|| reqURI.contains("javax.faces.resource")){
                            System.out.println("lasttfilter");
				chain.doFilter(request, response);
                        }else 
				resp.sendRedirect(reqt.getContextPath() + "/public/index.xhtml");
		
	}

	@Override
	public void destroy() {

	}
}