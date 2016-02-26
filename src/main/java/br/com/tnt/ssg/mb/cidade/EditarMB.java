package br.com.tnt.ssg.mb.cidade;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.tnt.ssg.cmt.bd.CidadeBD;
import br.com.tnt.ssg.dmp.Cidade;
import br.com.tnt.ssg.dmp.UF;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "editarCidadesMB")
@ViewScoped
public class EditarMB {

	@EJB
	private CidadeBD cidadeBD;

	private Cidade cidade;

	private List<UF> ufs;

	@PostConstruct
	public void init() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id");

		if (id != null && !id.isEmpty()) {
			try {
				cidade = cidadeBD.findById(Long.parseLong(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			cidade = new Cidade();
		}

		try {
			ufs = cidadeBD.findAllUF();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String salvar() {
		try {
			cidadeBD.save(cidade);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
				"Sucesso!");

		return "consultar?faces-redirect=true";
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public List<UF> getUfs() {
		return ufs;
	}

	public void setUfs(List<UF> ufs) {
		this.ufs = ufs;
	}

}
