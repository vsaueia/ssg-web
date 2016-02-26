package br.com.tnt.ssg.mb.cidade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.tnt.ssg.cmt.bd.CidadeBD;
import br.com.tnt.ssg.dmp.Cidade;
import br.com.tnt.ssg.dmp.UF;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "consultarCidadesMB")
@ViewScoped
public class ConsultarMB {

	@EJB
	private CidadeBD cidadeBD;

	private Cidade cidade = new Cidade();

	private Cidade search = new Cidade();

	private List<Cidade> cidades;

	private List<UF> ufs;

	@PostConstruct
	public void init() {
		try {
			ufs = cidadeBD.findAllUF();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void consultar() {
		try {
			this.cidades = cidadeBD.findAll(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		this.search = new Cidade();
		this.cidades = new ArrayList<>();
	}

	public void excluir() {
		try {
			cidadeBD.remove(cidade);
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

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Cidade getSearch() {
		return search;
	}

	public void setSearch(Cidade search) {
		this.search = search;
	}

	public List<UF> getUfs() {
		return ufs;
	}

	public void setUfs(List<UF> ufs) {
		this.ufs = ufs;
	}

}
