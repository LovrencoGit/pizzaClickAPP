/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import model.Pizza;
import model.PizzaPrenotata;

/**
 *
 * @author Lovrenco
 */
public class ArrayListPizzaPrenotataDisplayer {
    
    
    public static HashMap<String, Integer> getElencoPizzaPrenotataToHashMap(ArrayList<PizzaPrenotata> elencoPizze){ // <idPizza , quantita>
        HashMap<String,Integer> map = new HashMap<>();
        for(PizzaPrenotata pizza : elencoPizze){
            String nome = pizza.getNomePizzaPrenotata();
            Integer value = map.get(nome);
            if(value == null){
                map.put(nome, 1);
            }else{
                map.put(nome, value+1);
            }
        }
        return map;
    }
    
    public static PizzaPrenotata getPizzaByNomePizza(ArrayList<PizzaPrenotata> elencoPizze, String nome){
        for(PizzaPrenotata pizza : elencoPizze){
            if(pizza.getNomePizzaPrenotata().equals(nome)){
                return pizza;
            }
        }
        return null;
    }
    
    
}
