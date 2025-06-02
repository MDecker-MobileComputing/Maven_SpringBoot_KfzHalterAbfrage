package de.eldecker.dhbw.spring.restclient.konfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;


/** 
 * Konfiguration für Retry von HTTP-Calls.
 */
@Configuration
@EnableRetry
public class RetryConfig {

}
