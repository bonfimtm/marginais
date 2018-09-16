package br.com.marginais.model.facade;

import java.util.List;

import javax.persistence.TypedQuery;

import br.com.marginais.model.entities.Post;

/**
 *
 * @author Thiago Bonfim
 */
public class PostFacade extends AbstractFacade<Post> {

	public PostFacade() {
		super(Post.class);
	}

	public List<Post> findAllOrderByIdAsc() {
		try {
			TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findAllOrderByIdAsc", Post.class);
			return query.getResultList();
		} finally {
			close();
		}
	}

	public List<Post> findAllOrderByLastUpdated(int maxResults) {
		try {
			TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findAllOrderByLastUpdated", Post.class);
			query.setMaxResults(maxResults);
			return query.getResultList();
		} finally {
			close();
		}
	}

	public List<Post> findImagePostsOrderByLastUpdatedReleaseDate(int maxResults) {
		try {
			TypedQuery<Post> query = getEntityManager()
					.createNamedQuery("Post.findImagePostsOrderByLastUpdatedReleaseDate", Post.class);
			query.setMaxResults(maxResults);
			return query.getResultList();
		} finally {
			close();
		}
	}

	public List<Post> findNonVideoPostsOrderByIdDesc() {
		try {
			TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findNonVideoPostsOrderByIdDesc",
					Post.class);
			return query.getResultList();
		} finally {
			close();
		}
	}

	public List<Post> findNonVideoPostsOrderByIdDesc(int begin, int end) {
		try {
			TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findNonVideoPostsOrderByIdDesc",
					Post.class);
			query.setMaxResults(end - begin + 1);
			query.setFirstResult(begin);
			return query.getResultList();
		} finally {
			close();
		}
	}

	public Long countNonVideoPosts() {
		try {
			TypedQuery<Long> query = getEntityManager().createNamedQuery("Post.countNonVideoPosts", Long.class);
			return query.getSingleResult();
		} finally {
			close();
		}
	}

	public List<Post> findVideoPostsOrderByIdDesc() {
		try {
			TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findVideoPostsOrderByIdDesc",
					Post.class);
			return query.getResultList();
		} finally {
			close();
		}
	}

	public List<Post> findVideoPostsOrderByIdDesc(int begin, int end) {
		try {
			TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findVideoPostsOrderByIdDesc",
					Post.class);
			query.setMaxResults(end - begin + 1);
			query.setFirstResult(begin);
			return query.getResultList();
		} finally {
			close();
		}
	}

	public Long countVideoPosts() {
		try {
			TypedQuery<Long> query = getEntityManager().createNamedQuery("Post.countVideoPosts", Long.class);
			return query.getSingleResult();
		} finally {
			close();
		}
	}

	public Post findByUrlAddress(String urlAddress) {
		try {
			TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findByUrlAddress", Post.class);
			query.setParameter("urlAddress", urlAddress);
			return query.getResultList().size() == 1 ? query.getSingleResult() : null;
		} finally {
			close();
		}
	}

}
