package br.com.marginais.controller;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.marginais.controller.util.JsfUtil;
import br.com.marginais.controller.util.Slug;
import br.com.marginais.controller.util.Util;
import br.com.marginais.model.entities.Picture;
import br.com.marginais.model.entities.Post;
import br.com.marginais.model.facade.PictureFacade;
import br.com.marginais.model.facade.PostFacade;

/**
 *
 * @author Thiago Bonfim
 */
@javax.faces.bean.ManagedBean
@javax.faces.bean.ViewScoped
public class AdminCreateEditPostController implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183087164363247334L;

	private Post post;
	private List<Picture> pictures;

	private String subTitle;
	private String title;
	private String videoLink;
	private String content;

	private UploadedFile file;
	private static final int PICTURE_MAX_SIZE = 1048576;
	private static final int SM_MAX_WIDTH = 400;
	private static final int XS_MAX_WIDTH = 200;

	// Editing Post
	private String postId;

	@PostConstruct
	public void init() {
	}

	public void prepareCreate() {
		if (post == null) {
			post = new Post();
		}
	}

	public void prepareEdit() {
		try {
			Long id = Long.parseLong(postId);
			post = new PostFacade().find(id);

			title = post.getTitle();
			subTitle = post.getSubTitle();
			videoLink = post.getVideoLink();
			content = post.getContent();

			pictures = new PictureFacade().findByPostId(post.getId());
		} catch (NumberFormatException ex) {
			JsfUtil.addErrorMessage("Endereço inválido");
		}
	}

	// ================================ Create ================================
	// ================================= Edit =================================

	public String createOrEdit() {
		try {

			title = Util.trimCheck(title);
			subTitle = Util.trimCheck(subTitle);
			videoLink = Util.trimCheck(videoLink);
			content = Util.trimCheck(content);

			if (title == null) {
				JsfUtil.addErrorMessage("O Título é obrigatório.");
				return null;
			}

			if (title.length() < 5) {
				JsfUtil.addErrorMessage("O Título é muito curto.");
				return null;
			}
			
			if (title.length() > 170) {
				JsfUtil.addErrorMessage("O Título é muito longo.");
				return null;
			}

			if (subTitle != null) {
				if (subTitle.length() > 170) {
					JsfUtil.addErrorMessage("O subtítulo é muito longo.");
					return null;
				}
			}

			if (videoLink != null) {
				if (videoLink.length() > 100) {
					JsfUtil.addErrorMessage("A Id do vídeo é muito longa.");
					return null;
				}
			}

			if (content == null) {
				JsfUtil.addErrorMessage("O Texto é obrigatório.");
				return null;
			}
			if (content.length() > 50000) {
				JsfUtil.addErrorMessage("O Texto é muito longo.");
				return null;
			}
			
			content = content.replaceAll("&lt;", "").replaceAll("&gt;", "");

			String urlAddress = Slug.makeSlug(title);
			if (urlAddress.length() > 100) {
				urlAddress = urlAddress.substring(0, 100);
			}
			
			
			post.setTitle(title);
			post.setSubTitle(subTitle);
			post.setVideoLink(videoLink);
			post.setContent(content);
			post.setUrlAddress(urlAddress);

			
			PostFacade postFacade = new PostFacade();

			if (post.getId() == null) {
				// Create

				Timestamp time = new Timestamp(System.currentTimeMillis());
				
				post.setReleaseDate(time);
				post.setLastUpdated(time);
				
				postFacade.create(post);

				PictureFacade pictureFacade = new PictureFacade();
				if (pictures != null) {
					for (Picture p : pictures) {
						p.setPost(post);
						pictureFacade.create(p);
					}
				}

				// Clean up
				post = null;
				pictures = null;
				title = subTitle = videoLink = content = null;

				JsfUtil.addSuccessMessage("Postagem criada com sucesso.");
			} else {
				// Edit

				post.setLastUpdated(new Timestamp(System.currentTimeMillis()));
				postFacade.edit(post);

				// Updates view
				prepareEdit();

				JsfUtil.addSuccessMessage("Postagem atualizada com sucesso.");
			}

		} catch (Exception ex) {
			if (ex.toString().contains("duplicate key value violates unique constraint")
					&& ex.toString().contains("post_url_address_key")) {
				JsfUtil.addErrorMessage(
						"Título muito semelhante a título já existente. Por favor, selecione outro título");
			} else {
				JsfUtil.addErrorMessage("Falha catastrófica :O");
			}
		}
		return null;
	}

	public void handleFileUpload(FileUploadEvent event) {
		file = event.getFile();
		if (file != null && file.getContentType().contains("image")) {

			if (pictures == null) {
				pictures = new LinkedList<Picture>();
			}
			Picture p = new Picture();
			p.setFileName(file.getFileName());
			p.setFormat(file.getContentType());

			p.setPicture(Util.compressImage(file.getContents(), PICTURE_MAX_SIZE));
			//p.setPicture(file.getContents());
			p.setSmThumb(Util.resizeImage(file.getContents(), SM_MAX_WIDTH));
			p.setXsThumb(Util.resizeImage(file.getContents(), XS_MAX_WIDTH));

			pictures.add(p);

			if (post.getId() == null) {
				// Create

				JsfUtil.addSuccessMessage("Fotos enviadas com sucesso. Confirme para postá-las.");
			} else {
				// Edit

				p.setPost(post);
				new PictureFacade().create(p);

				post.setLastUpdated(new Timestamp(System.currentTimeMillis()));
				new PostFacade().edit(post);

				JsfUtil.addSuccessMessage("Fotos atualizadas com sucesso.");
			}
		}
	}

	// ================================ Delete ================================

	public void deletePicture(Picture picture) {
		new PictureFacade().remove(picture);

		post.setLastUpdated(new Timestamp(System.currentTimeMillis()));
		new PostFacade().edit(post);

		JsfUtil.addSuccessMessage("Imagem excluída com sucesso.");
		prepareEdit();
	}

	public void deletePictureFromView(Picture picture) {
		if (pictures != null) {
			pictures.remove(picture);
		}
		JsfUtil.addSuccessMessage("Imagem removida com sucesso.");
	}

	// =========================== Getters/Setters ============================

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

}
