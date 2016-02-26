package br.com.tnt.ssg.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.NamingException;

import br.com.tnt.ssg.cmt.bd.ServicoBD;
import br.com.tnt.ssg.dmp.Servico;
import br.com.tnt.ssg.util.EjbServiceLocator;

@FacesConverter(forClass = Servico.class)
public class ServicoConverter implements Converter {

	private ServicoBD servicoBD;

	public ServicoConverter() {
		EjbServiceLocator serviceLocator = new EjbServiceLocator();
		try {
			servicoBD = (ServicoBD) serviceLocator
					.lookup("java:global/ssg-web/ServicoBD");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String id) {
		Long servicoId = Long.parseLong(id);

		Servico servico = null;
		try {
			servico = servicoBD.findById(servicoId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return servico;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object obj) {
		if (obj != null && obj instanceof Servico) {
			Servico servico = (Servico) obj;

			if (servico.getId() != null) {
				return servico.getId().toString();
			}
		}

		return null;
	}

}
