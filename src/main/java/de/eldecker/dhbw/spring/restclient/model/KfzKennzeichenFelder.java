package de.eldecker.dhbw.spring.restclient.model;


/**
 * Objekt für Daten, die von Frontend für Suchanfrage empfangen werden.
 * 
 * @param feld1 Unterscheidungszeichen (ein bis drei Buchstaben), 
 *              z.B. "KA" für "Karlsruhe"
 * 
 * @param feld2 Erkennungsnummer: ein oder zwei Buchstaben
 * 
 * @param feld3 Erkennungsnummer: ein bis vier Ziffern
 */
public record KfzKennzeichenFelder( String feld1, 
                                    String feld2, 
                                    String feld3 ) {    
    /**
     * Baut KFZ-Kennzeichen aus den drei Feldern zusammen.
     * 
     * @return KFZ-Kennzeichen mit Leerzeichen, z.B. "BAD E 1234"
     */
    @Override
    public String toString() {
        
        return feld1 + " " + feld2 + " " + feld3;                 
    }
    
}
