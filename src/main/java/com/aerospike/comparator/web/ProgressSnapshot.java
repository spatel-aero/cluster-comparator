package com.aerospike.comparator.web;

public class ProgressSnapshot {
    private final long[] recordsProcessedPerCluster;
    private final long[] recordsProcessedThisScanPerCluster;
    private final long[] recordsMissingPerCluster;
    private final String[] clusterLabels;
    private final long recordsDifferent;
    private final long totalMissingRecords;
    private final long totalRecordsCompared;
    private final int partitionsComplete;
    private final int totalPartitions;
    private final boolean forceTerminated;
    private final String outputFile;
    private final String currentNamespace;
    private final String currentSetName;
    private final int currentNamespaceIndex;
    private final int namespaceCount;
    private final int currentSetIndex;
    private final int setCount;
    private final long elapsedThisScanMs;
    private final long elapsedTotalMs;
    private String state;
    private long completedAt;

    public ProgressSnapshot(long[] recordsProcessedPerCluster, long[] recordsProcessedThisScanPerCluster,
            long[] recordsMissingPerCluster, String[] clusterLabels, long recordsDifferent, long totalMissingRecords,
            long totalRecordsCompared, int partitionsComplete, int totalPartitions, boolean forceTerminated,
            String outputFile, String currentNamespace, String currentSetName, int currentNamespaceIndex,
            int namespaceCount, int currentSetIndex, int setCount, long elapsedThisScanMs, long elapsedTotalMs) {
        this.recordsProcessedPerCluster = recordsProcessedPerCluster;
        this.recordsProcessedThisScanPerCluster = recordsProcessedThisScanPerCluster;
        this.recordsMissingPerCluster = recordsMissingPerCluster;
        this.clusterLabels = clusterLabels;
        this.recordsDifferent = recordsDifferent;
        this.totalMissingRecords = totalMissingRecords;
        this.totalRecordsCompared = totalRecordsCompared;
        this.partitionsComplete = partitionsComplete;
        this.totalPartitions = totalPartitions;
        this.forceTerminated = forceTerminated;
        this.outputFile = outputFile;
        this.currentNamespace = currentNamespace;
        this.currentSetName = currentSetName;
        this.currentNamespaceIndex = currentNamespaceIndex;
        this.namespaceCount = namespaceCount;
        this.currentSetIndex = currentSetIndex;
        this.setCount = setCount;
        this.elapsedThisScanMs = elapsedThisScanMs;
        this.elapsedTotalMs = elapsedTotalMs;
    }

    public long[] getRecordsProcessedPerCluster() {
        return recordsProcessedPerCluster;
    }

    public long[] getRecordsProcessedThisScanPerCluster() {
        return recordsProcessedThisScanPerCluster;
    }

    public long[] getRecordsMissingPerCluster() {
        return recordsMissingPerCluster;
    }

    public String[] getClusterLabels() {
        return clusterLabels;
    }

    public long getRecordsDifferent() {
        return recordsDifferent;
    }

    public long getTotalMissingRecords() {
        return totalMissingRecords;
    }

    public long getTotalRecordsCompared() {
        return totalRecordsCompared;
    }

    public int getPartitionsComplete() {
        return partitionsComplete;
    }

    public int getTotalPartitions() {
        return totalPartitions;
    }

    public boolean isForceTerminated() {
        return forceTerminated;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public String getCurrentSetName() {
        return currentSetName;
    }

    public int getCurrentNamespaceIndex() {
        return currentNamespaceIndex;
    }

    public int getNamespaceCount() {
        return namespaceCount;
    }

    public int getCurrentSetIndex() {
        return currentSetIndex;
    }

    public int getSetCount() {
        return setCount;
    }

    public long getElapsedThisScanMs() {
        return elapsedThisScanMs;
    }

    public long getElapsedTotalMs() {
        return elapsedTotalMs;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }
}
