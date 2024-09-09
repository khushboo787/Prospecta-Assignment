package com.prospecta.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prospecta.exception.FileEmptyException;
import com.prospecta.model.Cell;
import com.prospecta.service.CsvService;

@RestController
@RequestMapping("/csv")
public class CsvController {

    private final CsvService csvService;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/process")
    public ResponseEntity<ByteArrayResource> processCsv(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileEmptyException("The uploaded file is empty.");
        }

        
            List<String[]> csvData = parseCsv(file);

            // Process the data and calculate formulas
            Cell[][] processedData = csvService.calculateFormulas(csvData);

            // Create a CSV response with processed data
            ByteArrayResource resource = createCsv(processedData);

            // Return the processed CSV
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=processed.csv")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
          
    }

    // Parsing method for reading CSV data
    private List<String[]> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {

            List<String[]> records = new ArrayList<>();

            for (CSVRecord record : parser) {
                records.add(record.stream().toArray(String[]::new));
            }

            return records;
        }
    }

    // CSV creation method for writing CSV data
    private ByteArrayResource createCsv(Cell[][] cells) throws IOException {
        try (StringWriter out = new StringWriter();
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {

            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    Cell cell = cells[i][j];
                    String value = (cell.getValue() != null) ? cell.getValue() : "";
                    printer.print(value);
                }
                printer.println();
            }
            printer.flush();

            return new ByteArrayResource(out.toString().getBytes());
        }
    }
}