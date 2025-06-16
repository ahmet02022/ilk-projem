package javaapplication17;

public class HypothesisTester {

    
    /**
     * İki uçlu t-testi istatistiğini hesaplar:
     * t = (örneklem ortalaması - H₀ değeri) / (standart hata)
     *
     * @param calculator     İstatistik hesaplayıcı (StatisticsCalculator örneği)
     * @param nullMean       Test edilen H₀ ortalaması (μ₀)
     * @return               Hesaplanan t-istatistiği
     */
    public static double calculateTStatistic(StatisticsCalculator calculator, double nullMean) {
        double sampleMean = calculator.mean();
        double standardError = calculator.standardError();
        return (sampleMean - nullMean) / standardError;
    }

    /**
     * Belirtilen serbestlik derecesi (df) için iki uçlu %95 güven t-kritik değerini döner.
     * df ≤ 10 için tablo değerleri, df > 10 için normal yaklaşımı (1.96) kullanılır.
     *
     * @param degreesOfFreedom  Serbestlik derecesi (n - 1)
     * @return                  Kritik t değeri (± işareti çağıran kodda ele alınır)
     */
    public static double getTCritical95(int degreesOfFreedom) {
        switch (degreesOfFreedom) {
            case 1:  return 12.706;
            case 2:  return 4.303;
            case 3:  return 3.182;
            case 4:  return 2.776;
            case 5:  return 2.571;
            case 6:  return 2.447;
            case 7:  return 2.365;
            case 8:  return 2.306;
            case 9:  return 2.262;
            case 10: return 2.228;
            default: return 1.96;  // df > 10 için yaklaşık Z değeri
        }
    }
}
