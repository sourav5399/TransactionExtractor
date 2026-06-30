package com.sourav.trasactionextractor.controller;
import com.sourav.trasactionextractor.model.TransactionResponse;
import com.sourav.trasactionextractor.service.TransactionExtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/transactions")
public class TransactionExtController {

    private final TransactionExtService transactionService;

    public TransactionExtController(TransactionExtService transactionService) {
        this.transactionService = transactionService;
    }

    // Endpoint to load your data
    // Example: POST http://localhost:8080/api/transactions/upload?filePath=/path/to/file.pdf
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return ResponseEntity.badRequest().body("Only PDF files are supported");
            }
            transactionService.ingestPdf(file);
            return ResponseEntity.ok("PDF content successfully indexed into Vector Store.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to process PDF: " + e.getMessage());
        }
    }

    // Endpoint to retrieve the sorted data
    // Example: GET http://localhost:8080/api/transactions/recent
    @GetMapping("/recent")
    public ResponseEntity<TransactionResponse> getRecentTransactions() {
        try {
            TransactionResponse analysis = transactionService.getRecentTransactions();
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new TransactionResponse());
        }
    }
}