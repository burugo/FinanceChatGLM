package xfp.pdf.run;

import org.apache.pdfbox.pdmodel.PDDocument;
import xfp.pdf.arrange.MarkPdf;
import xfp.pdf.core.PdfParser;
import xfp.pdf.pojo.ContentPojo;
import xfp.pdf.tools.FileTool;

import java.io.File;
import java.io.IOException;


public class Pdf2html {

    public static void main(String[] args) throws IOException {

        File file = new File(Path.inputAllPdfPath);
        if (!file.exists()){
            System.out.println('路径不存在：'+file.getAbsolutePath());
            return;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            PDDocument pdd = null;
            try {
                pdd = PDDocument.load(f);
                ContentPojo contentPojo = PdfParser.parsingUnTaggedPdfWithTableDetection(pdd);
                MarkPdf.markTitleSep(contentPojo);
                FileTool.saveHTML(Path.outputAllHtmlPath, contentPojo, f.getAbsolutePath());
                int progress = (i + 1) * 100 / files.length;
                System.out.println("Processing file " + (i + 1) + " of " + files.length + " (" + progress + "%): " + f.getName());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    pdd.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}