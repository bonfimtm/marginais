package br.com.marginais.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import br.com.marginais.controller.util.JsfUtil;
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
public class ViewPostController implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183087164363247334L;

	private Post post;
	private List<Picture> pictures;

	private String postId;

	@PostConstruct
	public void init() {
	}

	public void prepareView() {
		post = new PostFacade().findByUrlAddress(postId);

		if (post != null) {
			pictures = new PictureFacade().findByPostId(post.getId());
		} else {
			JsfUtil.addErrorMessage("Endereço inválido");
		}
	}

	public Long getFirstPictureId() {
		if (post != null) {
			List<Picture> pictures = new PictureFacade().findByPostId(post.getId());
			if (pictures != null && !pictures.isEmpty()) {
				return pictures.get(0).getId();
			}
		}
		return null;
	}

	// =========================== Getters/Setters ============================

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
