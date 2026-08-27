package com.aerospike.comparator;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.aerospike.client.Key;
import com.aerospike.client.command.Buffer;
import com.aerospike.comparator.ClusterComparatorOptions.CompareMode;
import com.aerospike.comparator.dbaccess.RecordMetadata;

public class ConsoleDifferenceHandler implements MissingRecordHandler, RecordDifferenceHandler, QuickCompareHandler {
    private final ClusterNameResolver resolver;
    public ConsoleDifferenceHandler(ClusterNameResolver resolver) {
        this.resolver = resolver;
    }
    
    @Override
    public void handle(CompareMode compareMode, int partitionId, Key key, List<Integer> missingFromClusters, boolean hasRecordLevelDifferences, RecordMetadata[] recordMetadatas) throws IOException {
        String recordMetadataDesc = "";
        if (recordMetadatas != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(" LUTS: ");
            for (int i = 0; i < recordMetadatas.length; i++) {
                RecordMetadata metadata = recordMetadatas[i];
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(resolver.clusterIdToName(i)).append("->");
                if (metadata == null) {
                    sb.append("null");
                }
                else {
                    sb.append(metadata.getLastUpdateMs());
                }
            }
            recordMetadataDesc = sb.toString();
        }
        List<String> clusterLabels = missingFromClusters.stream()
                .map(resolver::clusterIdToName)
                .collect(Collectors.toList());
        printMissingOrOverlapRecord(compareMode, key, clusterLabels, recordMetadataDesc);
    }

    static String formatClusterList(List<Integer> clusterIds, ClusterNameResolver resolver) {
        return clusterIds.stream()
                .map(resolver::clusterIdToName)
                .collect(Collectors.toList())
                .toString();
    }

    private void printMissingOrOverlapRecord(CompareMode compareMode, Key key, List<String> clusterLabels, String recordMetadataDesc) {
        if (compareMode == CompareMode.FIND_OVERLAP) {
            System.out.printf("OVERLAPPING RECORD:(%s,%s,%s,%s) Found on clusters %s%s\n", key.namespace,key.setName, key.userKey, Buffer.bytesToHexString(key.digest),
                    clusterLabels, recordMetadataDesc);
        }
        else {
            System.out.printf("MISSING RECORD:(%s,%s,%s,%s) Missing from clusters %s%s\n", key.namespace,key.setName, key.userKey, Buffer.bytesToHexString(key.digest),
                    clusterLabels, recordMetadataDesc);
        }
    }

    @Override
    public void handle(String namespace, int partitionId, long[] records, long[] tombstones, long[] netCounts) throws IOException {
        System.out.printf("QUICK COMPARE PARTITION:(%s,%d) %s\n",
                namespace,
                partitionId,
                CsvDifferenceHandler.buildQuickCompareHumanReadable(netCounts, resolver));
    }

    @Override
    public void handle(int partitionId, Key key, DifferenceCollection differences, List<Integer> missingFromClusters, RecordMetadata[] recordMetadatas)
            throws IOException {
        
        if (differences.isQuickCompare()) {
            System.out.printf("DIFFERENCES: %s,%s,%s,%s\n", key.namespace, key.setName, key.userKey, Buffer.bytesToHexString(key.digest));
        }
        else {
            String diffDescription = differences.getBinsDifferent().toHumanString(missingFromClusters, resolver);
            System.out.printf("DIFFERENCES: %s,%s,%s,%s,%s\n", key.namespace, key.setName, key.userKey, Buffer.bytesToHexString(key.digest), diffDescription);
        }
    }
}
