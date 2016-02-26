package br.com.tnt.ssg.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.NamingException;

import br.com.tnt.ssg.cmt.bd.CidadeBD;
import br.com.tnt.ssg.dmp.Cidade;
import br.com.tnt.ssg.util.EjbServiceLocator;

@FacesConverter(forClass = Cidade.class)
public class CidadeConverter implements Converter {

	private CidadeBD cidadeBD;

	public CidadeConverter() {
		EjbServiceLocator serviceLocator = new EjbServiceLocator();
		try {
			cidadeBD = (CidadeBD) serviceLocator
					.lookup("java:global/ssg-web/CidadeBD");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String id) {
		Long cidadeId = Long.parseLong(id);

		Cidade cidade = null;
		try {
			cidade = cidadeBD.findById(cidadeId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cidade;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj != null && obj instanceof Cidade) {
			Cidade cidade = (Cidade) obj;

			if (cidade.getId() != null) {
				return cidade.getId().toString();
			}
		}

		return null;
	}

}
