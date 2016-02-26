package br.com.tnt.ssg.mb.md;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.com.tnt.ssg.dmp.Contato;
import br.com.tnt.ssg.util.FacesUtils;

public class ContatoMD {

	private Contato contato = new Contato();
	private List<Contato> contatos = new ArrayList<Contato>();

	public ContatoMD(List<Contato> contatos) {
		this.contatos = new ArrayList<Contato>();
		this.contatos.addAll(contatos);
	}

	public ContatoMD() {

	}

	public void adicionarDetalhe() {
		if (!this.contatos.contains(this.contato)) {
			this.contatos.add(this.contato);
		} else {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_INFO,
									null,
									FacesUtils
											.getMessageByKey("editar.empresa.estadoAtuacao.duplicado")));
		}

		Collections.sort(contatos);

		limparDetalhe();
	}

	public void removerDetalhe() {
		this.contatos.remove(this.contato);
	}

	public void limparDetalhe() {
		this.contato = new Contato();
	}

	public void limparDetalhes() {
		this.contatos = new ArrayList<Contato>();
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}
}
