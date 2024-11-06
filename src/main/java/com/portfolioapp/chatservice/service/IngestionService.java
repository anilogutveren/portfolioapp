package com.portfolioapp.chatservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class IngestionService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);
    private final VectorStore vectorStore;
    private final FileDecryptionService fileDecryptionService;

    public IngestionService(VectorStore vectorStore, FileDecryptionService fileDecryptionService) {
        this.vectorStore = vectorStore;
        this.fileDecryptionService = fileDecryptionService;
    }

    @Override
    public void run(String... args) throws Exception {

        //fileDecryptionService.encryptFile("src/main/resources/docs/CV_English_WP_AnilLongEncrypted.pdf");

        String decryptedFilePath = "src/main/resources/docs/CV_English_WP_AnilLongDecrypted.pdf";
        fileDecryptionService.decryptFile(decryptedFilePath);

        // Set `resourcePDF` to point to the decrypted file
        Resource resourcePDF = new FileSystemResource(decryptedFilePath);

        if (resourcePDF.exists()) {
            var pdfReader = new PagePdfDocumentReader(resourcePDF);
            TextSplitter textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));
            log.info("VectorStore Loaded with data!");
        }


    }
}
