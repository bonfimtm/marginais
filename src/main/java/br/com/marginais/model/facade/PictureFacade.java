package br.com.marginais.model.facade;

import java.util.List;

import javax.persistence.TypedQuery;

import br.com.marginais.model.entities.Picture;

/**
 *
 * @author Thiago Bonfim
 */
public class PictureFacade extends AbstractFacade<Picture> {

	public PictureFacade() {
		super(Picture.class);
	}

	public List<Picture> findByPostId(Long postId) {
		try {
			if (postId == null) {
				return null;
			}
			TypedQuery<Picture> query = getEntityManager().createNamedQuery("Picture.findByPostId", Picture.class);
			query.setParameter("postId", postId);
			return query.getResultList();
		} finally {
			close();
		}

	}

}
