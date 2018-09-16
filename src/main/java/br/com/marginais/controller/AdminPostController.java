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
public class AdminPostController implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183087164363247334L;

	private List<Post> posts;
	private Post postToDelete;

	@PostConstruct
	public void init() {
		posts = new PostFacade().findAllOrderByIdAsc();
	}

	// ================================ List ================================

	public Long getFirstPictureId(Long postId) {
		if (posts != null && !posts.isEmpty()) {
			for (Post p : posts) {
				if (p.getId() == postId) {
					// List<Picture> pictures = p.getPictures();
					List<Picture> pictures = new PictureFacade().findByPostId(postId);
					if (pictures != null && !pictures.isEmpty()) {
						return pictures.get(0).getId();
					}
				}
			}
		}
		return null;
	}

	// =============================== Delete ===============================

	public void prepareDeletePost(Post interview) {
		postToDelete = interview;
	}

	public String deletePost() {
		new PostFacade().remove(postToDelete);
		init();
		JsfUtil.addSuccessMessage("Inscrição excluída com sucesso.");
		return null;
	}

	public void cancelDeletePost() {
		postToDelete = null;
	}

	// =========================== Getters/Setters ============================

	public List<Post> getPosts() {
		return posts;
	}
}
