package com.codetest.service;

import com.codetest.domain.Transaction;
import com.codetest.domain.TxnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jiang Wensi on 9/12/2020
 */
@Service
public class Processor {

    @Autowired
    Utility utility;

    public void process(String inputFilePath, String outputFilePath) throws IOException {
        List<String> txnLines = this.readFile(inputFilePath);
        List<Transaction> txns = this.readTransactions(txnLines);

        BigDecimal expense = new BigDecimal(0);
        BigDecimal income = new BigDecimal(0);

        Map<Integer, BigDecimal> monthExpenses = new LinkedHashMap<>();

        for (int i = 0; i < txns.size(); i++) {
            Transaction transaction = txns.get(i);
            if (transaction.getType().equals(TxnType.EXPENSE)) {
                expense = expense.add(transaction.getValue());

                int monthValue = transaction.getDate().getMonthValue();
                if (monthExpenses.get(monthValue) == null) {
                    monthExpenses.put(monthValue, new BigDecimal(0));
                }
                monthExpenses.put(monthValue, monthExpenses.get(monthValue).add(transaction.getValue()));

            } else {
                income = income.add(transaction.getValue());
            }
        }

        BigDecimal saving = income.subtract(expense);

        int maxExpenseMonthValue = 0;
        BigDecimal maxMonthlyExpense = BigDecimal.ZERO;

        for (Map.Entry<Integer, BigDecimal> entry : monthExpenses.entrySet()) {
            if (entry.getValue().compareTo(maxMonthlyExpense) > 0) {
                maxMonthlyExpense = entry.getValue();
                maxExpenseMonthValue = entry.getKey();
            }
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        Map<String, String> result = new LinkedHashMap<>();

        result.put("Total Income", df.format(income));
        result.put("Total Expenses", df.format(expense));
        result.put("Total Savings", df.format(saving));
        result.put("Top Expenses Month", df.format(maxMonthlyExpense) + " @" + utility.month(maxExpenseMonthValue));

        this.generateReport(outputFilePath,result);
    }

    public List<String> readFile(String path) throws IOException {
        List<String> results = new ArrayList<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while ((line = reader.readLine()) != null) {
                results.add(line);
            }
        }
        return results;
    }

    public List<Transaction> readTransactions(List<String> text) {
        List<Transaction> transactions = new ArrayList<>();
        if (text != null) {
            for (int i = 0; i < text.size(); i++) {
                String[] txn = text.get(i).split(",");
                LocalDate date = LocalDate.parse(txn[0], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                BigDecimal value = new BigDecimal(txn[1]);
                TxnType type;
                if (value.compareTo(BigDecimal.ZERO) < 0) {
                    type = TxnType.EXPENSE;
                } else {
                    type = TxnType.INCOME;
                }
                transactions.add(new Transaction(date, value.abs().setScale(2, RoundingMode.HALF_EVEN), type, txn[2]));
            }
        }
        return transactions;
    }

    public void generateReport(String path, Map<String, String> resultMap) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                writer.write(entry.getKey() + " : " + entry.getValue());
                writer.write("\n");
            }
        }
    }

}
