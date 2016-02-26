package br.com.tnt.ssg.mb.categoria;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.tnt.ssg.cmt.bd.CategoriaBD;
import br.com.tnt.ssg.dmp.Categoria;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "editarCategoriasMB")
@ViewScoped
public class EditarMB {

	@EJB
	private CategoriaBD categoriaBD;

	private Categoria categoria;

	@PostConstruct
	public void init() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id");

		if (id != null && !id.isEmpty()) {
			try {
				categoria = categoriaBD.findById(Long.parseLong(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			categoria = new Categoria();
		}
	}

	public String salvar() {
		try {
			categoriaBD.save(categoria);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
				"Sucesso!");

		return "consultar?faces-redirect=true";
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}
