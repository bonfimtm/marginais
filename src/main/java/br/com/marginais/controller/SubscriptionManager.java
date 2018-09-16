package br.com.marginais.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import br.com.marginais.controller.util.JsfUtil;
import br.com.marginais.model.entities.Subscription;
import br.com.marginais.model.facade.SubscriptionFacade;

/**
 *
 * @author Thiago Bonfim
 */
@javax.faces.bean.ManagedBean
@javax.faces.bean.ViewScoped
public class SubscriptionManager implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3183087164363247334L;

	private List<Subscription> subscriptions;
	private Subscription subscriptionToDelete;

	@PostConstruct
	public void init() {
		subscriptions = new SubscriptionFacade().findAll();
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void prepareDeleteSubscription(Subscription interview) {
		subscriptionToDelete = interview;
	}

	public String deleteSubscription() {
		new SubscriptionFacade().remove(subscriptionToDelete);
		init();
		JsfUtil.addSuccessMessage("Inscrição excluída com sucesso.");
		return null;
	}

	public void cancelDeleteSubscription() {
		subscriptionToDelete = null;
	}

	@SuppressWarnings("finally")
	public String downloadSubscriptions() {
		OutputStream output = null;
		FacesContext fc = null;
		try {

			String filename = "subscriptions.txt";

			fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

			response.reset();
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

			output = response.getOutputStream();

			for (Subscription s : subscriptions) {
				String str = String.format("<%s>,", s.getEmail());
				output.write(str.getBytes());
			}

		} catch (IOException ex) {
			JsfUtil.addErrorMessage("Erro ao fazer o download do arquivo.");
		} catch (NullPointerException ex) {
			JsfUtil.addErrorMessage("Erro ao fazer o download do arquivo.");
		} finally {
			try {
				output.flush();
				output.close();
			} catch (IOException e) {
				JsfUtil.addErrorMessage("Erro ao fazer o download do arquivo.");
			} catch (NullPointerException ex) {
				JsfUtil.addErrorMessage("Erro ao fazer o download do arquivo.");
			}

			fc.responseComplete();

			return null;
		}
	}

}
