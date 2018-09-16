package br.com.marginais.controller.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

//@WebFilter(filterName = "CompressResponseFilter", urlPatterns = { "/*" })
public class CompressResponseFilter implements Filter {

	private HtmlCompressor compressor;

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		CharResponseWrapper responseWrapper = new CharResponseWrapper((HttpServletResponse) resp);

		chain.doFilter(req, responseWrapper);

		String servletResponse = new String(responseWrapper.toString());

		String imageId = req.getParameter("item_id");

		if (imageId == null) {
			// It is not an image
			try {
				resp.getWriter().write(compressor.compress(servletResponse));
			} catch (IllegalStateException ex) {
			}
		}
	}

	public void init(FilterConfig config) throws ServletException {
		compressor = new HtmlCompressor();
		compressor.setCompressCss(true);
		compressor.setCompressJavaScript(true);
	}

	public void destroy() {
	}

}
