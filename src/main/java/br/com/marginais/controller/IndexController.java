package br.com.marginais.controller;

import java.util.List;

import javax.annotation.PostConstruct;

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
public class IndexController implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183087164363247334L;

	private List<Post> posts;

	@PostConstruct
	public void init() {
		posts = new PostFacade().findAllOrderByLastUpdated(15);
	}

	// ================================ List ================================

	public List<Post> getPosts() {
		return posts;
	}

	public Long getFirstPictureId(Long postId) {
		// List<Picture> pictures = p.getPictures();
		List<Picture> pictures = new PictureFacade().findByPostId(postId);
		if (pictures != null && !pictures.isEmpty()) {
			return pictures.get(0).getId();
		}
		return null;
	}

}
