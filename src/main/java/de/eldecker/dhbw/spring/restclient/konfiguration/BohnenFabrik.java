package de.eldecker.dhbw.spring.restclient.konfiguration;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;


/**
 * Konfig-Klasse, die Methoden enthält, die Beans bereitstellt. 
 */
@Configurable
public class BohnenFabrik {

    @Bean
    public ObjectMapper holeObjectMapper() {

        return JsonMapper.builder()
                .disable( FAIL_ON_UNKNOWN_PROPERTIES ) // Ignoriert unbekannte JSON-Felder beim Deserialisieren
                .enable(  INDENT_OUTPUT              ) // Schöne Ausgabe beim Serialisieren von Objekten in JSON
                .build();
    }
    
}

