import type { ClusterConfig } from './api';

/** Strip optional double quotes returned by backend clusterIdToName(). */
export function stripClusterQuotes(label: string): string {
  if (label.length >= 2 && label.startsWith('"') && label.endsWith('"')) {
    return label.slice(1, -1);
  }
  return label;
}

/**
 * Display label for a cluster row, matching backend clusterIdToName():
 * configured name, else 1-based ordinal (1, 2, …).
 */
export function getClusterDisplayName(
  index: number,
  cluster?: ClusterConfig,
  labelFromBackend?: string,
): string {
  if (labelFromBackend) {
    return stripClusterQuotes(labelFromBackend);
  }
  if (cluster?.clusterName?.trim()) {
    return cluster.clusterName.trim();
  }
  return String(index + 1);
}

/** Title for connection cards: "Cluster 1" or a configured / Aerospike cluster name. */
export function getClusterTitle(
  index: number,
  cluster?: ClusterConfig,
  labelFromBackend?: string,
): string {
  const displayName = getClusterDisplayName(index, cluster, labelFromBackend);
  if (cluster?.clusterName?.trim() || (labelFromBackend && !/^\d+$/.test(stripClusterQuotes(labelFromBackend)))) {
    return displayName;
  }
  return `Cluster ${displayName}`;
}
