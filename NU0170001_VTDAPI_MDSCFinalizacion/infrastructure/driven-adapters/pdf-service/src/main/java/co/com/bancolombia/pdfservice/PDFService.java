package co.com.bancolombia.pdfservice;

import co.com.bancolombia.model.pdf.IPDFService;
import co.com.bancolombia.model.redis.UserTransactional;
import lombok.RequiredArgsConstructor;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PDFService implements IPDFService {

    @Value("${services.pdf.dirTemplateWelcome}")
    private String dirTemplateWelcome;

    @Value("${services.pdf.templateFileName}")
    private String templateFileName;

    @Value("${services.pdf.urlGooglePlay}")
    private String urlGooglePlay;

    @Value("${services.pdf.urlAppStore}")
    private String urlAppStore;

    @Value("${services.pdf.urlRegulation}")
    private String urlRegulation;

    @Override
    public  String buildPdfWelcome(UserTransactional user, String deliveryMessage)  {
        File templatePdfFile = null;
        PDDocument templatePdfLoad = null;
        PDPage page = null;
        PDPageContentStream contentStream = null;
        byte[] bytesPdfResult = null;

        try(PDDocument temp = new PDDocument()) {
            templatePdfFile = new File(dirTemplateWelcome.concat(templateFileName));
            templatePdfLoad = PDDocument.load(templatePdfFile);
            page =  templatePdfLoad.getDocumentCatalog().getPages().get(0);

            temp.addPage(page);
            contentStream =  new PDPageContentStream(temp, page,PDPageContentStream.AppendMode.APPEND,true,true);
            PDType0Font font = PDType0Font.load(temp, new File(dirTemplateWelcome + "OpenSans_bold.ttf"));
            //content
            this.addTextOnPDF(contentStream,font, Color.BLACK,62,965,14,user.getFirstName());
            this.addTextOnPDF(contentStream,font, Color.BLACK,29,887,14, user.getAccountNumber());
            this.addTextOnPDF(contentStream,font, Color.BLACK,29,850,14, deliveryMessage);
            this.addTextOnPDF(contentStream,font, Color.BLACK,367,723,14, "$0");
            this.addTextOnPDF(contentStream,font, Color.BLACK,249,691,14,  user.getAtmCost());
            this.addTextOnPDF(contentStream,font, Color.BLACK,337,659,14,  user.getOfficeCost());
            this.addTextOnPDF(contentStream,font, Color.BLACK,330,627,14, user.getManagementFee());
            this.addTextOnPDF(contentStream,font, Color.BLACK,30,400,14, user.getGmf());
            // links
            this.addLinkOnPDF(page,294,180,100,50,urlGooglePlay);
            this.addLinkOnPDF(page,172,180,100,50,urlAppStore);
            this.addLinkOnPDF(page,130,133,30,20,urlRegulation);
            // END
            contentStream.close();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            temp.save(os);
            os.flush();
            os.close();
            templatePdfLoad.close();
            bytesPdfResult = os.toByteArray();
        } catch (IOException e) {
            return  null;
        }finally {
            extracted(templatePdfLoad, contentStream);
        }
        String res =  Base64.getEncoder().encodeToString(bytesPdfResult);
        return res;
    }

    private boolean extracted(PDDocument templatePdfLoad, PDPageContentStream contentStream) {
        if (templatePdfLoad != null) {
            try {
                templatePdfLoad.close();
            }catch (IOException e){
                return false ;
            }
        }
        if (contentStream != null) {
            try {
                contentStream.close();
            }catch (IOException e){
                return false;
            }
        }
        return true;
    }

    private void addLinkOnPDF(PDPage page, int positionX, int positionY, int width, int height, String url) throws IOException {
        PDAnnotationLink txtLink = new PDAnnotationLink();
        PDActionURI action = new PDActionURI ();
        action.setURI (url);
        txtLink.setAction (action);
        // Position
        PDRectangle position = new PDRectangle(positionX,positionY,width,height);
        txtLink.setRectangle (position);
        page.getAnnotations ().add (txtLink);
    }

    private void addTextOnPDF(PDPageContentStream contentStream,PDType0Font fontType, Color fontColor, int positionX, int positionY,
                              int fontSize, String text) throws IOException {
        List<String> listText = null;
        if (text.length()>74){
            listText = this.wrapText(text);
        }else{
            listText = Collections.singletonList(text);
        }
        for (String item: listText) {
            contentStream.setNonStrokingColor(fontColor);
            contentStream.beginText();
            contentStream.setFont(fontType, fontSize);
            contentStream.newLineAtOffset(positionX, positionY);
            contentStream.showText(item);
            contentStream.endText();
            positionY = positionY-fontSize-2;
        }

    }

    private List<String> wrapText(String text) {
        List<String> listLines = new ArrayList<>();
        AtomicReference<String> newLine = new AtomicReference<>("");
        List<String> partsOfStrings = Arrays.asList(text.split(" "));

        partsOfStrings.forEach(word->{
            String newLineTemp = newLine.get().concat(" ").concat(word).trim();
            if (newLineTemp.length()<= 74){
                newLine.set(newLine.get().concat(" ").concat(word).trim());
            }else{
                listLines.add(newLine.get());
                newLine.set(word);
            }
        });
        listLines.add(newLine.get().trim());
        return listLines;
    }
}
