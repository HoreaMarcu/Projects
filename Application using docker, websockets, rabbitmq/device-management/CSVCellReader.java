package com.example.devicemanagement;

import com.example.devicemanagement.services.DeviceService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CSVCellReader {

//    private static final String CSV_FILE = "D:\\DS\\Assignemnt1-BE\\device-management\\device-management\\sensor.csv";
    private static final String CSV_FILE = "sensor.csv";
    private static int currentCsvFileLine = 0;

    @Autowired
    private MessagePublisher messagePublisher;

    @Autowired
    private DeviceService deviceService;

    @Scheduled(fixedRate = 10000)
    public void readFile() {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE))) {
            for (int i = 0; i < currentCsvFileLine; i++) {
                reader.readNext();
            }

            String[] nextLine = reader.readNext();
            if (nextLine != null && nextLine.length > 0) {
                UUID id = deviceService.findDevices().get(currentCsvFileLine%2).getId();
                Integer max = deviceService.findDevices().get(currentCsvFileLine%2).getMaxHourlyEnergyConsumption();

                messagePublisher.publishMessage(new MessageRabbitMQ(LocalDateTime.now().toString(),id, nextLine[0],max));

                currentCsvFileLine++;
            }
        } catch (IOException | NumberFormatException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
