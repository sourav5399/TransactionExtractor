package com.sourav.trasactionextractor.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionResponse {
    @JsonProperty("transactions")
    public List<Transaction> transactions;
    
    @JsonProperty("message")
    public String message;
    
    public static class Transaction {
        @JsonProperty("from")
        public String from;
        
        @JsonProperty("to")
        public String to;
        
        @JsonProperty("amount")
        public double amount;
        
        @JsonProperty("time")
        public String time;
    }
}