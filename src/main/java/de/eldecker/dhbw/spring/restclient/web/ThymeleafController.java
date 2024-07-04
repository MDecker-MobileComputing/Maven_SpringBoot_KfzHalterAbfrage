package de.eldecker.dhbw.spring.restclient.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.eldecker.dhbw.spring.restclient.model.KfzKennzeichenFelder;


/**
 * Controller-Klasse für Thymeaf-Templates.
 */
@Controller
@RequestMapping( "/app" )
public class ThymeleafController {
    
    private static final Logger LOG = LoggerFactory.getLogger( ThymeleafController.class );

    /**
     * Seite für Eingabe des abzufragenden KFZ-Kennzeichens anzeigen.
     * 
     * @return Dateiname der anzuzeigenden Template-Datei ohne Suffix {@code .html}
     */
    @GetMapping( "/abfrage-kennzeichen" )
    public String abfrageKfzKennzeichenFormular() {
        
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
        
        LOG.info( "Abfrage für KFZ-Kennzeichen erhalten: " + kfzKennzeichenFelder );
        
        return "abfrage-ergebnis";
    }
}
