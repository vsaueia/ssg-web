package br.com.tnt.ssg.mb.servico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.tnt.ssg.cmt.bd.AvaliacaoBD;
import br.com.tnt.ssg.cmt.bd.ClienteBD;
import br.com.tnt.ssg.cmt.bd.ServicoBD;
import br.com.tnt.ssg.cmt.bd.SolicitacaoServicoBD;
import br.com.tnt.ssg.cmt.dao.UsuarioDAO;
import br.com.tnt.ssg.dmp.Avaliacao;
import br.com.tnt.ssg.dmp.Cliente;
import br.com.tnt.ssg.dmp.Servico;
import br.com.tnt.ssg.dmp.SolicitacaoServico;
import br.com.tnt.ssg.dmp.Usuario;
import br.com.tnt.ssg.enums.SituacaoServico;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "solicitarServicosMB")
@ViewScoped
public class SolicitarMB {

	@EJB
	private ServicoBD servicoBD;

	@EJB
	private SolicitacaoServicoBD solicitacaoServicoBD;

	@EJB
	private ClienteBD clienteBD;

	@EJB
	private UsuarioDAO usaurioDAO;

	@EJB
	private AvaliacaoBD avaliacaoBD;

	private Servico servico;

	private SituacaoServico situacaoServico;

	private SolicitacaoServico solicitacaoServico;

	private List<SolicitacaoServico> solicitacoes = new ArrayList<>();

	private Avaliacao avaliacao = new Avaliacao();

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
		}

		String idSolicitacao = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap()
				.get("idSolicitacao");
		if (idSolicitacao != null && !idSolicitacao.isEmpty()) {
			try {
				solicitacaoServico = solicitacaoServicoBD.findById(Long
						.parseLong(idSolicitacao));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				avaliacao = avaliacaoBD.findBySolicitacao(solicitacaoServico);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (avaliacao == null) {
				avaliacao = new Avaliacao();
			}
		}

	}

	public void consultar() {
		Usuario criteria = new Usuario();
		criteria.setLogin(FacesContext.getCurrentInstance()
				.getExternalContext().getUserPrincipal().getName());
		Usuario usuario;
		try {
			usuario = usaurioDAO.findAll(criteria).get(0);

			Cliente cliente = clienteBD.findByUsuario(usuario);

			this.solicitacoes = solicitacaoServicoBD.findAll(situacaoServico,
					cliente);
		} catch (Exception e) {
			e.printStackTrace();
		} // login deve ser
			// único

	}

	public void limpar() {
		this.situacaoServico = null;
	}

	public void excluir() {
		try {
			solicitacaoServicoBD.remove(solicitacaoServico);
		} catch (Exception e) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"Erro!");

			return;
		}

		FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
				"Remoção com sucesso!");

		consultar();
	}

	public String confirmar() {

		Usuario criteria = new Usuario();
		criteria.setLogin(FacesContext.getCurrentInstance()
				.getExternalContext().getUserPrincipal().getName());
		Usuario usuario;
		try {
			usuario = usaurioDAO.findAll(criteria).get(0);

			Cliente cliente = clienteBD.findByUsuario(usuario);

			SolicitacaoServico solicitacaoServico = new SolicitacaoServico();
			solicitacaoServico.setCliente(cliente);
			solicitacaoServico.setDataSolicitacao(new Date());
			solicitacaoServico.setServico(servico);
			solicitacaoServico.setSituacaoServico(SituacaoServico.SOLICITADO);
		} catch (Exception e1) {
			e1.printStackTrace();
		} // login deve ser
			// único

		try {
			solicitacaoServicoBD.save(solicitacaoServico);

			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
					"Sucesso!");

			return "consultar?faces-redirect=true";
		} catch (Exception e) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"Erro!");
		}
		return null;
	}

	public String confirmarAvaliacao() {

		avaliacao.setSolicitacaoServico(solicitacaoServico);
		avaliacao.setDataAvaliacao(new Date());

		try {
			avaliacaoBD.save(avaliacao);

			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
							"Sucesso!"));

			return "solicitacoesCliente?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
							"Erro!"));
		}
		return null;
	}

	public Integer getNotaFromSolicitacao(SolicitacaoServico solicitacao) {
		try {
			return avaliacaoBD.findBySolicitacao(solicitacao).getNota();
		} catch (Exception e) {
			return null;
		}
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public SituacaoServico getSituacaoServico() {
		return situacaoServico;
	}

	public void setSituacaoServico(SituacaoServico situacaoServico) {
		this.situacaoServico = situacaoServico;
	}

	public List<SituacaoServico> getSituacoes() {
		return Arrays.asList(SituacaoServico.values());
	}

	public List<SolicitacaoServico> getSolicitacoes() {
		return solicitacoes;
	}

	public void setSolicitacoes(List<SolicitacaoServico> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}

	public SolicitacaoServico getSolicitacaoServico() {
		return solicitacaoServico;
	}

	public void setSolicitacaoServico(SolicitacaoServico solicitacaoServico) {
		this.solicitacaoServico = solicitacaoServico;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}
}
