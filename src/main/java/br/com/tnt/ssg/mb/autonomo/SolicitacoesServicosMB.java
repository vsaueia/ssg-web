package br.com.tnt.ssg.mb.autonomo;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import br.com.tnt.ssg.cmt.bd.SolicitacaoServicoBD;
import br.com.tnt.ssg.dmp.AgendamentoServico;
import br.com.tnt.ssg.dmp.Autonomo;
import br.com.tnt.ssg.dmp.SolicitacaoServico;
import br.com.tnt.ssg.enums.SituacaoServico;
import br.com.tnt.ssg.mb.LoginMB;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "solicitacoesServicosMB")
@ViewScoped
public class SolicitacoesServicosMB {

	@EJB
	private SolicitacaoServicoBD solicitacaoServicoBD;

	private List<SolicitacaoServico> solicitacoesServicos;

	private SolicitacaoServico solicitacaoServico;

	private AgendamentoServico agendamentoServico;

	private SituacaoServico situacao = SituacaoServico.SOLICITADO;

	private List<SelectItem> situacoes;

	@PostConstruct
	public void init() {
		consultarSolicitacoesServicos(SituacaoServico.SOLICITADO);

		situacoes = FacesUtils.enumSelectItens(SituacaoServico.class);
	}

	private void consultarSolicitacoesServicos(SituacaoServico situacaoServico) {
		String login = LoginMB.getLogin();

		try {
			Autonomo autonomo = solicitacaoServicoBD.findAutonomoByLogin(login);

			SolicitacaoServico criteria = new SolicitacaoServico();
			criteria.setAutonomo(autonomo);
			criteria.setSituacaoServico(situacaoServico);

			solicitacoesServicos = solicitacaoServicoBD.findAll(criteria);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void consultarSolicitacoesPorSituacao() {
		consultarSolicitacoesServicos(situacao);
	}

	public void novoAgendamento() {
		agendamentoServico = new AgendamentoServico();
	}

	public void agendarServico() {

		try {
			this.solicitacaoServico
					.setSituacaoServico(SituacaoServico.AGENDADO);

			solicitacaoServicoBD.save(solicitacaoServico);

			agendamentoServico.setDataCriacao(new Date());
			agendamentoServico.setSolicitacaoServico(solicitacaoServico);

			solicitacaoServicoBD.save(agendamentoServico);

			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
					"Agendado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		consultarSolicitacoesServicos(SituacaoServico.SOLICITADO);
	}

	public void finalizarServico() {
		this.solicitacaoServico.setSituacaoServico(SituacaoServico.EXECUTADO);

		try {
			solicitacaoServicoBD.save(solicitacaoServico);

			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
					"Finalizado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		consultarSolicitacoesServicos(SituacaoServico.SOLICITADO);
	}

	public List<SolicitacaoServico> getSolicitacoesServicos() {
		return solicitacoesServicos;
	}

	public void setSolicitacoesServicos(
			List<SolicitacaoServico> solicitacoesServicos) {
		this.solicitacoesServicos = solicitacoesServicos;
	}

	public SolicitacaoServico getSolicitacaoServico() {
		return solicitacaoServico;
	}

	public void setSolicitacaoServico(SolicitacaoServico solicitacaoServico) {
		this.solicitacaoServico = solicitacaoServico;
	}

	public AgendamentoServico getAgendamentoServico() {
		return agendamentoServico;
	}

	public void setAgendamentoServico(AgendamentoServico agendamentoServico) {
		this.agendamentoServico = agendamentoServico;
	}

	public SituacaoServico getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoServico situacao) {
		this.situacao = situacao;
	}

	public List<SelectItem> getSituacoes() {
		return situacoes;
	}

	public void setSituacoes(List<SelectItem> situacoes) {
		this.situacoes = situacoes;
	}

}
