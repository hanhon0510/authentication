package com.example.authentication.service;

import com.example.authentication.model.SysLog;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

@Service
public class PDFServiceImpl implements PDFService{
    @Override
    public void writeSysLogsToPdf(OutputStream outputStream, List<SysLog> sysLogs) {
//        PdfWriter writer = new PdfWriter(outputStream);
//        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
//        Document document = new Document(pdfDoc);
//
//        for (SysLog log : sysLogs) {
//            document.add(new Paragraph("Created Time: " + log.getCreatedTime()));
//            document.add(new Paragraph("Method: " + log.getMethod()));
//            document.add(new Paragraph("SysLog Id: " + log.getSysLogId()));
//            document.add(new Paragraph("---------------------------"));
//        }
//
//        document.close();
    }
}
