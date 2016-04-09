/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.clusterlabeling;

import java.util.Map;
import newsclusteringlucene.utils.ClusterStat;
import newsclusteringlucene.utils.CollectionStat;

/**
 *
 * @author shadiq
 */
public interface ClusterLabeling {

    public int getNumDoc();

    /**
     * Returns the most recommended Cluster Label by the underlying algorithm
     * @return
     */
    public String getClusterLabel();

    /**
     * Returns a list of cluster labels starting from the highest score.
     * @return
     */
    public String[] getClusterLabels();

    public CollectionStat getCollectionStatistics();

    public ClusterStat getClusterStatistics();
    
    public Map<String, Double> getClusterLabelsScore();
}
