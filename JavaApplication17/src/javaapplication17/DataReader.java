/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication17;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ahmtm
 */
public class DataReader {
     /**
     * CSV dosyasındaki "combined mpg" sütununu okur ve bir liste olarak döner.
     *
     * @param filePath CSV dosyasının yolu
     * @return "combined mpg" verilerini içeren liste
     * @throws IOException Dosya okuma hatası
     */
    public static List<Double> readCombinedMpg(String filePath) throws IOException, CsvValidationException {
        List<Double> mpgList = new ArrayList<>();

        // CSV dosyasını oku
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();  // İlk satır başlık
            if (header == null) {
                throw new IOException("CSV dosyasında başlık bulunamadı.");
            }

            // 'combined mpg' sütununu bulalım  her bir aracın yakıt verimliliği (MPG) değerleridir
            int mpgIndex = -1;
            for (int i = 0; i < header.length; i++) {
                if ("comb08".equalsIgnoreCase(header[i])) {
                    mpgIndex = i;
                    break;
                }
            }

            // Eğer 'combined mpg' sütunu bulunamazsa hata ver
            if (mpgIndex == -1) {
                throw new IOException("'combined mpg' sütunu bulunamadı.");
            }

            // Verileri oku ve mpg verilerini listeye ekle
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length > mpgIndex && !line[mpgIndex].isEmpty()) {
                    mpgList.add(Double.parseDouble(line[mpgIndex]));
                }
            }
        }

        return mpgList;
    }
}
