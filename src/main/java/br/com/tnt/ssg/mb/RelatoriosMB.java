package br.com.tnt.ssg.mb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.tnt.ssg.cmt.bd.RelatoriosBD;
import br.com.tnt.ssg.dmp.Autonomo;
import br.com.tnt.ssg.util.JasperUtils;

@ManagedBean(name = "relatoriosMB")
@ViewScoped
public class RelatoriosMB {

	@EJB
	private RelatoriosBD relatoriosBD;

	private byte[] relatorioAgendamentos;

	public void gerarRelatorioAgendamentos() {
		try {
			Autonomo autonomo = relatoriosBD.getAutonomoByLogin(LoginMB
					.getLogin());

			this.relatorioAgendamentos = relatoriosBD
					.buildAgendaReport(autonomo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exibirRelatorioAgendamentos() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		String formattedDate = sdf.format(new Date());

		try {
			JasperUtils.exibirPdf(FacesContext.getCurrentInstance(),
					relatorioAgendamentos, "agendamentos_" + formattedDate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
