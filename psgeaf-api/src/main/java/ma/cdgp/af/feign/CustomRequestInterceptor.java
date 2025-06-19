package ma.cdgp.af.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomRequestInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CustomRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        // Log the request details
        logger.debug("Feign request: {} {}", template.method(), template.url());
        if (template.requestBody().asBytes() != null) {
            logger.debug("Request body: {}", new String(template.requestBody().asBytes()));
        }
    }
}
