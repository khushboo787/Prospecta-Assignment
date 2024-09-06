package com.prospecta.controller;

import java.io.*;
import java.util.*;
import java.util.stream.StreamSupport;

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
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // Parse the input CSV
            List<String[]> csvData = parseCsv(file);

            // Process the data and calculate formulas
            String[][] processedData = csvService.calculateFormulas(csvData);

            // Create a CSV response with processed data
            ByteArrayResource resource = createCsv(processedData);

            // Return the processed CSV
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=processed.csv")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Parsing method for reading CSV data
    private List<String[]> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {

            List<String[]> records = new ArrayList<>();

            for (CSVRecord record : parser) {
                records.add(StreamSupport.stream(record.spliterator(), false)
                        .toArray(String[]::new));
            }

            return records;
        }
    }

    // CSV creation method for writing CSV data
    private ByteArrayResource createCsv(String[][] data) throws IOException {
        try (StringWriter out = new StringWriter();
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {

            for (String[] row : data) {
                printer.printRecord((Object[]) row);
            }
            printer.flush();

            return new ByteArrayResource(out.toString().getBytes());
        }
    }
}
