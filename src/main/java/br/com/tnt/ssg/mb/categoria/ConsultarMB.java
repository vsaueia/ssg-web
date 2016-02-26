package br.com.tnt.ssg.mb.categoria;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.tnt.ssg.cmt.bd.CategoriaBD;
import br.com.tnt.ssg.dmp.Categoria;
import br.com.tnt.ssg.exceptions.CategoriaException;
import br.com.tnt.ssg.util.FacesUtils;

@ManagedBean(name = "consultarCategoriasMB")
@ViewScoped
public class ConsultarMB {

	@EJB
	private CategoriaBD categoriaBD;

	private Categoria categoria = new Categoria();

	private Categoria search = new Categoria();

	private List<Categoria> categorias;

	public void consultar() {
		try {
			this.categorias = categoriaBD.findAll(search);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		this.search = new Categoria();
		this.categorias = new ArrayList<>();
	}

	public void excluir() {
		try {
			categoriaBD.remove(categoria);
		} catch (Exception e) {
			if (e instanceof CategoriaException) {
				CategoriaException e1 = (CategoriaException) e;

				if (e1.isCategoriaComServicos()) {
					FacesUtils
							.addFacesMessage(FacesMessage.SEVERITY_ERROR,
									"Erro!",
									"Categoria não pode ser removida por estar associada a serviços!");

					return;
				}
			}
		}

		FacesUtils.addFacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!",
				"Remoção com sucesso!");

		consultar();
	}

	public String editar() {
		return "editar";
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Categoria getSearch() {
		return search;
	}

	public void setSearch(Categoria search) {
		this.search = search;
	}
}
