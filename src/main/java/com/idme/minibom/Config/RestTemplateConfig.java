package com.idme.minibom.Config;

import com.huawei.innovation.rdm.delegate.exception.RdmDelegateException;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * RestTemplate 配置
 *
 * @since 2024-04-10
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 构建 RestTemplate 对象，忽略证书校验。
     *
     * @return RestTemplate 对象
     */
    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    /**
     * 构建 RestTemplate 对象，忽略证书校验。
     *
     * @param builder builder
     * @return RestTemplate 对象
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    /**
                     * 获取证书颁发者列表
                     *
                     * @return 证书颁发者列表
                     */
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    /**
                     * 校验客户端证书
                     *
                     * @param certs the peer certificate chain
                     * @param authType the authentication type based on the client certificate
                     */
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    /**
                     * 校验服务端证书
                     *
                     * @param certs the peer certificate chain
                     * @param authType the key exchange algorithm used
                     */
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RdmDelegateException("config.1", e.getMessage());
        }
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                // 需要替换为自己的代理服务器，如果没有，需要去除
               // .setProxy(new HttpHost("proxy.huawei.com", 8080))
                .build();
        HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
        customRequestFactory.setHttpClient(httpClient);

        return builder.requestFactory(() -> customRequestFactory).build();
    }
}
