/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bundestagdataimport;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Jeff
 */
public class BundestagDataImport {
    
    private DBConnection db;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new BundestagDataImport().doStuff();
        
        
    }
    
    private void doStuff() {
        db = new DBConnection();
        db.connectToMysql("localhost", "bundestag", "bundestag", "bundestag");
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File("c:\\file.xml");
 
        try {
            
            File f = new File("");
            
            JFileChooser fileChooser = new JFileChooser("./");
            fileChooser.setName("Bitte XML-Datei wählen");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XML-Files", "xml");
            fileChooser.setFileFilter(filter);
            int approved = fileChooser.showOpenDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {
                f = fileChooser.getSelectedFile();
            } else {
                System.err.println("No File Selected");
                System.exit(1);
            }
            
            Document document = (Document) builder.build(f);
            Element rootNode = document.getRootElement();
            List abgeordnetenList = rootNode.getChild("abgeordnete").getChildren("abgeordneter");
            List parteienList = rootNode.getChild("parteien").getChildren("partei");
            List bundeslandList = rootNode.getChild("bundeslaender").getChildren("bundesland");
            
            workList("bundesland", bundeslandList);
            workList("partei", parteienList);
            workList("abgeordneter", abgeordnetenList);
            
            
        } catch (IOException ex) {
            Logger.getLogger(BundestagDataImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(BundestagDataImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void workList(String qName, List list) {
        for (int i = 0; i < list.size(); i++) {
 
            Element node = (Element) list.get(i);
            
            switch(qName) {
                case "abgeordneter":

                    String vorname = node.getChildText("vorname");
                    String nachname = node.getChildText("nachname");
                    String bild = node.getChildText("bild");
                    String geschlecht = node.getChildText("geschlecht");
                    String konfession = node.getChildText("konfession");
                    String familienstand = node.getChildText("familienstand");
                    int kinder = Integer.parseInt(node.getChildText("kinder"));
                    String wahlkreis = node.getChildText("wahlkreis");
                    String bundesland = node.getChildText("bundesland");
                    String partei = node.getChildText("partei");
                    String fraktion = node.getChildText("fraktion");
                    String geburtsdatum = node.getChildText("geburtsdatum");
                    String aktuellerberuf = node.getChildText("berufaktuell");
                    String berufe = node.getChildText("berufe");
                    String email = node.getChildText("email");
                    String website = node.getChildText("website");
                    
                    Element addNode = node.getChild("adresse");
                    String addBundestag = addNode.getChildText("bundestag");
                    String addWahlkreis = addNode.getChildText("wahlkreis");

                    int idpartei = db.getParteiIDByName(partei);
                    int idbundesland = db.getBundeslandIDByName(bundesland);

                    if(idpartei == -1 || idbundesland == -1) {
                        System.out.println("Partei: " + partei + " -> " + idpartei);
                        System.out.println("Bundesland: " + bundesland + " -> " + idbundesland);
                        System.out.println("Foreign Keys nicht gefunden ... bitte erst Parteien und Bundesländer importieren!");
                        System.exit(1);
                    }

                    db.writeToAbgeordneter(vorname, nachname, bild, geschlecht, konfession, familienstand, kinder, wahlkreis, fraktion,
                            geburtsdatum, aktuellerberuf, berufe, addBundestag, addWahlkreis, email, website, idpartei, idbundesland);

                    break;
                case "partei":

                    String kuerzung = node.getChildText("kurzname");
                    String pname = node.getChildText("name");
                    String parteichef = node.getChildText("parteichef");
                    String pfraktion = node.getChildText("fraktion");
                    String fraktionsvorsitzender = node.getChildText("fraktionsvorsitzender");
                    String pwappen = node.getChildText("wappen");
                    int mitgliederzahl = Integer.parseInt(node.getChildText("mitgliederzahl"));

                    db.writeToPartei(kuerzung, pname, parteichef, pfraktion, fraktionsvorsitzender, pwappen, mitgliederzahl);

                    break;
                case "bundesland":
                    String bname = node.getChildText("name");
                    String kurzname = node.getChildText("kurzname");
                    String hauptstadt = node.getChildText("hauptstadt");
                    String bwappen = node.getChildText("wappen");

                    db.writeToBundesland(bname, kurzname, hauptstadt, bwappen);
                    break;
            }
        }
    }
    
}
