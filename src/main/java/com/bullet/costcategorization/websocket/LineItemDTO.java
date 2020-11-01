package com.bullet.costcategorization.websocket;

import com.bullet.costcategorization.domain.Category;
import com.bullet.costcategorization.domain.LineItem;
import io.vavr.Tuple2;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class LineItemDTO {
    private final String category;
    private final String date;
    private final String year;
    private final String month;
    private final String description;
    private final String transactionType;
    private final double amount;
    private final String statement;

    public LineItemDTO(Tuple2<Category,LineItem> categorizedLineItem, DateTimeFormatter dateTimeFormatter) {
        var lineItem = categorizedLineItem._2;
        this.category = categorizedLineItem._1.toString();
        this.date = dateTimeFormatter.format(lineItem.getDate());
        this.year = String.valueOf(lineItem.getDate().getYear());
        this.month = lineItem.getDate().getMonth().toString();
        this.description = lineItem.getDescription();
        this.transactionType = lineItem.getTransactionType().toString();
        this.amount = lineItem.getAmount();
        this.statement = lineItem.getStatement();
    }
}
