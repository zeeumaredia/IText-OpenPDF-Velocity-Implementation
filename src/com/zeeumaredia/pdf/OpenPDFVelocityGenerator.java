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

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

public class OpenPDFVelocityGenerator {

	final private static String timeStamp = new SimpleDateFormat("hh_mm_ss").format(Calendar.getInstance().getTime());
	private static final String VM_TEMPLATE_PATH = "resources/vmtemplates/velocityTemplate.vm";
	private static final String PDF_OUTPUT = "output/openPDFVelocity/velocityOpenPDF_" + timeStamp + ".pdf";

	public static void main(String[] args) {
		try {
			OpenPDFVelocityGenerator htmlToPdf = new OpenPDFVelocityGenerator();
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
		try {

			Document document = new Document(PageSize.A4,20f,20f,20f,20f);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);

			// open document
			document.open();
	
			HTMLWorker htmlWorker = new HTMLWorker(document);
			htmlWorker.parse(new StringReader(html));
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
