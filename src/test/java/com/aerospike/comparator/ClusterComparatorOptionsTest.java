package com.aerospike.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ClusterComparatorOptionsTest {

    @Test
    void resolveThreadsToUse_capsDefaultAutoThreads() {
        assertEquals(32, ClusterComparatorOptions.resolveThreadsToUse(0, 112));
        assertEquals(16, ClusterComparatorOptions.resolveThreadsToUse(0, 16));
    }

    @Test
    void resolveThreadsToUse_usesAllCoresWhenMinusOne() {
        assertEquals(112, ClusterComparatorOptions.resolveThreadsToUse(-1, 112));
    }

    @Test
    void resolveThreadsToUse_honorsExplicitPositiveCount() {
        assertEquals(150, ClusterComparatorOptions.resolveThreadsToUse(150, 112));
    }

    @Test
    void isValidThreadsValue_rejectsInvalidNegativeValues() {
        assertTrue(ClusterComparatorOptions.isValidThreadsValue(-1));
        assertTrue(ClusterComparatorOptions.isValidThreadsValue(0));
        assertTrue(ClusterComparatorOptions.isValidThreadsValue(32));
        assertFalse(ClusterComparatorOptions.isValidThreadsValue(-2));
    }

    @Test
    void clusterIdToName_usesOneBasedOrdinalsWhenClusterNameNotSet() throws Exception {
        ClusterComparatorOptions options = new ClusterComparatorOptions(new String[] {
                "--hosts1", "h1:3000",
                "--hosts2", "h2:3000",
                "--namespaces", "test",
                "--action", "scan"
        });

        assertEquals("1", options.clusterIdToName(0));
        assertEquals("2", options.clusterIdToName(1));
    }

    @Test
    void clusterIdToName_usesConfiguredClusterNamesWhenSet() throws Exception {
        ClusterComparatorOptions options = new ClusterComparatorOptions(new String[] {
                "--hosts1", "h1:3000",
                "--hosts2", "h2:3000",
                "--clusterName1", "source",
                "--clusterName2", "target",
                "--namespaces", "test",
                "--action", "scan"
        });

        assertEquals("\"source\"", options.clusterIdToName(0));
        assertEquals("\"target\"", options.clusterIdToName(1));
    }
}
