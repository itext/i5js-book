/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */
package part4.chapter15;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class SvgLayers extends SvgToPdf {

    /** The resulting PDF. */
    public static final String RESULT = "results/part4/chapter15/foobarcity.pdf";
    /** The map. */
    public static final String RUES = "resources/xml/foobarrues.svg";
    /** The map. */
    public static final String STRATEN = "resources/xml/foobarstraten.svg";

    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException 
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
    	Document document = new Document(new Rectangle(6000, 6000));
        // step 2
    	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        writer.setViewerPreferences(PdfWriter.PageModeUseOC | PdfWriter.FitWindow);
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        // step 3
        document.open();
        // step 4
        // CREATE GRID LAYER
        PdfLayer gridLayer = new PdfLayer("Grid", writer);
        gridLayer.setZoom(0.2f, 1);
        gridLayer.setOnPanel(false);
        // CREATE STREET LAYERS
        PdfLayer streetlayer = PdfLayer.createTitle(
                "Streets / Rues / Straten", writer);
        PdfLayer streetlayer_en = new PdfLayer("English", writer);
        streetlayer_en.setOn(true);
        streetlayer_en.setLanguage("en", true);
        PdfLayer streetlayer_fr = new PdfLayer("Fran\u00e7ais", writer);
        streetlayer_fr.setOn(false);
        streetlayer_fr.setLanguage("fr", false);
        PdfLayer streetlayer_nl = new PdfLayer("Nederlands", writer);
        streetlayer_nl.setOn(false);
        streetlayer_nl.setLanguage("nl", false);
        streetlayer.addChild(streetlayer_en);
        streetlayer.addChild(streetlayer_fr);
        streetlayer.addChild(streetlayer_nl);
        ArrayList<PdfLayer> radio = new ArrayList<PdfLayer>();
        radio.add(streetlayer_en);
        radio.add(streetlayer_fr);
        radio.add(streetlayer_nl);
        writer.addOCGRadioGroup(radio);
        // CREATE MAP
        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate map = cb.createTemplate(6000, 6000);
        // DRAW CITY
        drawSvg(map, CITY);
        cb.addTemplate(map, 0, 0);
        PdfTemplate streets = cb.createTemplate(6000, 6000);
        // DRAW STREETS
        drawSvg(streets, STREETS);
        streets.setLayer(streetlayer_en);
        cb.addTemplate(streets, 0, 0);
        PdfTemplate rues = cb.createTemplate(6000, 6000);
        drawSvg(rues, RUES);
        rues.setLayer(streetlayer_fr);
        cb.addTemplate(rues, 0, 0);
        PdfTemplate straten = cb.createTemplate(6000, 6000);
        drawSvg(straten, STRATEN);
        straten.setLayer(streetlayer_nl);
        cb.addTemplate(straten, 0, 0);
        // DRAW GRID
        cb.saveState();
        cb.beginLayer(gridLayer);
        cb.setGrayStroke(0.7f);
        cb.setLineWidth(2);
        for (int i = 0; i < 8; i++) {
            cb.moveTo(1250, 1500 + i * 500);
            cb.lineTo(4750, 1500 + i * 500);
        }
        for (int i = 0; i < 8; i++) {
            cb.moveTo(1250 + i * 500, 1500);
            cb.lineTo(1250 + i * 500, 5000);
        }
        cb.stroke();
        cb.endLayer();
        cb.restoreState();
        
        // CREATE INFO LAYERS
        PdfLayer cityInfoLayer = new PdfLayer("Foobar Info", writer);
        cityInfoLayer.setOn(false);
        PdfLayer hotelLayer = new PdfLayer("Hotel", writer);
        hotelLayer.setOn(false);
        cityInfoLayer.addChild(hotelLayer);
        PdfLayer parkingLayer = new PdfLayer("Parking", writer);
        parkingLayer.setOn(false);
        cityInfoLayer.addChild(parkingLayer);
        PdfLayer businessLayer = new PdfLayer("Industry", writer);
        businessLayer.setOn(false);
        cityInfoLayer.addChild(businessLayer);
        PdfLayer cultureLayer = PdfLayer.createTitle("Leisure and Culture",
                writer);
        PdfLayer goingoutLayer = new PdfLayer("Going out", writer);
        goingoutLayer.setOn(false);
        cultureLayer.addChild(goingoutLayer);
        PdfLayer restoLayer = new PdfLayer("Restaurants", writer);
        restoLayer.setOn(false);
        goingoutLayer.addChild(restoLayer);
        PdfLayer theatreLayer = new PdfLayer("(Movie) Theatres", writer);
        theatreLayer.setOn(false);
        goingoutLayer.addChild(theatreLayer);
        PdfLayer monumentLayer = new PdfLayer("Museums and Monuments",
                writer);
        monumentLayer.setOn(false);
        cultureLayer.addChild(monumentLayer);
        PdfLayer sportsLayer = new PdfLayer("Sports", writer);
        sportsLayer.setOn(false);
        cultureLayer.addChild(sportsLayer);
        // DRAW INFO
        BaseFont font = BaseFont.createFont(
                "c:/windows/fonts/webdings.ttf", BaseFont.WINANSI,
                BaseFont.EMBEDDED);
        cb.saveState();
        cb.beginText();
        cb.setRGBColorFill(0x00, 0x00, 0xFF);
        cb.setFontAndSize(font, 36);
        cb.beginLayer(cityInfoLayer);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x69), 2700, 3100, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x69), 3000, 2050, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x69), 3100, 2550, 0);
        cb.beginLayer(hotelLayer);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2000, 1900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2100, 1950, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2200, 2200, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2700, 3000, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2750, 3050, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2500, 3500, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2300, 2000, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 3250, 2200, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 3300, 2300, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 3400, 3050, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 3250, 3200, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2750, 3800, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2900, 3800, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 3000, 2400, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2000, 2800, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe3), 2600, 3200, 0);
        cb.endLayer(); // hotelLayer
        cb.beginLayer(parkingLayer);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe8), 2400, 2000, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe8), 2100, 2600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe8), 3300, 2250, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe8), 3000, 3900, 0);
        cb.endLayer(); // parkingLayer
        cb.beginLayer(businessLayer);
        cb.setRGBColorFill(0xC0, 0xC0, 0xC0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3050, 3600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3200, 3900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3150, 3700, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3260, 3610, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3350, 3750, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3500, 4000, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3500, 3800, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3450, 3700, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x46), 3450, 3600, 0);
        cb.endLayer(); // businessLayer
        cb.endLayer(); // cityInfoLayer
        cb.beginLayer(goingoutLayer);
        cb.beginLayer(restoLayer);
        cb.setRGBColorFill(0xFF, 0x14, 0x93);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 2650, 3500, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 2400, 1900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 2750, 3850, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 2700, 3200, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 2900, 3100, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 2850, 3000, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 2800, 2900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 2300, 2900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 1950, 2650, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 1800, 2750, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 3350, 3150, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 3400, 3100, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xe4), 3250, 3450, 0);
        cb.endLayer(); // restoLayer
        cb.beginLayer(theatreLayer);
        cb.setRGBColorFill(0xDC, 0x14, 0x3C);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xae), 2850, 3300, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xae), 3050, 2900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xae), 2650, 2900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xae), 2750, 2600, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xB8), 2800, 3350, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xB8), 2550, 2850, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xB8), 2850, 3300, 0);
        cb.endLayer(); // theatreLayer
        cb.endLayer(); // goingoutLayer
        cb.beginLayer(monumentLayer);
        cb.setRGBColorFill(0x00, 0x00, 0x00);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x47), 3250, 2750, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x47), 2750, 2900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x47), 2850, 3500, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xad), 2150, 3550, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xad), 3300, 2730, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xad), 2200, 2000, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xad), 2900, 3300, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0xad), 2080, 3000, 0);
        cb.endLayer(); // monumentLayer
        cb.beginLayer(sportsLayer);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x53), 2700, 4050, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x53), 2700, 3900, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x53), 2800, 3980, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x53), 1950, 2800, 0);
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
                .valueOf((char) 0x53), 3700, 2450, 0);
        cb.endLayer(); // sportsLayer
        cb.endText();
        cb.restoreState();
        // step 5
        document.close();
    }
    
    /**
     * Main method.
     *
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException 
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new SvgLayers().createPdf(RESULT);
    }
}
