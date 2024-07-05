package de.eldecker.dhbw.spring.restclient.logik;

import static java.util.Optional.empty;
import static org.springframework.http.HttpStatus.NOT_FOUND; 

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.eldecker.dhbw.spring.restclient.model.KfzHalter;
import de.eldecker.dhbw.spring.restclient.model.KfzKennzeichenException;


/**
 * Klasse mit Logik zur Abfrage von KFZ-Kennzeichen über REST-Call
 * zu einem anderen Microservice.
 */
@Service
public class KfzKennzeichenAbfrageService {
    
    private static final Logger LOG = LoggerFactory.getLogger( KfzKennzeichenAbfrageService.class );

    /** Objekt für REST-Calls. */
    private final RestClient _restClient;

    /** Objekt für Deserialisierung von JSON nach Java-Objekt. */
    private final ObjectMapper _objectMapper;

    
    /**
     * Konstruktor für Erzeugung {@code RestClient}-Objekt mit Basis-URL {@code http://localhost:8080}.
     */
    @Autowired
    public KfzKennzeichenAbfrageService ( RestClient.Builder restClientBuilder,
                                          ObjectMapper objectMapper ) {

        _restClient = restClientBuilder.baseUrl( "http://localhost:8080" ).build();
        
        _objectMapper = objectMapper;
    }


    /**
     * Abfrage KFZ-Kennzeichen von externer API.
     *
     * @param kfzKennzeichen Abzufragendes KFZ-Kennzeichen
     * 
     * @return Optional enthält Infos über KFZ-Halter wenn gefunden, sonst leer.
     *
     * @throws KfzKennzeichenException Fehler bei REST-Call, z.B. HTTP-Status-Code 500
     *         von Client oder Fehler bei Deserialisierung von JSON.
     */
    public Optional<KfzHalter> kfzKennzeichenAbfragen( String kfzKennzeichen ) 
           throws KfzKennzeichenException {

        final String pfad = "/api/v1/abfrage/" + kfzKennzeichen;        
        LOG.info( "Pfad für REST-Request: " + pfad );

        try {
            
            ResponseEntity<String> responseEntity  = 
                                        _restClient.get()
                                                   .uri( pfad )
                                                   .retrieve()
                                                   .toEntity( String.class );
    
            String jsonString = responseEntity.getBody();
            
            KfzHalter kfzHalter = _objectMapper.readValue( jsonString, KfzHalter.class );
            
            return Optional.of( kfzHalter );
        }
        catch ( RestClientResponseException ex ) {
            
            LOG.error( "Exception bei REST-Abfrage: " + ex.getMessage() );

            if ( ex.getStatusCode().equals( NOT_FOUND ) ) {

                return empty();
                
            } else {

                throw new KfzKennzeichenException( 
                        "Fehler bei Abfrage KFZ-Kennzeichen: " + ex.getStatusCode() );
            }
        }
        catch ( JsonProcessingException ex ) {

            LOG.error( "Exception bei Deserialisierung Antwort von REST-Abfrage: " + 
                       ex.getMessage() );
            
            throw new KfzKennzeichenException( 
                    "JSON-Payload mit KFZ-Halter konnte nicht deserialisiert werden: " + 
                    ex.getMessage() );
        }
    }

}
