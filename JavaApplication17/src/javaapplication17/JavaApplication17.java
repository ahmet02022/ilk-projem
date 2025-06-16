package javaapplication17;

import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import javaapplication17.Visualizer;
import java.util.List;
import java.util.Arrays;
import javaapplication17.DataReader;  // DataReader'ı import ettik

/**
 * JavaApplication17 sınıfı, CSV dosyasından veri okur, 
 * temel istatistiksel analizleri yapar ve görselleştirmeler ile hipotez testi gerçekleştirir.
 */
public class JavaApplication17 {

    /**
     * Ana metod: Programın çalıştırılabilir kısmı.
     * Veriyi okur, analizleri yapar, görselleştirmeleri oluşturur ve sonuçları ekrana yazdırır.
     * 
     * @param args komut satırı argümanları
     * @throws IOException dosya okuma hatası
     * @throws CsvValidationException CSV doğrulama hatası
     */
   public static void main(String[] args) throws IOException, CsvValidationException {
        // ———————————————————————————————————
        // 1. Adım: Gerçek Veri Kümesini CSV Dosyasından Okuma
        // ———————————————————————————————————
        // DataReader sınıfı ile CSV dosyasını okuyoruz
        List<Double> mpgList = null;
        try {
            // "resources/vehicles.csv" dosyasını oku ve mpgList'e at
            mpgList = DataReader.readCombinedMpg("resources/vehicles.csv");
        } catch (IOException | CsvValidationException e) {
            // Eğer veri okuma hatası oluşursa hata mesajı yazdır
            System.err.println("Veri dosyası okunurken bir hata oluştu: " + e.getMessage());
            return; // Hata durumunda programı sonlandır
        }

        // ———————————————————————————————————
        // 2. Adım: Temel İstatistikler Hesaplama
        // ———————————————————————————————————
        // mpgList verisini kullanarak istatistik hesaplamalarını başlatıyoruz
        StatisticsCalculator istat = new StatisticsCalculator(mpgList);
        
        // Hesaplanan temel istatistikleri ekrana yazdırıyoruz
        System.out.println("=== Tanımlayıcı İstatistikler ===");
        System.out.printf("Örneklem sayısı: %d%n", istat.sampleSize()); // Örneklem büyüklüğü (N)
        System.out.printf("Ortalama (Mean): %.3f%n", istat.mean()); // Ortalama hesaplama
        System.out.printf("Medyan (Median): %.3f%n", istat.median()); // Medyan hesaplama
        System.out.printf("Varyans (Variance): %.3f%n", istat.variance()); // Varyans hesaplama
        System.out.printf("Standart Sapma: %.3f%n", istat.standardDeviation()); // Standart sapma hesaplama
        System.out.printf("Standart Hata: %.3f%n", istat.standardError()); // Standart hata hesaplama
        System.out.println();

        // ———————————————————————————————————
        // 3. Adım: Ortalama için %95 Güven Aralığı Hesaplama
        // ———————————————————————————————————
        // Ortalama için %95 güven aralığını hesaplıyoruz
        double[] ortalamaCI = istat.meanConfidenceInterval95();
        System.out.println("=== Güven Aralığı (Ortalama) ===");
        System.out.printf("Ortalama %%95 CI: [%.3f, %.3f]%n", ortalamaCI[0], ortalamaCI[1]); // Sonuçları yazdır
        System.out.println();

        // ———————————————————————————————————
        // 4. Adım: Örneklem Büyüklüğü Tahmini
        // ———————————————————————————————————
        // 90% güven aralığı ve ±0.1 hata ile gerekli örneklem büyüklüğünü hesaplıyoruz
        System.out.println("=== Örneklem Büyüklüğü Tahmini ===");
        long gerekliN = hesaplaGerekliOrneklem(istat.standardDeviation(), 0.1, 1.645); // Hata payı ve güven katsayısı ile hesapla
        System.out.printf("90%% güven, ±0.10 hata => N ≈ %d%n", gerekliN); // Hesaplanan örneklem büyüklüğünü yazdır
        System.out.println();

        // ———————————————————————————————————
        // 5. Adım: Veri Görselleştirme
        // ———————————————————————————————————
        // Histogram ve Box-Plot grafiklerini oluşturuyoruz
        System.out.println("=== Veri Görselleştirme ===");
        Visualizer.showHistogram(mpgList, "MPG Histogram"); // Histogram gösterimi
        Visualizer.showBoxPlot(mpgList, "Combined MPG Boxplot (From Vehicles.csv)");

        // ———————————————————————————————————
        // 6. Adım: Hipotez Testi (H₀: μ = 25)
        // ———————————————————————————————————
        // H₀: μ = 25 hipotezini test ediyoruz
        System.out.println("=== Hipotez Testi (H₀: μ = 25) ===");
        double tIstatistiği = HypothesisTester.calculateTStatistic(istat, 25.0); // t-istatistiği hesapla
        int serbestlik = istat.sampleSize() - 1; // Serbestlik derecesi (n - 1)
        double kritikT = HypothesisTester.getTCritical95(serbestlik); // Kritik t değeri al

        // Sonuçları yazdırıyoruz
        System.out.printf("t = %.3f, df = %d, kritik ±%.3f%n", tIstatistiği, serbestlik, kritikT);

        if (Math.abs(tIstatistiği) > kritikT) {
            System.out.println("Karar: H₀ reddedildi"); // H₀ reddedildiyse
        } else {
            System.out.println("Karar: H₀ reddedilemedi"); // H₀ reddedilemediyse
        }
        System.out.println();

        // ———————————————————————————————————
        // 7. Adım: Varyans için %95 Güven Aralığı Hesaplama
        // ———————————————————————————————————
        // Varyans için %95 güven aralığını hesaplıyoruz
        double[] varyansCI = istat.varianceConfidenceInterval95();
        System.out.println("=== Güven Aralığı (Varyans) ===");
        System.out.printf("Varyans %%95 CI: [%.3f, %.3f]%n", varyansCI[0], varyansCI[1]); // Sonuçları yazdır
    }

    /** 
     *  z: güven katsayısı (1.645 for %90) 
     *  s: standart sapma 
     *  E: izin verilen hata payı 
     *  Bu metot, örneklem büyüklüğünü hesaplar.
     */
    private static long hesaplaGerekliOrneklem(double s, double E, double z) {
        double n = Math.pow(z * s / E, 2); // Örneklem büyüklüğü hesaplaması
        return (long)Math.ceil(n); // Yukarıya yuvarla
    }
}
