package ma.cdgp.af.feign;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.Client;
import feign.Request;
import feign.Response;
import feign.Util;

public class BufferingFeignClient implements Client {

    private static final Logger logger = LoggerFactory.getLogger(BufferingFeignClient.class);
    private final Client delegate;

    public BufferingFeignClient() {
        this.delegate = new feign.okhttp.OkHttpClient(UnsafeOkHttpClient.getUnsafeOkHttpClient());
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        // Log the request details
        logger.debug("Feign request: {} {}", request.httpMethod(), request.url());
        if (request.body() != null) {
            logger.debug("Request body: {}", new String(request.body(), Util.UTF_8));
        }

        // Execute the request
        Response response = delegate.execute(request, options);

        // Log the response details
        logger.debug("Feign response: {} {}", response.status(), response.reason());
        if (response.body() != null) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            String responseBody = new String(bodyData, Util.UTF_8);
            logger.debug("Response body: {}", responseBody);
            return response.toBuilder().body(bodyData).build();
        }

        return response;
    }
}
