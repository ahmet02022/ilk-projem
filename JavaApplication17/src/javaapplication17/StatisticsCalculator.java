package javaapplication17;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticsCalculator {

    private final List<Double> data;
    private final int n;

    public StatisticsCalculator(List<Double> data) {
        // orijinali bozmamak için kopya alıyoruz ve sıralıyoruz
        this.data = new ArrayList<>(data);
        Collections.sort(this.data);
        this.n = this.data.size();
    }

    /**
     * Örneklem sayısı (N)
     */
    public int sampleSize() {
        return n;
    }

    /**
     * Ortalama (mean) değeri hesaplar.
     */
    public double mean() {
        double toplam = 0.0;
        for (double v : data) {
            toplam += v;
        }
        return toplam / n;
    }

    /**
     * Medyan (median) değeri hesaplar.
     */
    public double median() {
        if (n == 0) {
            return Double.NaN;
        }
        int ortanca = n / 2;
        if (n % 2 == 1) {
            // Tek sayı‍da ortadaki eleman
            return data.get(ortanca);
        } else {
            // Çift sayı‍da iki ortanca değerin ortalaması
            return (data.get(ortanca - 1) + data.get(ortanca)) / 2.0;
        }
    }

    /**
     * Örneklem varyansı (s²), n−1 ile böler.
     */
    public double variance() {
        double m = mean();
        double kareToplam = 0.0;
        for (double v : data) {
            double fark = v - m;
            kareToplam += fark * fark;
        }
        return kareToplam / (n - 1);
    }

    /**
     * Standart sapma (s = √variance).
     */
    public double standardDeviation() {
        return Math.sqrt(variance());
    }

    /**
     * Standart hata (s/√n) hesaplar.
     */
    public double standardError() {
        return standardDeviation() / Math.sqrt(n);
    }

    /**
     * Ortalama için %95 güven aralığı yarı-genişliğini (half-width) döner.
     * Burada sabit z = 1.96 kullanılır.
     */
    public double meanCiHalfWidth95() {
        final double Z_95 = 1.96;
        return Z_95 * standardError();
    }

    /**
     * Ortalama için %95 güven aralığını [alt, üst] formatında döner.
     */
    public double[] meanConfidenceInterval95() {
        double orta = mean();
        double yaricevre = meanCiHalfWidth95();
        return new double[]{orta - yaricevre, orta + yaricevre};
    }

    /**
     * Varyans için %95 güven aralığını [alt, üst] biçiminde hesaplar. Formül:
     * [(n−1)s² / χ²₁₋α/₂, (n−1)s² / χ²_{α/₂}]
     */
    public double[] varianceConfidenceInterval95() {
        int df = n - 1;
        double s2 = variance();
        double chi2Low = chi2LowerCritical(df);
        double chi2High = chi2UpperCritical(df);

        double alt = df * s2 / chi2Low;
        double ust = df * s2 / chi2High;
        return new double[]{alt, ust};
    }

    // ——— χ² kritik değerleri (df ≤ 10 için tablo) ———
    private static double chi2LowerCritical(int df) {
        switch (df) {
            case 1:
                return 5.0239;
            case 2:
                return 7.3778;
            case 3:
                return 9.3484;
            case 4:
                return 11.1433;
            case 5:
                return 12.8325;
            case 6:
                return 14.4494;
            case 7:
                return 16.0128;
            case 8:
                return 17.5346;
            case 9:
                return 19.0228;
            case 10:
                return 20.4832;
            default:
                return df;      // basit yaklaşık
        }
    }

    private static double chi2UpperCritical(int df) {
        switch (df) {
            case 1:
                return 0.0009823;
            case 2:
                return 0.05064;
            case 3:
                return 0.2158;
            case 4:
                return 0.4844;
            case 5:
                return 0.8312;
            case 6:
                return 1.2373;
            case 7:
                return 1.6899;
            case 8:
                return 2.1797;
            case 9:
                return 2.7004;
            case 10:
                return 3.2469;
            default:
                return 1.0;      // basit yaklaşık
        }
    }

}
