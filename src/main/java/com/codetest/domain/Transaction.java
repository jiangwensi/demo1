package com.codetest.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Created by Jiang Wensi on 9/12/2020
 */
public class Transaction {
    private LocalDate date;
    private BigDecimal value;
    private TxnType type;
    private String description;

    public Transaction(LocalDate date, BigDecimal value, TxnType type, String description) {
        this.date = date;
        this.value = value.setScale(2, RoundingMode.HALF_EVEN);
        this.type = type;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public TxnType getType() {
        return type;
    }

    public void setType(TxnType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!date.equals(that.date)) return false;
        if (!value.equals(that.value)) return false;
        if (type != that.type) return false;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
