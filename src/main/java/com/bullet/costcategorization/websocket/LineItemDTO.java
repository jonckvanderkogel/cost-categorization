package com.bullet.costcategorization.websocket;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import io.vavr.Tuple2;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class LineItemDTO {
    private String category;
    private String date;
    private String description;
    private String transactionType;
    private double amount;
    private String statement;

    public LineItemDTO(Tuple2<Category,LineItem> categorizedLineItem, DateTimeFormatter dateTimeFormatter) {
        var lineItem = categorizedLineItem._2;
        this.category = categorizedLineItem._1.toString();
        this.date = dateTimeFormatter.format(lineItem.getDate());
        this.description = lineItem.getDescription();
        this.transactionType = lineItem.getTransactionType().toString();
        this.amount = lineItem.getAmount();
        this.statement = lineItem.getStatement();
    }
}
