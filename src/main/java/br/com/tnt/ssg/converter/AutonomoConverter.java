package br.com.tnt.ssg.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.NamingException;

import br.com.tnt.ssg.cmt.bd.AutonomoBD;
import br.com.tnt.ssg.dmp.Autonomo;
import br.com.tnt.ssg.util.EjbServiceLocator;

@FacesConverter(forClass = Autonomo.class)
public class AutonomoConverter implements Converter {

	private AutonomoBD autonomoBD;

	public AutonomoConverter() {
		EjbServiceLocator serviceLocator = new EjbServiceLocator();
		try {
			autonomoBD = (AutonomoBD) serviceLocator
					.lookup("java:global/ssg-web/AutonomoBD");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String id) {
		Autonomo autonomo = new Autonomo();
		autonomo.setId(Long.parseLong(id));

		return autonomo;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj != null && obj instanceof Autonomo) {
			Autonomo autonomo = (Autonomo) obj;

			if (autonomo.getId() != null) {
				return autonomo.getId().toString();
			}
		}

		return null;
	}

}
