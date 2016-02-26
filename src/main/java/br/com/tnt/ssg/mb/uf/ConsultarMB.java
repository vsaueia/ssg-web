package br.com.tnt.ssg.mb.uf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.tnt.ssg.cmt.bd.UFBD;
import br.com.tnt.ssg.dmp.UF;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "consultarUFsMB")
@ViewScoped
public class ConsultarMB {

	@EJB
	private UFBD ufBD;

	private UF uf = new UF();

	private UF search = new UF();

	private List<UF> ufs;

	@PostConstruct
	public void init() {
	}

	public void consultar() {
		try {
			this.ufs = ufBD.findAll(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		this.search = new UF();
		this.ufs = new ArrayList<>();
	}

	public void excluir() {
		try {
			ufBD.remove(uf);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
				"Remoção com sucesso!");

		consultar();
	}

	public String editar() {
		return "editar";
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}

	public UF getSearch() {
		return search;
	}

	public void setSearch(UF search) {
		this.search = search;
	}

	public List<UF> getUfs() {
		return ufs;
	}

	public void setUfs(List<UF> ufs) {
		this.ufs = ufs;
	}

}
