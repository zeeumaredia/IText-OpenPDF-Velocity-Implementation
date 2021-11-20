package com.zeeumaredia.pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class ITextVelocityGenerator {
	final private static String timeStamp = new SimpleDateFormat("hh_mm_ss").format(Calendar.getInstance().getTime());
	private static final String VM_TEMPLATE_PATH = "resources/vmtemplates/velocityTemplate.vm";
	private static final String PDF_OUTPUT = "output/iText/iTextPDF_" + timeStamp + ".pdf";

	public static void main(String[] args) {
		try {
			ITextVelocityGenerator htmlToPdf = new ITextVelocityGenerator();
			htmlToPdf.velocityTemplateGeneration(VM_TEMPLATE_PATH);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void velocityTemplateGeneration(String templatePath) throws Exception {
		// Initialize Essentials, Get VM Template
		VelocityEngine ve = new VelocityEngine();
		Template t = ve.getTemplate(templatePath, "UTF-8");
		ve.init();
		VelocityContext context = new VelocityContext();

		// Assign Key their Value Data.
		context.put("username", "Zeeshan Zulfiqar");

		// Merge The Context and Writer
		Writer writer = new StringWriter();
		t.merge(context, writer);

		generatePdf(writer.toString());
	}

	public void generatePdf(String html) {

		PdfWriter pdfWriter = null;

		// create a new document
		Document document = new Document();
		try {

			document = new Document();
			document.setPageSize(PageSize.A4);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			pdfWriter = PdfWriter.getInstance(document, baos);

			// open document
			document.open();

			XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
			xmlWorkerHelper.getDefaultCssResolver(true);
			xmlWorkerHelper.parseXHtml(pdfWriter, document, new StringReader(html));
			// close the document
			document.close();

			try (OutputStream outputStream = new FileOutputStream(PDF_OUTPUT)) {
				baos.writeTo(outputStream);
			}
			baos.flush();
			baos.close();
			System.out.println("PDF generated successfully");

		} catch (Exception e) {
			System.out.println("PDF Generation Failed: \t : " + e.toString());
		}
	}
}
