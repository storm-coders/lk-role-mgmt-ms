package com.vcgdev.demo.config.crypto;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.vcgdev.common.crypto.CryptoService;
import com.vcgdev.common.crypto.KeyFactoryProvider;
import com.vcgdev.common.exception.ServiceException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import lombok.Setter;
@Configuration
@ConfigurationProperties(prefix = "com.vcgdev.crypto")
@Setter
public class KeyFactoryProviderConfig extends KeyFactoryProvider{
    
    protected String ivSpec;
    protected String pwd;
    protected String saltString;

    @Bean
    @Lazy
    public SecretKey getSecretKey() throws ServiceException {
        this.password = pwd;
        this.salt = saltString;
        return this.generateKeys();
    }

    @Bean
    @Lazy
    public IvParameterSpec getSpec() {
        return this.ivSpecFromString(ivSpec);
    }

    @Bean
    public CryptoService cryptoService() {
        return new CryptoService();
    }
}
