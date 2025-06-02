package de.eldecker.dhbw.spring.restclient.logik;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;


/**
 * RetryListener, schreibt Log-Nachricht, wenn tatsächlich ein Retry
 * ausgeführt wird.
 */
@Component
class MeinRetryListener implements RetryListener {

    private static final Logger logger = LoggerFactory.getLogger( MeinRetryListener.class );


    /**
     * Gibt immer {@code true} zurück.
     * 
     * @return {@code true}
     */
    @Override
    public <T, E extends Throwable> boolean open( RetryContext context,
                                                  RetryCallback<T, E> callback ) {
        return true;
    }

    
    /**
     * Schreibt Log-Nachricht mit Anzahl Retry-Versucht. 
     */
    @Override
    public <T, E extends Throwable> void onError( RetryContext context,
                                                  RetryCallback<T, E> callback,
                                                  Throwable throwable ) {

        logger.info( "Versuch Nr. " + context.getRetryCount() );
    }

    
    /**
     * Leere Implementierung.
     */
    @Override
    public <T, E extends Throwable> void close( RetryContext context,
                                                RetryCallback<T, E> callback,
                                                Throwable throwable ) {
    }
    
}
