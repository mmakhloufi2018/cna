package ma.cdgp.af.feign;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;
import feign.codec.ErrorDecoder;
import feign.Logger;
import feign.okhttp.OkHttpClient;


@Configuration
public class FeignClientConfiguration {
//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
////         return new BasicAuthRequestInterceptor("deadmin", "5xG39G652");
//         return new BasicAuthRequestInterceptor("devadmin", "newpswd1");
//    }
    
    
    @Bean
    public Client feignClient() {
    	return new ma.cdgp.af.feign.BufferingFeignClient();
       // return new CustomFeignClient();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                //Do your validations
                return true;
            }
            };

            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            return sslContext.getSocketFactory();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public CustomRequestInterceptor customRequestInterceptor() {
        return new CustomRequestInterceptor();
    }
    
    
//    @Bean
//    public CustomFeignRequestLogging customFeignRequestLogging() {
//        return new CustomFeignRequestLogging();
//    }

}