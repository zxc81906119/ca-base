package com.redhat.cleanbase.pdf.processor;

import com.lowagie.text.Document;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.*;

@Slf4j
public class PdfProcessor implements BasePdfProcessor {

    public void htmlToPdf(Reader reader, OutputStream outputStream) throws IOException {
        try (
                val document = new Document();
                reader
        ) {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            new HTMLWorker(document)
                    .parse(reader);
        }
    }

    public void htmlToPdf(File pdfFile, String html) throws IOException {
        val pdfOutputStream = new FileOutputStream(pdfFile, false);
        htmlToPdf(new StringReader(html), pdfOutputStream);
    }

}