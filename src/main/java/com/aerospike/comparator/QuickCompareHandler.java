package com.aerospike.comparator;

import java.io.IOException;

public interface QuickCompareHandler {
    void handle(String namespace, int partitionId, long[] records, long[] tombstones, long[] netCounts) throws IOException;
}
