package smart.servicios;

import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class GeneradorPDF {
	
    private Logger logger = LoggerFactory.getLogger(GeneradorPDF.class);


    public ByteArrayOutputStream  crearPDF(String data) {
        String urlBase = "http://localhost:8080";
        String htmlContent = data;
    	ByteArrayOutputStream  fileOutputStream = new ByteArrayOutputStream();
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent,urlBase);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					logger.error("Error creando pdf", e);
				}
			}
		}
        return fileOutputStream;
    }
}