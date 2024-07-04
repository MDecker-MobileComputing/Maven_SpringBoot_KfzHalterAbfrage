package de.eldecker.dhbw.spring.restclient.model;


/**
 * Eigene Exception-Klasse bei Fehlern bei Abfrage von KFZ-Kennzeichen
 * von anderem Microservice.
 */
@SuppressWarnings("serial")
public class KfzKennzeichenException extends Exception {
    
    public KfzKennzeichenException( String nachricht ) {
        
        super( nachricht );
    }

}
