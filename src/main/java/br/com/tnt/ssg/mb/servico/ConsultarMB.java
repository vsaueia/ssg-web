package br.com.tnt.ssg.mb.servico;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.tnt.ssg.cmt.bd.CidadeBD;
import br.com.tnt.ssg.cmt.bd.ServicoBD;
import br.com.tnt.ssg.dmp.Autonomo;
import br.com.tnt.ssg.dmp.Categoria;
import br.com.tnt.ssg.dmp.Cidade;
import br.com.tnt.ssg.dmp.Servico;
import br.com.tnt.ssg.dmp.UF;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "consultarServicosMB")
@ViewScoped
public class ConsultarMB {

	@EJB
	private ServicoBD servicoBD;

	private Servico servico = new Servico();

	private Servico search = new Servico();

	private List<Servico> servicos;

	private List<Categoria> categorias;

	private List<Autonomo> autonomos;

	@PostConstruct
	public void init() {
		try {
			categorias = servicoBD.findAllCategoria();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			autonomos = servicoBD.findAllAutonomo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ufs = cidadeBD.findAllUF();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void consultar() {
		try {
			this.servicos = servicoBD.findAll(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		this.search = new Servico();
		this.servicos = new ArrayList<>();
	}

	public void excluir() {
		try {
			servicoBD.remove(servico);
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

	public List<Servico> getServicos() {
		return servicos;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public Servico getSearch() {
		return search;
	}

	public void setSearch(Servico search) {
		this.search = search;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Autonomo> getAutonomos() {
		return autonomos;
	}

	// mudar de lugar
	private List<UF> ufs = new ArrayList<>();
	private List<Cidade> cidades = new ArrayList<>();

	@EJB
	private CidadeBD cidadeBD;

	public void buscarCidades() {
		Cidade criteria = new Cidade();
		criteria.setUf(search.getUf());
		try {
			cidades = cidadeBD.findAll(criteria);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<UF> getUfs() {
		return ufs;
	}

	public void setUfs(List<UF> ufs) {
		this.ufs = ufs;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public CidadeBD getCidadeBD() {
		return cidadeBD;
	}

	public void setCidadeBD(CidadeBD cidadeBD) {
		this.cidadeBD = cidadeBD;
	}
}
