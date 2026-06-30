package com.sourav.trasactionextractor.intf;

import com.sourav.trasactionextractor.model.TransactionResponse;

public interface TransactionExtBot {
    TransactionResponse analyze(String userQuery);
}
