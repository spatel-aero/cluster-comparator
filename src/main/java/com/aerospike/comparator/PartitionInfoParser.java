package com.aerospike.comparator;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses Aerospike {@code partition-info} responses. The first semicolon-separated
 * entry is a header row naming each colon-separated field; data rows use the same
 * column order. Trailing {@code version} / {@code final_version} fields may contain
 * colons and are parsed as the last two columns.
 */
final class PartitionInfoParser {

    private PartitionInfoParser() {
    }

    static String[] parseHeader(String headerLine) {
        if (headerLine == null || headerLine.isEmpty()) {
            throw new IllegalArgumentException("partition-info header line is missing");
        }
        String trimmed = headerLine.trim();
        if (trimmed.endsWith(";")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        String[] columns = trimmed.split(":", -1);
        if (columns.length < 2 || !"namespace".equals(columns[0])) {
            throw new IllegalArgumentException(
                    "partition-info header must start with 'namespace', not: " + headerLine);
        }
        return columns;
    }

    static PartitionData parseRow(String headerLine, String dataLine) {
        String[] columnNames = parseHeader(headerLine);
        String[] values = splitRow(dataLine, columnNames);
        Map<String, String> fields = new HashMap<>(columnNames.length);
        for (int i = 0; i < columnNames.length; i++) {
            fields.put(columnNames[i], values[i]);
        }
        return new PartitionData(fields);
    }

    static String[] splitRow(String dataLine, String[] columnNames) {
        if (dataLine == null || dataLine.isEmpty()) {
            throw new IllegalArgumentException("partition-info data line is empty");
        }
        String trimmed = dataLine.trim();
        int expectedColumns = columnNames.length;
        String[] parts = trimmed.split(":", -1);

        if (parts.length == expectedColumns) {
            return parts;
        }
        if (parts.length < expectedColumns) {
            throw new IllegalArgumentException(String.format(
                    "partition-info row has %d fields, expected %d: %s",
                    parts.length, expectedColumns, dataLine));
        }

        int versionIndex = indexOf(columnNames, "version");
        int finalVersionIndex = indexOf(columnNames, "final_version");
        if (versionIndex >= 0 && finalVersionIndex == expectedColumns - 1
                && finalVersionIndex == versionIndex + 1) {
            String[] columns = new String[expectedColumns];
            System.arraycopy(parts, 0, columns, 0, versionIndex);
            columns[versionIndex] = join(parts, versionIndex, parts.length - 1);
            columns[finalVersionIndex] = parts[parts.length - 1];
            return columns;
        }

        throw new IllegalArgumentException(String.format(
                "partition-info row has %d fields, expected %d, and could not reconcile trailing columns: %s",
                parts.length, expectedColumns, dataLine));
    }

    private static int indexOf(String[] columnNames, String name) {
        for (int i = 0; i < columnNames.length; i++) {
            if (name.equals(columnNames[i])) {
                return i;
            }
        }
        return -1;
    }

    private static String join(String[] parts, int fromInclusive, int toExclusive) {
        StringBuilder sb = new StringBuilder(parts[fromInclusive]);
        for (int i = fromInclusive + 1; i < toExclusive; i++) {
            sb.append(':').append(parts[i]);
        }
        return sb.toString();
    }
}
