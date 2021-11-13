/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.util;

import java.util.HashMap;

/**
 *
 * @author ryo20
 */
public class HashMapWrapper {
    HashMap<Long, Integer> map;
    
    public HashMapWrapper(HashMap<Long, Integer> map) {
        this.map = map;
    }
}
