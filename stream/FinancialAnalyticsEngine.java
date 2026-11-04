package id.ac.trinita.stream;

import java.util.*;
import java.util.stream.Collectors;

import id.ac.trinita.stream.Transaction;
import id.ac.trinita.stream.TransactionType;

public class FinancialAnalyticsEngine {

    /**
     * Mengambil 3 transaksi transfer bernilai terbesar untuk rekening tertentu.
     */
    public List<Transaction> getTop3LargestTransfers(List<Transaction> transactions, String accountId) {
        return transactions.stream()
            .filter(t -> t.accountId().equals(accountId))
            .filter(t -> t.type() == TransactionType.TRANSFER)
            .sorted(Comparator.comparingDouble(Transaction::amount).reversed())
            .limit(3)
            .toList(); // Java 16+ Unmodifiable List shortcut
    }

    /**
     * Mengelompokkan transaksi berdasarkan Kota dan menjumlahkan total perputaran uang.
     */
    public Map<String, Double> calculateTotalVolumePerCity(List<Transaction> transactions) {
        return transactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::locationCity,
                Collectors.summingDouble(Transaction::amount)
            ));
    }

    /**
     * Mempartisi transaksi menjadi Transaksi Aman vs Transaksi Mencurigakan (Fraud Risk).
     */
    public Map<Boolean, List<Transaction>> partitionBySuspiciousRisk(List<Transaction> transactions) {
        return transactions.stream()
            .collect(Collectors.partitioningBy(Transaction::isFlaggedSuspicious));
    }

    /**
     * Menghitung statistik lengkap nominal transaksi (Min, Max, Avg, Sum, Count).
     */
    public DoubleSummaryStatistics getTransactionSummary(List<Transaction> transactions) {
        return transactions.stream()
            .collect(Collectors.summarizingDouble(Transaction::amount));
    }
    /**
     * Menghitung jumlah transaksi, total nominal, dan rata-rata
     * menggunakan Collectors.teeing.
     */
    /**
     * Menghitung jumlah transaksi, total nominal, dan rata-rata
     * menggunakan Collectors.teeing.
     */
    public Summary getTransactionSummaryWithTeeing(List<Transaction> transactions) {
        return transactions.stream()
            .collect(Collectors.teeing(
                Collectors.counting(),
                Collectors.reducing(
                    java.math.BigDecimal.ZERO,
                    t -> java.math.BigDecimal.valueOf(t.amount()),
                    java.math.BigDecimal::add
                ),
                (count, total) -> new Summary(
                    count,
                    total,
                    count == 0
                        ? java.math.BigDecimal.ZERO
                        : total.divide(
                            java.math.BigDecimal.valueOf(count),
                            2,
                            java.math.RoundingMode.HALF_UP
                        )
                )
            ));
    }

    /**
     * Menyimpan ringkasan transaksi menggunakan BigDecimal.
     */
    public record Summary(
        long count,
        java.math.BigDecimal total,
        java.math.BigDecimal average
    ) {}
}