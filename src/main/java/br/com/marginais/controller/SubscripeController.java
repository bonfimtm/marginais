package br.com.marginais.controller;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import br.com.marginais.controller.util.JsfUtil;
import br.com.marginais.model.entities.Subscription;
import br.com.marginais.model.facade.SubscriptionFacade;

@ManagedBean
@RequestScoped
public class SubscripeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1130263329213414529L;
	
	private String email;
	
	public void subscripe() {
		if (email == null || email.equals("")) {
			JsfUtil.addErrorMessage("O Endereço de E-mail é obrigatório.");
			return;
		}
		
		Subscription subscription = new Subscription();
		subscription.setEmail(email);
		subscription.setSubscriptionDate(new Timestamp(System.currentTimeMillis()));
		
		new SubscriptionFacade().create(subscription);
		
		JsfUtil.addSuccessMessage("Obrigado por se inscrever :)");
		
		email = null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
