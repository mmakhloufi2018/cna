package ma.cdgp.af.feign;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomErrorDecoder implements ErrorDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        // Log the response status and body
        logger.error("Feign call failed: {} status: {}", methodKey, response.status());
        try {
            if (response.body() != null) {
            	
            	 // Execute the request
                //Response response = delegate.execute(request, options)
                // Log the response details
                logger.error("Feign response: {} {}", response.status(), response.reason());
                if (response.body() != null) {
                    byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                    String responseBody = new String(bodyData, Util.UTF_8);
                    logger.error("Response body: {}", responseBody);
                    //return response.toBuilder().body(bodyData).build();
                }
            	
            }
        } catch (Exception e) {
            logger.error("Error reading response body", e);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
