package com.aerospike.comparator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.aerospike.client.Key;
import com.aerospike.comparator.RecordComparator.DifferenceType;

public class DifferenceCollectionTest {

    @Test
    void toStringUsesOneBasedClusterLabels() {
        Key key = new Key("test", "name", 2);
        DifferenceCollection differences = new DifferenceCollection(Arrays.asList(0, 1));
        DifferenceSet diffSet = new DifferenceSet(key, false, null, 0, 1);
        diffSet.pushPath("test");
        diffSet.pushPath("name");
        diffSet.pushPath("city");
        diffSet.addDifference(DifferenceType.CONTENTS, "Melbourne", "Sydney", 0, 1);
        differences.add(diffSet);

        String output = differences.toString();

        assertTrue(output.contains("[1] vs [2]"));
        assertFalse(output.contains("[0]"));
    }

    @Test
    void humanStringUsesResolverClusterLabels() throws Exception {
        ClusterComparatorOptions options = new ClusterComparatorOptions(new String[] {
                "--hosts1", "h1:3000",
                "--hosts2", "h2:3000",
                "--clusterName1", "source",
                "--clusterName2", "target",
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

        String output = differences.getBinsDifferent().toHumanString(null, options);

        assertTrue(output.contains("[\"source\"] vs [\"target\"]"));
    }
}
