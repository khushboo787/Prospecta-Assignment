package com.prospecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CsvService {

    public String[][] calculateFormulas(List<String[]> csvData) {
        int rows = csvData.size();
        int cols = csvData.get(0).length;
        String[][] result = new String[rows][cols];

        // Iterate over each cell in the CSV
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String cellValue = csvData.get(i)[j];

                if (cellValue.startsWith("=")) {
                    result[i][j] = evaluateFormula(cellValue, result);
                } else {
                    result[i][j] = cellValue;
                }
            }
        }

        return result;
    }

    private String evaluateFormula(String formula, String[][] data) {
        formula = formula.substring(1); // Remove the '=' sign

        // Handle basic addition formulas (e.g., A1+A2, 4+5)
        String[] parts = formula.split("\\+");

        int result = 0;
        for (String part : parts) {
            part = part.trim();
            if (isCellReference(part)) {
                // Get the value from the referenced cell
                int[] indices = getCellIndices(part);
                result += Integer.parseInt(data[indices[0]][indices[1]]);
            } else {
                // Add numeric value
                result += Integer.parseInt(part);
            }
        }

        return String.valueOf(result);
    }

    private boolean isCellReference(String value) {
        return value.matches("[A-Za-z]+[0-9]+");
    }

    private int[] getCellIndices(String cellReference) {
        int row = Integer.parseInt(cellReference.substring(1)) - 1;
        int col = cellReference.charAt(0) - 'A';
        return new int[]{row, col};
    }
}
