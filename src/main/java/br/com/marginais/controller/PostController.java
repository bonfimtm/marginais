package br.com.marginais.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import br.com.marginais.controller.util.JsfUtil;
import br.com.marginais.controller.util.PaginationHelper;
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
public class PostController implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183087164363247334L;

	private String page;
	private int pageNumber;

	private Long count;
	private PaginationHelper<Post> pageItems;

	@PostConstruct
	public void init() {
	}

	public void preparePosts() {
		pageNumber = 1;
		preparePagedPosts();
	}

	public void preparePagedPosts() {
		final PostFacade postFacade = new PostFacade();

		try {

			if (page != null) {
				pageNumber = Integer.parseInt(page);
			}
			count = postFacade.countNonVideoPosts();
			pageItems = new PaginationHelper<Post>(count, pageNumber, 2) {

				@Override
				public List<Post> getData() {
					return postFacade.findNonVideoPostsOrderByIdDesc(begin, end);
				}
			};

			if (!pageItems.isValidPage()) {
				JsfUtil.addErrorMessage("Página inválida");
			}

		} catch (NumberFormatException ex) {
			JsfUtil.addErrorMessage("Endereço inválido");
		}
	}

	public boolean getHasPrev() {
		return pageItems != null ? pageItems.getHasPrev() : false;
	}

	public boolean getHasNext() {
		return pageItems != null ? pageItems.getHasNext() : false;
	}

	public long getTotalOfPages() {
		return pageItems != null ? pageItems.getTotalOfPages() : 0;
	}

	public List<Post> getPosts() {
		return pageItems != null ? pageItems.getItems() : null;
	}

	// ================================= List =================================

	public Long getFirstPictureId(Long postId) {
		// List<Picture> pictures = p.getPictures();
		List<Picture> pictures = new PictureFacade().findByPostId(postId);
		if (pictures != null && !pictures.isEmpty()) {
			return pictures.get(0).getId();
		}
		return null;
	}

	// =========================== Getters/Setters ============================

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public Long getCount() {
		return count;
	}

}
