package br.com.marginais.controller.services;

import java.io.IOException;
import java.net.URLEncoder;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.marginais.model.entities.Picture;
import br.com.marginais.model.facade.PictureFacade;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2570927056611438598L;
	@ManagedProperty(value = "#{imageService}")
	private ImageService service;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("item_id");
		Picture picture = new PictureFacade().find(Long.valueOf(id));

		response.setContentType(picture.getFormat());
		response.setContentLength(picture.getPicture().length);
		response.setHeader("Content-Disposition",
				"inline;filename=\"" + URLEncoder.encode(picture.getId().toString(), "UTF-8") + "\"");
		response.getOutputStream().write(picture.getPicture());
	}

}
