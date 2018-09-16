package br.com.marginais.controller.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.marginais.model.entities.Picture;
import br.com.marginais.model.facade.PictureFacade;

@ManagedBean
@ApplicationScoped
public class ImageService implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3746460783784235859L;

	public StreamedContent getPicture() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the view. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the image. Return a real
			// StreamedContent with the image bytes.
			String id = context.getExternalContext().getRequestParameterMap().get("item_id");
			Picture picture = new PictureFacade().find(Long.valueOf(id));
			return picture != null ? new DefaultStreamedContent(new ByteArrayInputStream(picture.getPicture()))
					: new DefaultStreamedContent();
		}
	}
	
	public StreamedContent getSmThumb() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the view. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the image. Return a real
			// StreamedContent with the image bytes.
			String id = context.getExternalContext().getRequestParameterMap().get("item_id");
			Picture picture = new PictureFacade().find(Long.valueOf(id));
			return picture != null ? new DefaultStreamedContent(new ByteArrayInputStream(picture.getSmThumb()))
					: new DefaultStreamedContent();
		}
	}
	
	public StreamedContent getXsThumb() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the view. Return a stub StreamedContent so
			// that it will generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the image. Return a real
			// StreamedContent with the image bytes.
			String id = context.getExternalContext().getRequestParameterMap().get("item_id");
			Picture picture = new PictureFacade().find(Long.valueOf(id));
			return picture != null ? new DefaultStreamedContent(new ByteArrayInputStream(picture.getXsThumb()))
					: new DefaultStreamedContent();
		}
	}

}
