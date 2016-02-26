package br.com.tnt.ssg.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.NamingException;

import br.com.tnt.ssg.cmt.bd.CategoriaBD;
import br.com.tnt.ssg.dmp.Categoria;
import br.com.tnt.ssg.util.EjbServiceLocator;

@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter implements Converter {

	private CategoriaBD categoriaBD;

	public CategoriaConverter() {
		EjbServiceLocator serviceLocator = new EjbServiceLocator();
		try {
			categoriaBD = (CategoriaBD) serviceLocator
					.lookup("java:global/ssg-web/CategoriaBD");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String id) {
		Long categoriaId = Long.parseLong(id);

		Categoria categoria = null;
		try {
			categoria = categoriaBD.findById(categoriaId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return categoria;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj != null && obj instanceof Categoria) {
			Categoria categoria = (Categoria) obj;

			if (categoria.getId() != null) {
				return categoria.getId().toString();
			}
		}

		return null;
	}

}
