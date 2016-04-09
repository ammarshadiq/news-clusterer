/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.utils;

/**
 *
 * @author Shadev
 */
public interface DistanceCalculator<T> {
    public double getDistance(T a,T b);
}
