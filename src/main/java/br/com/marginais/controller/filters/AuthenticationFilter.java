package br.com.marginais.controller.filters;

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

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = { "/admin/*" })
public class AuthenticationFilter implements Filter {

	@SuppressWarnings("unused")
	private FilterConfig config;

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		if (((HttpServletRequest) req).getSession().getAttribute("user") == null) {
			((HttpServletResponse) resp).sendRedirect(req.getServletContext().getContextPath() + "/auth");
		} else {
			chain.doFilter(req, resp);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	public void destroy() {
		config = null;
	}

}