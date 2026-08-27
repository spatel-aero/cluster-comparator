package com.aerospike.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.aerospike.client.Key;
import com.aerospike.comparator.RecordComparator.DifferenceType;

public class ConsoleDifferenceHandlerTest {

    @Test
    void formatClusterListUsesOneBasedOrdinals() throws Exception {
        ClusterComparatorOptions options = new ClusterComparatorOptions(new String[] {
                "--hosts1", "h1:3000",
                "--hosts2", "h2:3000",
                "--namespaces", "test",
                "--action", "scan"
        });

        assertEquals("[2]", ConsoleDifferenceHandler.formatClusterList(Collections.singletonList(1), options));
        assertEquals("[1, 2]", ConsoleDifferenceHandler.formatClusterList(Arrays.asList(0, 1), options));
    }

    @Test
    void formatClusterListUsesConfiguredClusterNames() throws Exception {
        ClusterComparatorOptions options = new ClusterComparatorOptions(new String[] {
                "--hosts1", "h1:3000",
                "--hosts2", "h2:3000",
                "--clusterName1", "source",
                "--clusterName2", "target",
                "--namespaces", "test",
                "--action", "scan"
        });

        assertEquals("[\"target\"]", ConsoleDifferenceHandler.formatClusterList(Collections.singletonList(1), options));
    }

    @Test
    void recordDifferenceDescriptionUsesClusterLabels() throws Exception {
        ClusterComparatorOptions options = new ClusterComparatorOptions(new String[] {
                "--hosts1", "h1:3000",
                "--hosts2", "h2:3000",
                "--namespaces", "test",
                "--action", "scan"
        });
        Key key = new Key("test", "name", 2);

        DifferenceCollection differences = new DifferenceCollection(Arrays.asList(0, 1));
        DifferenceSet diffSet = new DifferenceSet(key, false, null, 0, 1);
        diffSet.pushPath("test");
        diffSet.pushPath("name");
        diffSet.pushPath("city");
        diffSet.addDifference(DifferenceType.CONTENTS, "Melbourne", "Sydney", 0, 1);
        differences.add(diffSet);

        String output = differences.getBinsDifferent().toHumanString(Collections.emptyList(), options);

        assertTrue(output.contains("Bin \"city\" has different values [1] vs [2]"));
        assertFalse(output.contains("[0]"));
    }
}
