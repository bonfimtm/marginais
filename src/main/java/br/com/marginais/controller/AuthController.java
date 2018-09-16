package br.com.marginais.controller;

import javax.faces.context.FacesContext;

import br.com.marginais.controller.util.JsfUtil;

/**
 *
 * @author thiago
 */
@SuppressWarnings("serial")
@javax.faces.bean.ManagedBean
@javax.faces.bean.SessionScoped
public class AuthController implements java.io.Serializable {

    private boolean authorized;
    private String passwd;
    private int attempts;

    public AuthController() {
        authorized = false;
        passwd = null;
    }

    public String auth() {
    	if (passwd != null && passwd.equals("admin@admin")) {
            passwd = null;
            attempts = 0;
            authorized = true;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", "authorized");
            return "/admin/?faces-redirect=true";
        } else if (passwd == null) {
            JsfUtil.addErrorMessage("É necessário informar a senha.");
        } else {
            attempts++;
            JsfUtil.addErrorMessage("Senha incorreta.");
        }
    	return null;
    }

    public String unAuth() {
        authorized = false;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        return "/auth?faces-redirect=true";
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getAttempts() {
        return attempts;
    }

}
