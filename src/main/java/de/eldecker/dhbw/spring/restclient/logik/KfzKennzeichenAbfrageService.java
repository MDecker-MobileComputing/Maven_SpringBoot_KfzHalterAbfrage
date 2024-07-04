package de.eldecker.dhbw.spring.restclient.logik;

import static java.util.Optional.empty;
import static org.springframework.http.HttpStatus.NOT_FOUND; 

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import de.eldecker.dhbw.spring.restclient.model.KfzKennzeichenException;


/**
 * Klasse mit Logik zur Abfrage von KFZ-Kennzeichen über REST-Call
 * zu einem anderen Microservice.
 */
@Service
public class KfzKennzeichenAbfrageService {


    /** Objekt für REST-Calls. */
    private final RestClient _restClient;


    /**
     * Konstruktor für Erzeugung {@code RestClient}-Objekt mit Basis-URL {@code http://localhost:8080}.
     */
    @Autowired
    public KfzKennzeichenAbfrageService ( RestClient.Builder restClientBuilder ) {

        _restClient = restClientBuilder.baseUrl( "http://localhost:8080" ).build();
    }


    /**
     * Abfrage KFZ-Kennzeichen von externer API.
     *
     *
     * @param kfzKennzeichen
     * @return Optional enthält KFZ-Kennzeichen, wenn gefunden; wenn nicht gefunden, dann
     *         ist das Optional leer.
     *
     * @throws KfzKennzeichenException Fehler bei REST-Call, z.B. HTTP-Status-Code 500
     *         von Client.
     */
    public Optional<String> kfzKennzeichenAbfragen( String kfzKennzeichen ) throws KfzKennzeichenException {

        final String pfad = "/api/v1/abfrage/" + kfzKennzeichen;

        try {
            
            ResponseEntity<String> responseEntity  = 
                                        _restClient.get()
                                                   .uri( pfad )
                                                   .retrieve()
                                                   .toEntity( String.class );
    
            return Optional.of( responseEntity.getBody() );
        }
        catch ( RestClientResponseException ex ) {

            if ( ex.getStatusCode().equals( NOT_FOUND ) ) {

                return empty();
                
            } else {

                throw new KfzKennzeichenException( 
                        "Fehler bei Abfrage KFZ-Kennzeichen: " + ex.getStatusCode() );
            }
        }
    }

}
