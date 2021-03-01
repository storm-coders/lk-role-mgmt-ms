package com.vcgdev.demo.config.crypto;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.vcgdev.common.crypto.CryptoService;
import com.vcgdev.common.exception.ServiceException;

import org.springframework.stereotype.Component;

@Component
public class CryptoServiceFacade {
    private final CryptoService cryptoService;
    private final SecretKey secretKey;
    private final IvParameterSpec ivParameterSpec;
    private static final String CRYPTO_ALGORITHM = "AES/CBC/PKCS5Padding";

    public CryptoServiceFacade(CryptoService cryptoService,
        SecretKey secretKey,
        IvParameterSpec ivParameterSpec) {
            this.cryptoService = cryptoService;
            this.secretKey = secretKey;
            this.ivParameterSpec = ivParameterSpec;
    }

    public String encrypt(String data) throws ServiceException {
        return cryptoService.encrypt(CRYPTO_ALGORITHM, data, secretKey, ivParameterSpec);
    }

    public String decrypt(String data) throws ServiceException {
        return cryptoService.decrypt(CRYPTO_ALGORITHM, data, secretKey, ivParameterSpec);
    }
}
