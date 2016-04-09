/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.clusterlabeling.numberofwordscaling;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shadiq
 */
public class ClusterLabelScalingPoisson implements ClusterLabelScaling {

    private double mean;
    private Map<Integer, Double> scalingFactorMap = new HashMap<Integer, Double>();

    public ClusterLabelScalingPoisson(){
        this.mean = 4;
    }

    public ClusterLabelScalingPoisson(double mean){
        this.mean = mean;
    }    

    public double getScale(int numberOfWord) {
        if (scalingFactorMap.containsKey(numberOfWord)){
            return scalingFactorMap.get(numberOfWord);
        } else {
            double scalingFactor = PoissonScalingFactor(numberOfWord, mean);
            scalingFactorMap.put(numberOfWord, scalingFactor);
            return scalingFactor;
        }
    }

    private static double PoissonScalingFactor(int numOfWord, double mean){
        double val = (Math.pow(mean, numOfWord)*(Math.pow(Math.E, -mean)))/fact(numOfWord);
        return val;
    }

    /**
        * Calculate the factorial of n.
        *
        * @param n the number to calculate the factorial of.
        * @return n! - the factorial of n.
        */
    private static int fact(int n) {
        switch (n){
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 6;
            case 4:
                return 24;
            case 5:
                return 120;
            case 6:
                return 720;
            case 7:
                return 5040;
            case 8:
                return 40320;
            case 9:
                return 362880;
            case 10:
                return 3628800;
            default:
                 if (n <= 1) {
                     return 1;
                 } else {
                    return n * fact(n - 1);
                }
        }
    }

    public static void main (String[] args){
        double mean = 2.5d;
        double a = 0;
        for (int i = 0; i < 11; i++) {
            a = PoissonScalingFactor(i, mean);
            System.out.println(i+" = "+a);
        }
        System.out.println("factorial 12="+fact(12));
    }
}
