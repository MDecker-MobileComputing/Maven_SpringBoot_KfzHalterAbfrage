package de.eldecker.dhbw.spring.restclient.web;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.eldecker.dhbw.spring.restclient.logik.KfzKennzeichenAbfrageService;
import de.eldecker.dhbw.spring.restclient.model.KfzKennzeichenException;
import de.eldecker.dhbw.spring.restclient.model.KfzKennzeichenFelder;


/**
 * Controller-Klasse für Thymeaf-Templates.
 */
@Controller
@RequestMapping( "/app" )
public class ThymeleafController {
    
    private static final Logger LOG = LoggerFactory.getLogger( ThymeleafController.class );

    /** 
     * Service-Bean mit eigentlicher Logik für Abfrage von KFZ-Kennzeichen bei 
     * anderem Microservice. 
     */
    @Autowired
    private KfzKennzeichenAbfrageService _kfzKennzeichenService;
    
    /** Objekt mit leeren Feldern, wird für {@code th:object} in Thymeleaf-Template benötigt. */
    private final static KfzKennzeichenFelder KFZKENNZEICHEN_FELDER_LEER = 
                                                    new KfzKennzeichenFelder( "", "", "" );    
    
    /**
     * Seite für Eingabe des abzufragenden KFZ-Kennzeichens anzeigen.
     * 
     * @param model Objekt für Platzhalterwerte in Template-Datei
     * 
     * @return Dateiname der anzuzeigenden Template-Datei ohne Suffix {@code .html}
     */
    @GetMapping( "/abfrage-kennzeichen" )
    public String abfrageKfzKennzeichenFormular( Model model ) {
                        
        model.addAttribute( "kfzKennzeichenFelder", KFZKENNZEICHEN_FELDER_LEER );
        
        return "abfrage-formular";
    }
    
    
    /**
     * Controller-Methode für Suche nach KFZ-Kennzeichen.
     * 
     * @param kfzKennzeichenFelder In Web-Formular eingetragene Bestandteile des zu 
     *                             suchenden Kennzeichens.
     *                              
     * @param model Objekt für Platzhalterwerte in Template-Datei
     * 
     * @return Dateiname der anzuzeigenden Template-Datei ohne Suffix {@code .html}
     */
    @GetMapping( "/abfrage-kennzeichen-action" )
    public String abfrageKfzKennzeichenAction(
            @ModelAttribute("kfzKennzeichenFelder") KfzKennzeichenFelder kfzKennzeichenFelder,
            Model model ) {
        
        LOG.info( "Abfrage für KFZ-Kennzeichen \"{}\" erhalten.", kfzKennzeichenFelder );
        
        model.addAttribute( "kennzeichen", kfzKennzeichenFelder );
        
        String ergebnisText = "";
        
        Optional<String> ergebnisOptional = null;
        try {
        
            ergebnisOptional =_kfzKennzeichenService.kfzKennzeichenAbfragen( kfzKennzeichenFelder.toString() );
            
            if ( ergebnisOptional.isEmpty() ) {
                
                ergebnisText = "KFZ-Kennzeichen \"" + kfzKennzeichenFelder + "\" nicht gefunden.";
                
            } else {
                
                ergebnisText = ergebnisOptional.get();
            }
        }
        catch ( KfzKennzeichenException ex ) {
            
            ergebnisText = "Fehler bei Abfrage von KFZ-Kennzeichen aufgetreten: " + ex.getMessage();              
            LOG.error( ergebnisText );            
        }
                
        model.addAttribute( "ergebnis", ergebnisText );
        
        return "abfrage-ergebnis";
    }
}
