package br.com.marginais.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the picture database table.
 * 
 */
@Entity
@Table(name = "picture")
@NamedQueries({
	// All Pictures
	@NamedQuery(name="Picture.findAll", query="SELECT p FROM Picture p"),
	
	// Pictures of a Post
	@NamedQuery(name="Picture.findByPostId", query="SELECT p FROM Picture p WHERE p.post.id = :postId ORDER BY p.id ASC")
})
public class Picture implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Column(name="file_name")
	private String fileName;

	private String format;

	private byte[] picture;

	@Column(name="sm_thumb")
	private byte[] smThumb;

	@Column(name="xs_thumb")
	private byte[] xsThumb;

	//bidirectional many-to-one association to Post
	@ManyToOne
	private Post post;

	public Picture() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public byte[] getPicture() {
		return this.picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public byte[] getSmThumb() {
		return this.smThumb;
	}

	public void setSmThumb(byte[] smThumb) {
		this.smThumb = smThumb;
	}

	public byte[] getXsThumb() {
		return this.xsThumb;
	}

	public void setXsThumb(byte[] xsThumb) {
		this.xsThumb = xsThumb;
	}

	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}