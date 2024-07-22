package com.redhat.cleanbase.pdf.processor;

import java.io.*;

public interface BasePdfProcessor {
    void htmlToPdf(Reader reader, OutputStream outputStream) throws IOException;

    void htmlToPdf(File pdfFile, String html) throws IOException;
}
