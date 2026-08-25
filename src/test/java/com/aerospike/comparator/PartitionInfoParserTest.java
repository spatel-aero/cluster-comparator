package com.aerospike.comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class PartitionInfoParserTest {

    @Test
    void parseHeaderStripsTrailingSemicolon() {
        String[] columns = PartitionInfoParser.parseHeader(
                "namespace:partition:records:tombstones;");

        assertArrayEquals(
                new String[] {"namespace", "partition", "records", "tombstones"},
                columns);
    }

    @Test
    void splitRowReconcilesExtraColonsInVersionField() {
        String[] columnNames = new String[] {
                "namespace", "partition", "records", "version", "final_version"
        };

        String[] values = PartitionInfoParser.splitRow(
                "test:1:10:part:of:version:final", columnNames);

        assertEquals("test", values[0]);
        assertEquals("1", values[1]);
        assertEquals("10", values[2]);
        assertEquals("part:of:version", values[3]);
        assertEquals("final", values[4]);
    }
}
