package de.eldecker.dhbw.spring.restclient.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping( "/app" )
public class ThymeleafController {
    
    private static final Logger LOG = LoggerFactory.getLogger( ThymeleafController.class );

    /**
     * Seite für Eingabe des abzufragenden KFZ-Kennzeichens eingeben.
     * 
     * @return Dateiname der Template-Datei ohne Suffix {@code .html}
     */
    @GetMapping( "/abfrage-kennzeichen" )
    public String abfrageKfzKennzeichenFormular() {
        
        return "abfrage-formular";
    }
    
    
    @GetMapping( "/abfrage-kennzeichen-action" )
    public String abfrageKfzKennzeichenAction( Model model ) {
        
        return "";
    }
}
