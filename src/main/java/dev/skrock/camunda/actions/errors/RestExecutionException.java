/*
  ------------------------------------------------------------------------------
        (c) by data experts gmbh
              Postfach 1130
              Woldegker Str. 12
              17001 Neubrandenburg

  Dieses Dokument und die hierin enthaltenen Informationen unterliegen
  dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
  Herausgebers weder als ganzes noch in Teilen dupliziert oder reproduziert
  noch manipuliert werden.
*/

package dev.skrock.camunda.actions.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
public class RestExecutionException extends ExecutionException {

    public static void checkResponse(ResponseEntity<?> response) throws RestExecutionException {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RestExecutionException("received non-ok response", response);
        }
    }

    private final transient ResponseEntity<?> response;

    public RestExecutionException(String message, ResponseEntity<?> response) {
        super(message);
        this.response = response;
    }

    @Override
    public String getMessage() {
        return """
                %s
                
                Response: %s
                """.formatted(super.getMessage(), response);
    }
}
