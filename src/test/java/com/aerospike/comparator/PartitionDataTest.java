package com.aerospike.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class PartitionDataTest {

    private static final String HEADER_6_0 = "namespace:partition:state:n_replicas:replica:"
            + "n_dupl:working_master:emigrates:lead_emigrates:immigrates:records:"
            + "tombstones:regime:version:final_version";

    private static final String HEADER_7_2 = "namespace:partition:state:n_replicas:replica:"
            + "n_dupl:working_master:proxy_dst:emigrates:lead_emigrates:immigrates:records:"
            + "tombstones:regime:version:final_version";

    private static final String HEADER_8_1 = "namespace:partition:state:n_replicas:replica:"
            + "succession:n_dupl:working_master:proxy_dst:emigrates:lead_emigrates:"
            + "immigrates:records:tombstones:regime:version:final_version";

    private static final String HEADER_8_1_1 = "namespace:partition:state:n_replicas:replica:"
            + "succession:n_dupl:working_master:proxy_dst:emigrates:lead_emigrates:"
            + "immigrates:records:tombstones:regime:tree_id:version:final_version";

    @Test
    void parsesCurrentServerFormat() {
        String row = "test:4091:S:1:0:0:0:BB92339D50DDCC2:BB92339D50DDCC2:0:0:0:0:0:0:1:abe218022e92.0.mp-:abe218022e92.0.mp-.";

        PartitionData data = PartitionData.parse(HEADER_8_1_1, row);

        assertEquals("test", data.getNamespace());
        assertEquals(4091, data.getPartitionId());
        assertEquals("S", data.getState());
        assertEquals(1, data.getnReplicas());
        assertEquals(0, data.getReplica());
        assertEquals(0, data.getnDupl());
        assertEquals("BB92339D50DDCC2", data.getWorkingMaster());
        assertEquals(0, data.getEmigrates());
        assertEquals(0, data.getLeadEmigrates());
        assertEquals(0, data.getImmigrates());
        assertEquals(0, data.getRecords());
        assertEquals(0, data.getTombstones());
    }

    @Test
    void parsesServer6Format() {
        String row = "test:42:S:2:1:0:ABCD1234:3:4:5:100:7:9:ver.1:ver.2";

        PartitionData data = PartitionData.parse(HEADER_6_0, row);

        assertEquals("test", data.getNamespace());
        assertEquals(42, data.getPartitionId());
        assertEquals("ABCD1234", data.getWorkingMaster());
        assertEquals(3, data.getEmigrates());
        assertEquals(4, data.getLeadEmigrates());
        assertEquals(5, data.getImmigrates());
        assertEquals(100, data.getRecords());
        assertEquals(7, data.getTombstones());
    }

    @Test
    void parsesServer72FormatWithProxyDst() {
        String row = "test:99:S:1:0:0:MASTERNODE:PROXYNODE:1:2:3:50:6:0:ver.1:ver.2";

        PartitionData data = PartitionData.parse(HEADER_7_2, row);

        assertEquals("MASTERNODE", data.getWorkingMaster());
        assertEquals(1, data.getEmigrates());
        assertEquals(2, data.getLeadEmigrates());
        assertEquals(3, data.getImmigrates());
        assertEquals(50, data.getRecords());
        assertEquals(6, data.getTombstones());
    }

    @Test
    void parsesServer81FormatWithSuccession() {
        String row = "test:17:Z:2:-1:3:1:NODE1111:NODE2222:0:0:0:25:3:0:ver.1:ver.2";

        PartitionData data = PartitionData.parse(HEADER_8_1, row);

        assertEquals(-1, data.getReplica());
        assertEquals(1, data.getnDupl());
        assertEquals("NODE1111", data.getWorkingMaster());
        assertEquals(25, data.getRecords());
        assertEquals(3, data.getTombstones());
    }

    @Test
    void parsesVersionFieldContainingColons() {
        String row = "test:1:S:1:0:0:NODE:0:0:0:10:2:0:part:extra:version:final";

        PartitionData data = PartitionData.parse(HEADER_6_0, row);

        assertEquals("NODE", data.getWorkingMaster());
        assertEquals(10, data.getRecords());
        assertEquals(2, data.getTombstones());
    }

    @Test
    void rejectsRowWithTooFewFields() {
        assertThrows(IllegalArgumentException.class,
                () -> PartitionData.parse(HEADER_6_0, "test:1:S"));
    }

    @Test
    void rejectsMissingHeader() {
        assertThrows(IllegalArgumentException.class,
                () -> PartitionData.parse("", "test:1:S:2:1:0:NODE:0:0:0:1:0:0:v1:v2"));
    }
}
