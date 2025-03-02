package dev.skrock.camunda.toolkit.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseUtil {

    public void checkResponse(ResponseEntity<?> response) throws ResponseException {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResponseException("received non-ok response", response);
        }
    }
}
