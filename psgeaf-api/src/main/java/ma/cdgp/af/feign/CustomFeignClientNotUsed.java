package ma.cdgp.af.feign;

import feign.Client;
import feign.Request;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CustomFeignClientNotUsed implements Client {

    private static final Logger logger = LoggerFactory.getLogger(CustomFeignClientNotUsed.class);
    private final Client delegate;

    public CustomFeignClientNotUsed() {
    	this.delegate = new feign.okhttp.OkHttpClient(UnsafeOkHttpClient.getUnsafeOkHttpClient());
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        // Log the request details
        logger.debug("Feign request: {} {}", request.httpMethod(), request.url());
        if (request.body() != null) {
            logger.debug("Request body: {}", new String(request.body()));
        }

        // Execute the request
        Response response = delegate.execute(request, options);

        // Log the response details
//        logger.info("Feign response: {} {}", response.status(), response.reason());
//        if (response.body() != null) {
//            // Convert the response body to a String
//            String responseBody = response.body().asReader(StandardCharsets.UTF_8).toString();
//            logger.info("Response body: {}", responseBody);
//        }
        
        logger.debug("Feign response: {} {}", response.status(), response.reason());
        if (response.body() != null) {
            // Convert the response body to a String
            String responseBody = new BufferedReader(
                new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
            logger.debug("Response body: {}", responseBody);
        }
        
//        logger.info("Feign response: {} {}", response.status(), response.reason());
//        if (response.body() != null) {
//            try (BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
//                String responseBody = reader.lines().collect(Collectors.joining("\n"));
//                logger.info("Response body: {}", responseBody);
//            } catch (IOException e) {
//                logger.error("Error reading response body", e);
//            }
//        }

        return response;
    }
}
