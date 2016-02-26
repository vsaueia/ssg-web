package br.com.tnt.ssg.mb.autonomo;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.tnt.ssg.cmt.bd.AutonomoBD;
import br.com.tnt.ssg.dmp.Autonomo;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "consultarAutonomosMB")
@ViewScoped
public class ConsultarMB {

	@EJB
	private AutonomoBD autonomoBD;

	private Autonomo autonomo = new Autonomo();

	private Autonomo search = new Autonomo();

	private List<Autonomo> autonomos;

	public void consultar() {
		try {
			this.autonomos = autonomoBD.findAll(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		this.search = new Autonomo();
		this.autonomos = new ArrayList<>();
	}

	public void excluir() {
		try {
			autonomoBD.remove(autonomo);
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

	public List<Autonomo> getAutonomos() {
		return autonomos;
	}

	public void setAutonomos(List<Autonomo> autonomos) {
		this.autonomos = autonomos;
	}

	public Autonomo getAutonomo() {
		return autonomo;
	}

	public void setAutonomo(Autonomo autonomo) {
		this.autonomo = autonomo;
	}

	public Autonomo getSearch() {
		return search;
	}

	public void setSearch(Autonomo search) {
		this.search = search;
	}
}
