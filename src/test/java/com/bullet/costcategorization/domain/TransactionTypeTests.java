package com.bullet.costcategorization.domain;

import com.bullet.costcategorization.domain.LineItem.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.bullet.costcategorization.domain.LineItem.TransactionType.*;

public class TransactionTypeTests {
    @Test
    public void expectAf() {
        TransactionType transactionType = TransactionType.fromString("Af");

        Assertions.assertEquals(AF, transactionType);
    }

    @Test
    public void expectBij() {
        TransactionType transactionType = TransactionType.fromString("Bij");

        Assertions.assertEquals(BIJ, transactionType);
    }

    @Test
    public void expectUnknown() {
        TransactionType transactionType = TransactionType.fromString("bla");

        Assertions.assertEquals(UNKNOWN, transactionType);
    }

    @Test
    public void casingShouldNotMatter() {
        TransactionType transactionType = TransactionType.fromString("aF");

        Assertions.assertEquals(AF, transactionType);
    }
}
