package br.com.marginais.model.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the post database table.
 * 
 */
@Entity
@Table(name = "post")
@NamedQueries({
		// All Posts
		@NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p"),

		// Posts ordered by Id Ascendent
		@NamedQuery(name = "Post.findAllOrderByIdAsc", query = "SELECT p FROM Post p ORDER BY p.lastUpdated ASC"),
		
		// All Posts ordered by lastUpdated
		@NamedQuery(name = "Post.findAllOrderByLastUpdated", query = "SELECT p FROM Post p ORDER BY p.lastUpdated DESC"),

		// Posts that contain images
		@NamedQuery(name = "Post.findImagePostsOrderByLastUpdatedReleaseDate", query = "SELECT DISTINCT p FROM Post p, Picture pic WHERE pic.post.id = p.id ORDER BY p.lastUpdated DESC"),

		// Posts that do not contain a video
		@NamedQuery(name = "Post.findNonVideoPostsOrderByIdDesc", query = "SELECT p FROM Post p WHERE p.videoLink IS NULL ORDER BY p.lastUpdated DESC"),
		// Counts Posts that contain images and do not contain a video
		@NamedQuery(name = "Post.countNonVideoPosts", query = "SELECT COUNT(p) FROM Post p WHERE p.videoLink IS NULL"),

		// Posts that contain a video
		@NamedQuery(name = "Post.findVideoPostsOrderByIdDesc", query = "SELECT p FROM Post p WHERE p.videoLink IS NOT NULL ORDER BY p.lastUpdated DESC"),
		// Counts Posts that contain a video
		@NamedQuery(name = "Post.countVideoPosts", query = "SELECT COUNT(p) FROM Post p WHERE p.videoLink IS NOT NULL"),

		// Post by URL Address
		@NamedQuery(name = "Post.findByUrlAddress", query = "SELECT p FROM Post p WHERE p.urlAddress = :urlAddress") })
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	@Column(name = "last_updated")
	private Timestamp lastUpdated;

	@Column(name = "release_date")
	private Timestamp releaseDate;

	@Column(name = "sub_title")
	private String subTitle;

	private String title;

	@Column(name = "url_address")
	private String urlAddress;

	@Column(name = "video_link")
	private String videoLink;

	// bidirectional many-to-one association to Picture
	@OneToMany(mappedBy = "post")
	private List<Picture> pictures;

	public Post() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Timestamp getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Timestamp releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getSubTitle() {
		return this.subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlAddress() {
		return this.urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public String getVideoLink() {
		return this.videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public List<Picture> getPictures() {
		return this.pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public Picture addPicture(Picture picture) {
		getPictures().add(picture);
		picture.setPost(this);

		return picture;
	}

	public Picture removePicture(Picture picture) {
		getPictures().remove(picture);
		picture.setPost(null);

		return picture;
	}

}