package com.prospecta.service;

import org.springframework.stereotype.Service;
import com.prospecta.model.Cell;
import java.util.List;


@Service
public class CsvService {

    public Cell[][] calculateFormulas(List<String[]> csvData) {
        int rows = csvData.size();
        int cols = csvData.get(0).length;
        Cell[][] result = new Cell[rows][cols];

        // Initialize result array with Cell objects
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String cellValue = csvData.get(i)[j];
                Cell cell = new Cell();

                if (cellValue != null && cellValue.startsWith("=")) {
                    cell.setFormula(cellValue);
                } else {
                    cell.setValue(cellValue != null ? cellValue : "");
                }

                result[i][j] = cell;
            }
        }

        // Evaluate formulas
        evaluateFormulas(result);

        return result;
    }

    private void evaluateFormulas(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = cells[i][j];
                if (cell.getFormula() != null) {
                    String formula = cell.getFormula().substring(1); // Remove '='
                    String evaluatedValue = evaluateFormula(formula, cells);
                    cell.setValue(evaluatedValue);
                    cell.setFormula(null); // Remove the formula after evaluation
                }
            }
        }
    }

    private String evaluateFormula(String formula, Cell[][] cells) {
        formula = formula.trim();

        // Handle basic addition formulas (e.g., A1+A2, 4+5)
        String[] parts = formula.split("\\+");

        int result = 0;
        for (String part : parts) {
            part = part.trim();
            if (isCellReference(part)) {
                // Get the value from the referenced cell
                int[] indices = getCellIndices(part);
                // Check if the indices are within bounds
                if (indices[0] >= 0 && indices[0] < cells.length && indices[1] >= 0 && indices[1] < cells[0].length) {
                    String value = cells[indices[0]][indices[1]].getValue();
                    result += parseValue(value);
                } else {
                    throw new IllegalArgumentException("Cell reference out of bounds: " + part);
                }
            } else {
                // Add numeric value
                result += parseValue(part);
            }
        }

        return String.valueOf(result);
    }

    private boolean isCellReference(String value) {
        return value.matches("[A-Za-z]+[0-9]+");
    }

    private int[] getCellIndices(String cellReference) {
        // Cell reference is in format A1, B2, etc.
        int row = Integer.parseInt(cellReference.substring(1)) - 1;
        int col = cellReference.charAt(0) - 'A';
        return new int[]{row, col};
    }

    private int parseValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value: " + value);
        }
    }
}