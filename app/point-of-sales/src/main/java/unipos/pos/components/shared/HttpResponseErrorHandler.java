package unipos.pos.components.shared;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by Gradnig on 07.11.2015.
 */
public class HttpResponseErrorHandler  implements ResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return RestUtil.isError(response.getStatusCode());
    }
}