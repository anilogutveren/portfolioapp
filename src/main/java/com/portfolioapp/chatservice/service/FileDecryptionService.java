package com.portfolioapp.chatservice.service;

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileDecryptionService {

    private final StandardPBEByteEncryptor encryptor;

    public FileDecryptionService(@Value("${jasypt.encryptor.password}") String password) {
        // Configure encryptor
        this.encryptor = new StandardPBEByteEncryptor();
        SimplePBEConfig config = new SimplePBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        config.setIvGenerator(new RandomIvGenerator());
        this.encryptor.setConfig(config);
    }

    /**
     * Decrypts an encrypted file and saves it as a decrypted version.
     * @param decryptedFilePath Path where the decrypted file will be saved.
     */
    public void decryptFile(String decryptedFilePath) throws Exception {

        String encryptedFilePath = "src/main/resources/docs/CV_English_WP_AnilLongEncrypted.pdf";

        // Read the encrypted file bytes
        byte[] encryptedData = Files.readAllBytes(Paths.get(encryptedFilePath));

        // Decrypt the data
        byte[] decryptedData = encryptor.decrypt(encryptedData);

        // Save the decrypted file
        try (FileOutputStream fos = new FileOutputStream(decryptedFilePath)) {
            fos.write(decryptedData);
        }

    }


    /**
     * Encrypts a file and saves it as an encrypted version.
     * @param encryptedFilePath Path where the encrypted file will be saved.
     * Please do NOT delete this method. It is used for creating the encrypted file for the first time.
     */
    public void encryptFile(String encryptedFilePath) throws Exception {

        String filePath = "src/main/resources/docs/CV_English_WP_AnilLong.pdf";

        // Read the original file as byte array
        byte[] fileBytes = Files.readAllBytes(new File(filePath).toPath());

        // Encrypt the file bytes
        byte[] encryptedData = encryptor.encrypt(fileBytes);

        // Save encrypted data to the specified path
        try (FileOutputStream fos = new FileOutputStream(encryptedFilePath)) {
            fos.write(encryptedData);
        }

    }
}
