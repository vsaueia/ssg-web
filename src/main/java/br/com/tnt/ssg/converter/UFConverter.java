package br.com.tnt.ssg.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.NamingException;

import br.com.tnt.ssg.cmt.bd.UFBD;
import br.com.tnt.ssg.dmp.UF;
import br.com.tnt.ssg.util.EjbServiceLocator;

@FacesConverter(forClass = UF.class)
public class UFConverter implements Converter {

	private UFBD ufBD;

	public UFConverter() {
		EjbServiceLocator serviceLocator = new EjbServiceLocator();
		try {
			ufBD = (UFBD) serviceLocator.lookup("java:global/ssg-web/UFBD");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String id) {
		Long ufId = Long.parseLong(id);

		UF uf = null;
		try {
			uf = ufBD.findById(ufId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return uf;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj != null && obj instanceof UF) {
			UF uf = (UF) obj;

			if (uf.getId() != null) {
				return uf.getId().toString();
			}
		}

		return null;
	}

}
