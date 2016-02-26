package br.com.tnt.ssg.mb.servico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.tnt.ssg.cmt.bd.AvaliacaoBD;
import br.com.tnt.ssg.cmt.bd.ServicoBD;
import br.com.tnt.ssg.cmt.bd.SolicitacaoServicoBD;
import br.com.tnt.ssg.dmp.Servico;
import br.com.tnt.ssg.dmp.SolicitacaoServico;
import br.com.tnt.ssg.enums.SituacaoServico;

@ManagedBean(name = "historicoMB")
@ViewScoped
public class HistoricoMB {

	@EJB
	private ServicoBD servicoBD;

	@EJB
	private SolicitacaoServicoBD solicitacaoServicoBD;

	@EJB
	private AvaliacaoBD avaliacaoBD;

	private List<SolicitacaoServico> solicitacoes = new ArrayList<>();

	Servico servico = null;

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

		try {
			solicitacoes = solicitacaoServicoBD
					.findAllExecutadosByServico(servico);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getNotaFromSolicitacao(SolicitacaoServico solicitacao) {
		try {
			return avaliacaoBD.findBySolicitacao(solicitacao).getNota();
		} catch (Exception e) {
			return null;
		}
	}

	public String shortComentarioFromSolicitacao(SolicitacaoServico solicitacao) {
		try {
			String comentario = avaliacaoBD.findBySolicitacao(solicitacao)
					.getComentario();
			return (comentario.length() <= 30 ? comentario : comentario
					.substring(0, 30) + "...");
		} catch (Exception e) {
			return null;
		}
	}

	public String comentarioFromSolicitacao(SolicitacaoServico solicitacao) {
		try {
			return avaliacaoBD.findBySolicitacao(solicitacao).getComentario();
		} catch (Exception e) {
			return null;
		}
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

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

}
