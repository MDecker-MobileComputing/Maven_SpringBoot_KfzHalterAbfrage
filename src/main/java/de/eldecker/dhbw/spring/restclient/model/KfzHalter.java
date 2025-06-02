package de.eldecker.dhbw.spring.restclient.model;


/**
 * Klasse als Deserialisierungsziel für JSON-Playload mit von 
 * REST-Endpunkt erhaltenen Daten von anderem Microservice. 
 */
public record KfzHalter( String anrede,                                  
                         String vorname,
                         String nachname,
                         int plz,
                         String wohnort,
                         String anschrift ) {
    
    /**
     * Gibt String für Anzeige mit KFZ-Halter-Daten zurück.
     * 
     * @return String mit allen Attribute, für Anzeige auf UI.
     *         Beispielwert: 
     *         </i>Herr Dr. Jan Mustermeyer, Bahnhofstraße 5, 76532 Baden-Baden</i>              
     */
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append( anrede    ).append( " "  )
          .append( vorname   ).append( " "  ).append( nachname ).append( ", " )
          .append( anschrift ).append( ", " )
          .append( plz       ).append( " "  ).append( wohnort );
        
        return sb.toString();
    }
    
}
