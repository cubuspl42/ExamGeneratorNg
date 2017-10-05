package pl.edu.pg.examgeneratorng;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String documentPath = args[1];
        String outputPath = args[2];

        OdfTextDocument odt = (OdfTextDocument) OdfDocument.loadDocument(documentPath);
        OdfContentDom contentDom = odt.getContentDom();

        List<CodePlaceholder> placeholders = Placeholders.findPlaceholders(contentDom.getRootElement());
        Placeholders.fillPlaceholders(contentDom, placeholders);

        odt.save(new File(outputPath));
    }
}
