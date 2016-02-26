package br.com.tnt.ssg.mb;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "loginMB")
@RequestScoped
public class LoginMB {

	@PostConstruct
	public void init() {

	}

	public String loginSignup() {

		return "/paginas/signup/cliente";
	}

	public String logout() {
		FacesContext ctx = FacesContext.getCurrentInstance();

		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(false);
		session.invalidate();

		return "/paginas/login/login";
	}

	public static String getLogin() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getUserPrincipal().getName();
	}
}
