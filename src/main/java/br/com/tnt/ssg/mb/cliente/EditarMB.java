package br.com.tnt.ssg.mb.cliente;

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

import br.com.tnt.ssg.cmt.bd.ClienteBD;
import br.com.tnt.ssg.dmp.Cliente;
import br.com.tnt.ssg.dmp.Contato;
import br.com.tnt.ssg.enums.TipoContato;
import br.com.tnt.ssg.mb.md.ContatoMD;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "editarClientesMB")
@ViewScoped
public class EditarMB {

	@EJB
	private ClienteBD clienteBD;

	private Cliente cliente;

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
				cliente = clienteBD.findById(Long.parseLong(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			cliente = new Cliente();
		}

		tiposContato = new ArrayList<>();

		this.contatoMD = new ContatoMD();

		this.tiposContato = FacesUtils.enumSelectItens(TipoContato.class);
	}

	public String salvar() {
		if (!verificarCliente()) {
			return "";
		}

		cliente.setCpf(cliente.getCpf().replaceAll("\\D", ""));

		List<Contato> contatos = contatoMD.getContatos();

		for (Contato contato : contatos) {
			contato.setCliente(cliente);
		}

		cliente.setContatos(contatos);

		try {
			clienteBD.save(cliente);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		try {
			request.login(cliente.getUsuario().getLogin(), cliente.getUsuario()
					.getSenha());
		} catch (ServletException e) {
			e.printStackTrace();
		}

		return "/index?faces-redirect=true";
	}

	private boolean verificarCliente() {
		if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar o nome!");

			return false;
		}
		if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar o CPF!");

			return false;
		}
		if (cliente.getUsuario().getLogin() == null
				|| cliente.getUsuario().getLogin().trim().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar o login!");

			return false;
		}
		if (cliente.getUsuario().getSenha() == null
				|| cliente.getUsuario().getSenha().trim().isEmpty()) {
			FacesUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
					"É preciso informar a senha!");

			return false;
		}
		if (!cliente.getUsuario().getSenha().equals(confirmacao)) {
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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getConfirmacao() {
		return confirmacao;
	}

	public void setConfirmacao(String confirmacao) {
		this.confirmacao = confirmacao;
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

	public ClienteBD getClienteBD() {
		return clienteBD;
	}

	public void setClienteBD(ClienteBD clienteBD) {
		this.clienteBD = clienteBD;
	}

	public ContatoMD getContatoMD() {
		return contatoMD;
	}

	public void setContatoMD(ContatoMD contatoMD) {
		this.contatoMD = contatoMD;
	}
}
