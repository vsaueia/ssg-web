package br.com.tnt.ssg.mb.uf;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.tnt.ssg.cmt.bd.UFBD;
import br.com.tnt.ssg.dmp.UF;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "editarUFsMB")
@ViewScoped
public class EditarMB {

	@EJB
	private UFBD ufBD;

	private UF uf;

	@PostConstruct
	public void init() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id");

		if (id != null && !id.isEmpty()) {
			try {
				uf = ufBD.findById(Long.parseLong(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			uf = new UF();
		}
	}

	public String salvar() {
		try {
			ufBD.save(uf);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
				"Sucesso!");

		return "consultar?faces-redirect=true";
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}

}
