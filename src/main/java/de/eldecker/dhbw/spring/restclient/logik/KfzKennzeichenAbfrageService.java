package de.eldecker.dhbw.spring.restclient.logik;

import static java.util.Optional.empty;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import de.eldecker.dhbw.spring.restclient.konfiguration.RetryConfig;
import de.eldecker.dhbw.spring.restclient.model.KfzHalter;
import de.eldecker.dhbw.spring.restclient.model.KfzKennzeichenException;


/**
 * Klasse mit Logik zur Abfrage von KFZ-Kennzeichen über REST-Call
 * zu einem anderen Microservice.
 */
@Service
@CacheConfig( cacheNames = "kfzHalterCache" )
public class KfzKennzeichenAbfrageService {

    private static final Logger LOG = LoggerFactory.getLogger( KfzKennzeichenAbfrageService.class );

    /** Objekt für REST-Calls. */
    private final RestClient _restClient;
    
    /** Zähler für Entleerungen des Caches für die Abfrage-Ergebnisse. */ 
    private int cacheEntleerungsZaehler = 0;


    /**
     * Konstruktor für Erzeugung {@code RestClient}-Objekt mit
     * Basis-URL {@code http://localhost:8080}.
     */
    @Autowired
    public KfzKennzeichenAbfrageService( RestClient.Builder restClientBuilder ) {

        _restClient = restClientBuilder.baseUrl( "http://localhost:8080" )
                                       .build();
    }


    /**
     * Abfrage KFZ-Kennzeichen von externer API.
     * <br><br>
     * 
     * Methode cacht Rückgabewerte und führt bei Fehlern während HTTP-Calls
     * auch automatische Retries durch; siehe hierzu auch die Konfig-Beans
     * {@link RetryConfig} und {@link CacheConfig}.
     *
     * @param kfzKennzeichen Abzufragendes KFZ-Kennzeichen
     *
     * @return Optional enthält Infos über KFZ-Halter wenn gefunden, sonst leer.
     *
     * @throws KfzKennzeichenException Fehler bei REST-Call, z.B. HTTP-Status-Code 
     *         500 von Client oder Fehler bei Deserialisierung von JSON.
     */
    @Cacheable( value = "kfzHalterCache" )
    @Retryable( retryFor    = KfzKennzeichenException.class,
                maxAttempts = 2,
                backoff     = @Backoff( delay = 500 ),
                listeners   = "meinRetryListener" )
    public Optional<KfzHalter> kfzKennzeichenAbfragen( String kfzKennzeichen )
                                                  throws KfzKennzeichenException {

        final String pfad = "/api/v1/abfrage/" + kfzKennzeichen;
        LOG.info( "Pfad für REST-Request: {}", pfad );

        try {

            ResponseEntity<KfzHalter> responseEntity = _restClient.get()
                                                                  .uri( pfad )
                                                                  .retrieve()
                                                                  .toEntity( KfzHalter.class );
            return Optional.of( responseEntity.getBody() );
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
    }


    /**
     * Wenn diese Methode aufgerufen wird, dann wird der Cache gelöscht.
     * Wir lassen die Methode regelmäßig von einem Scheduler aufrufen.
     * <br><br>
     *
     * Bedeutung des Cron-Ausdrucks: An Arbeitstagen (Montag bis einschl.
     * Freitag) zwischen 8 und 18 Uhr alle 3 Minuten ausführen.
     * <br><br>
     * 
     * Siehe auch Klasse {@link SchedulerConfig}.
     */
    @Scheduled( cron = "0 */3 8-18 * * MON-FRI" )
    @CacheEvict( value = "kfzHalterCache", allEntries = true )
    public void cacheLoeschen() {
    	
    	cacheEntleerungsZaehler++;
        LOG.info( "Der Cache wurde geleert ({}).", cacheEntleerungsZaehler );
    }

}
