package br.com.tnt.ssg.mb.servico;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.com.tnt.ssg.cmt.bd.ServicoBD;
import br.com.tnt.ssg.dmp.Autonomo;
import br.com.tnt.ssg.dmp.Categoria;
import br.com.tnt.ssg.dmp.Cidade;
import br.com.tnt.ssg.dmp.CidadeServico;
import br.com.tnt.ssg.dmp.Servico;
import br.com.tnt.ssg.dmp.UF;
import br.com.tnt.ssg.mb.md.CidadeServicoMD;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "editarServicosMB")
@ViewScoped
public class EditarMB {

	@EJB
	private ServicoBD servicoBD;

	private Servico servico;

	private List<Categoria> categorias;

	private CidadeServicoMD cidadeServicoMD;

	private List<UF> ufs;

	private UF uf;

	private List<Cidade> cidades;

	@PostConstruct
	public void init() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id");

		if (id != null && !id.isEmpty()) {
			try {
				servico = servicoBD.findByIdWithCidades(Long.parseLong(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.cidadeServicoMD = new CidadeServicoMD(
					servico.getCidadeServicoList());
		} else {
			servico = new Servico();
			this.cidadeServicoMD = new CidadeServicoMD();
		}

		try {
			categorias = servicoBD.findAllCategoria();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.ufs = servicoBD.findAllUF();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String salvar() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		String login = request.getUserPrincipal().getName();

		try {
			Autonomo autonomo = servicoBD.findAutonomoByLogin(login);

			servico.setAutonomo(autonomo);

			for (CidadeServico cidadeServico : cidadeServicoMD
					.getCidadesServico()) {
				cidadeServico.setServico(servico);
			}

			servico.setCidadeServicoList(cidadeServicoMD.getCidadesServico());

			servicoBD.save(servico);

			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
					"Sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/paginas/autonomo/meusServicos?faces-redirect=true";
	}

	public void buscarCidades() {
		try {
			this.cidades = this.servicoBD.findByUF(uf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adicionarDetalhe() {
		this.cidadeServicoMD.adicionarDetalhe();
		this.uf = null;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public CidadeServicoMD getCidadeServicoMD() {
		return cidadeServicoMD;
	}

	public void setCidadeServicoMD(CidadeServicoMD cidadeServicoMD) {
		this.cidadeServicoMD = cidadeServicoMD;
	}

	public List<UF> getUfs() {
		return ufs;
	}

	public void setUfs(List<UF> ufs) {
		this.ufs = ufs;
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

}
