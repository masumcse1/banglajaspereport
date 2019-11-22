package com.report;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Named
@RequestScoped
public class ReportController {

	@Resource(lookup ="java:/afs_ds")
	private DataSource ds;

	ResultSet resultset;

	private String accountNo;

	public void getReport() throws Exception {

		Map reportParameter = new HashMap<>();
		reportParameter.put("accountNo", accountNo);
		System.out.println("do report---------JRRestultSetDataSource-----");

		String reportName = "/report/loansample.jasper";

		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String actualPath = servletContext.getRealPath(reportName);

		JasperReport jasperReport = (JasperReport) JRLoader
				.loadObjectFromFile(actualPath);
		Statement stmt = ds.getConnection().createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		String reportsql = "select * from loaninfo";
		resultset = stmt.executeQuery(reportsql);

		resultset.last();
		if (resultset.getRow() != 0) {
			resultset.beforeFirst();
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, reportParameter, new JRResultSetDataSource(
							resultset));
			 doPDF(jasperPrint);

		} else {

		}

	}

	public void doPDF(JasperPrint jasperPrint) throws Exception {

		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();

		httpServletResponse.setContentType("application/pdf");
		httpServletResponse.addHeader("Content-disposition","attachment;filename=abcreport.pdf");
		ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
		FacesContext.getCurrentInstance().responseComplete();
	}

	public ResultSet getResultset() {
		return resultset;
	}

	public void setResultset(ResultSet resultset) {
		this.resultset = resultset;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

}
