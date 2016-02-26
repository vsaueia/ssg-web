package br.com.tnt.ssg.mb.autonomo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import br.com.tnt.ssg.cmt.bd.AutonomoBD;
import br.com.tnt.ssg.dmp.Autonomo;
import br.com.tnt.ssg.dmp.Contato;
import br.com.tnt.ssg.enums.TipoContato;
import br.com.tnt.ssg.mb.md.ContatoMD;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "editarAutonomosMB")
@ViewScoped
public class EditarMB {

	@EJB
	private AutonomoBD autonomoBD;

	private Autonomo autonomo;

	private Contato contato;

	private ContatoMD contatoMD;

	private List<SelectItem> tiposContato;

	private String confirmacao;

	@PostConstruct
	public void init() {
		String id = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("id");

		if (id != null && !id.isEmpty()) {
			try {
				autonomo = autonomoBD.findById(Long.parseLong(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			autonomo = new Autonomo();
		}

		tiposContato = new ArrayList<>();

		this.contatoMD = new ContatoMD();

		this.tiposContato = FacesUtils.enumSelectItens(TipoContato.class);
	}

	private boolean verificarAutonomo() {
		if (autonomo.getNomeFantasia() == null
				|| autonomo.getNomeFantasia().trim().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar o nomeFantasia!");

			return false;
		}
		if (autonomo.getCpf() == null || autonomo.getCpf().trim().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar o cpf!");

			return false;
		}
		if (autonomo.getUsuario().getLogin() == null
				|| autonomo.getUsuario().getLogin().trim().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar o login!");

			return false;
		}
		if (autonomo.getUsuario().getSenha() == null
				|| autonomo.getUsuario().getSenha().trim().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar a senha!");

			return false;
		}
		if (!autonomo.getUsuario().getSenha().equals(confirmacao)) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"Senhas não conferem!");

			return false;
		}
		if (contatoMD.getContatos().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar pelo menos um contato!");

			return false;
		}
		return true;
	}

	public String salvar() {
		if (!verificarAutonomo()) {
			return "";
		}

		autonomo.setCpf(autonomo.getCpf().replaceAll("\\D", ""));

		List<Contato> contatos = contatoMD.getContatos();

		for (Contato contato : contatos) {
			contato.setAutonomo(autonomo);
		}

		autonomo.setContatos(contatos);

		try {
			autonomoBD.save(autonomo);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		try {
			request.login(autonomo.getUsuario().getLogin(), autonomo
					.getUsuario().getSenha());
		} catch (ServletException e) {
			e.printStackTrace();
		}

		return "/index?faces-redirect=true";
	}

	public Autonomo getAutonomo() {
		return autonomo;
	}

	public void setAutonomo(Autonomo autonomo) {
		this.autonomo = autonomo;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public List<SelectItem> getTiposContato() {
		return tiposContato;
	}

	public void setTiposContato(List<SelectItem> tiposContato) {
		this.tiposContato = tiposContato;
	}

	public ContatoMD getContatoMD() {
		return contatoMD;
	}

	public void setContatoMD(ContatoMD contatoMD) {
		this.contatoMD = contatoMD;
	}

	public String getConfirmacao() {
		return confirmacao;
	}

	public void setConfirmacao(String confirmacao) {
		this.confirmacao = confirmacao;
	}

}
