package de.eldecker.dhbw.spring.restclient.logik;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

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

        ResponseSpec responseSpec = _restClient.get().uri( pfad ).retrieve();

        ResponseEntity<Void> responseEntityVoid = responseSpec.toBodilessEntity();

        HttpStatusCode statusCode = responseEntityVoid.getStatusCode();
        LOG.info( "Status-Code von REST-Request " + pfad + ": " + statusCode );

        if ( statusCode.is2xxSuccessful() ) {

            String ergebnisString = responseSpec.body( String.class );
            return Optional.of( ergebnisString );

        } else {

            throw new KfzKennzeichenException(
                         "Fehler bei REST-Anfrage für KFZ-Kennzeichen-Abfrage: " +
                         responseEntityVoid.getStatusCode()
                      );
        }
    }

}
