package unipos.common.http;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


/**
 * Created by ggradnig on 06.02.2017.
 */

@Builder
@Data
public class HttpResponseObject{
    private String message;
    private Object entity;

    public static ResponseEntity<HttpResponseObject> error(String message){
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(HttpResponseObject.builder().entity(null).message(message).build());
    }

    public static ResponseEntity<HttpResponseObject> success(Object entity){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(HttpResponseObject.builder().entity(entity).message("").build());
    }
}
