package br.com.tnt.ssg.mb.md;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;

import br.com.tnt.ssg.dmp.CidadeServico;
import br.com.tnt.ssg.util.FacesUtils;

public class CidadeServicoMD {

	private CidadeServico cidadeServico = new CidadeServico();
	private List<CidadeServico> cidadesServico = new ArrayList<CidadeServico>();

	public CidadeServicoMD(List<CidadeServico> cidades) {
		this.cidadesServico = new ArrayList<CidadeServico>();
		this.cidadesServico.addAll(cidades);
	}

	public CidadeServicoMD() {

	}

	public void adicionarDetalhe() {
		if (!this.cidadesServico.contains(this.cidadeServico)) {
			this.cidadesServico.add(this.cidadeServico);
		} else {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"Cidade já adicionada!");
		}

		Collections.sort(cidadesServico);

		limparDetalhe();
	}

	public void removerDetalhe() {
		this.cidadesServico.remove(this.cidadeServico);
	}

	public void limparDetalhe() {
		this.cidadeServico = new CidadeServico();
	}

	public void limparDetalhes() {
		this.cidadesServico = new ArrayList<CidadeServico>();
	}

	public CidadeServico getCidadeServico() {
		return cidadeServico;
	}

	public void setCidadeServico(CidadeServico cidadeServico) {
		this.cidadeServico = cidadeServico;
	}

	public List<CidadeServico> getCidadesServico() {
		return cidadesServico;
	}

	public void setCidadesServico(List<CidadeServico> cidadesServico) {
		this.cidadesServico = cidadesServico;
	}

}
