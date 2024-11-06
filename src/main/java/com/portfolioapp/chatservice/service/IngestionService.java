package com.portfolioapp.chatservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class IngestionService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);
    private final VectorStore vectorStore;

    @Autowired
    private FileDecryptionService fileDecryptionService;

    @Value("classpath:/docs/CV_English_AnilLong_WP.pdf")
    private Resource resourcePDF;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) throws Exception {

        //fileDecryptionService.encryptFile("src/main/resources/docs/CV_English_WP_AnilLongEncrypted.pdf");

        fileDecryptionService.decryptFile("src/main/resources/docs/CV_English_WP_AnilLongDecrypted.pdf");

        var pdfReader = new PagePdfDocumentReader(resourcePDF);
        TextSplitter textSplitter = new TokenTextSplitter();
        vectorStore.accept(textSplitter.apply(pdfReader.get()));
        log.info("VectorStore Loaded with data!");
    }
}
