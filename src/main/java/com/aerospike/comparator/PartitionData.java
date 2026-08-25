package com.aerospike.comparator;

import java.util.Map;

public class PartitionData {
    private final String namespace;
    private final int partitionId;
    private final String state;
    private final int nReplicas;
    private final int replica;
    private final int nDupl;
    private final String workingMaster;
    private final long emigrates;
    private final long leadEmigrates;
    private final long immigrates;
    private final long records;
    private final long tombstones;

    PartitionData(Map<String, String> fields) {
        namespace = required(fields, "namespace");
        partitionId = parseInt(fields, "partition");
        state = fields.getOrDefault("state", "");
        nReplicas = parseInt(fields, "n_replicas");
        replica = parseInt(fields, "replica");
        nDupl = parseInt(fields, "n_dupl");
        workingMaster = fields.getOrDefault("working_master", "");
        emigrates = parseLong(fields, "emigrates");
        leadEmigrates = parseLong(fields, "lead_emigrates");
        immigrates = parseLong(fields, "immigrates");
        records = parseLong(fields, "records");
        tombstones = parseLong(fields, "tombstones");
    }

    static PartitionData parse(String headerLine, String dataLine) {
        return PartitionInfoParser.parseRow(headerLine, dataLine);
    }

    private static String required(Map<String, String> fields, String name) {
        String value = fields.get(name);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("partition-info row is missing required field: " + name);
        }
        return value;
    }

    private static int parseInt(Map<String, String> fields, String name) {
        String value = fields.get(name);
        if (value == null || value.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    private static long parseLong(Map<String, String> fields, String name) {
        String value = fields.get(name);
        if (value == null || value.isEmpty()) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public String getNamespace() {
        return namespace;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public String getState() {
        return state;
    }

    public int getnReplicas() {
        return nReplicas;
    }

    public int getReplica() {
        return replica;
    }

    public int getnDupl() {
        return nDupl;
    }

    public String getWorkingMaster() {
        return workingMaster;
    }

    public long getEmigrates() {
        return emigrates;
    }

    public long getLeadEmigrates() {
        return leadEmigrates;
    }

    public long getImmigrates() {
        return immigrates;
    }

    public long getRecords() {
        return records;
    }

    public long getTombstones() {
        return tombstones;
    }
}
