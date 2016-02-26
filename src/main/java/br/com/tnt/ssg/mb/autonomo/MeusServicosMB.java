package br.com.tnt.ssg.mb.autonomo;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.tnt.ssg.cmt.bd.ServicoBD;
import br.com.tnt.ssg.dmp.Autonomo;
import br.com.tnt.ssg.dmp.Servico;
import br.com.tnt.ssg.exceptions.ServicoException;
import br.com.tnt.ssg.mb.LoginMB;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "meusServicosMB")
@ViewScoped
public class MeusServicosMB {

	@EJB
	private ServicoBD servicoBD;

	private List<Servico> servicos;

	private Servico servico;

	@PostConstruct
	public void init() {
		consultar();
	}

	private void consultar() {
		String login = LoginMB.getLogin();

		try {
			Autonomo autonomo = servicoBD.findAutonomoByLogin(login);
			Servico criteria = new Servico();
			criteria.setAutonomo(autonomo);

			servicos = servicoBD.findAll(criteria);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void excluir() {
		try {
			servicoBD.remove(servico);

			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
					"Remoção com sucesso");

			consultar();
		} catch (Exception e) {
			if (e instanceof ServicoException) {
				ServicoException e1 = (ServicoException) e;
				if (e1.isServicoComSolicitacoes()) {
					FacesUtils
							.addFacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erro!",
									"Serviço não pode ser removido por estar associado a solicitações!");
				}
			}
		}
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}
}
