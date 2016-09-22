/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import model.Pizza;

/**
 *
 * @author Lovrenco
 */
public class ArrayListPizzaDisplayer {
    
    
    public static HashMap<Integer, Integer> getElencoPizzeToHashMap(ArrayList<Pizza> elencoPizze){ // <idPizza , quantita>
        HashMap<Integer,Integer> map = new HashMap<>();
        for(Pizza pizza : elencoPizze){
            int idPizza = pizza.getIdPizza();
            Integer value = map.get(idPizza);
            if(value == null){
                map.put(idPizza, 1);
            }else{
                map.put(idPizza, value+1);
            }
        }
        return map;
    }
    
    public static Pizza getPizzaByIdPizza(ArrayList<Pizza> elencoPizze, int idPizza){
        for(Pizza pizza : elencoPizze){
            if(pizza.getIdPizza() == idPizza){
                return pizza;
            }
        }
        return null;
    }
    
    
}
