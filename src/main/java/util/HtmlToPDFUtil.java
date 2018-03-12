package util;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;


public class HtmlToPDFUtil {

    /**
     * 生成PDF
     *
     * @param content       �?段HTML
     * @param outFilePath   生成路径
     * @param imgPrefixPath 图片绝对路径
     * @throws Exception
     */
    public static void html2PDF(String content, String outFilePath, String imgPrefixPath) throws Exception {

        Document document = new Document();
        PdfWriter pdfwriter = PdfWriter.getInstance(document,
                new FileOutputStream(outFilePath));
        pdfwriter.setViewerPreferences(PdfWriter.HideToolbar);
        document.open();
        if (content == null) {
            content = "No Data!";
        }
        InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"));
        InputStream incss = null;
        XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, in, incss, Charset.forName("UTF-8"), new ChineseFontProvide());
        document.close();
    }

}
