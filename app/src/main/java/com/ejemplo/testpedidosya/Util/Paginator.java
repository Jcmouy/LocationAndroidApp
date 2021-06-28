package com.ejemplo.testpedidosya.Util;

import com.ejemplo.testpedidosya.MainActivity;
import com.ejemplo.testpedidosya.Pojo.Restaurant;

import java.util.ArrayList;

public class Paginator {
    /*
   Para este trabajo se tomo la desicion de trabajar solamente con los 100 primeros
   resultados tal como vienen por defecto del api, esta implementacion solo realiza
   una llamada por búsqueda al getRestaurants y luego trabaja en memoria pero tiene
   la limitante de que si se encuentran mas resultados en una busqueda estos nunca
   se verian. Otras opciones que dependen del uso habitual de los usuarios serían
   realizar una invocación al api por cambio de página (cargar de a 20), o
   permitir que se seleccionen el maximo de resultados..
    */
    public static final int ITEMS_PER_PAGE = 20;

    public int getTotalPages() {

        int total_num_items = MainActivity.list_restaurants.size();

        int remainingItems=total_num_items % ITEMS_PER_PAGE;
        if(remainingItems>0)
        {
            return total_num_items / ITEMS_PER_PAGE;
        }
        return (total_num_items / ITEMS_PER_PAGE)-1;

    }

    public ArrayList<Restaurant> generatePage(int currentPage)
    {
        int startItem = currentPage * ITEMS_PER_PAGE;
        int lastItem = startItem + ITEMS_PER_PAGE;

        ArrayList<Restaurant> currentRestaurants = new ArrayList<>();

        try {
            for (int i = 0; i < MainActivity.list_restaurants.size(); i++) {

                //Add current page's data
                if (i >= startItem && i < lastItem) {
                    currentRestaurants.add(MainActivity.list_restaurants.get(i));
                }
            }
            return currentRestaurants;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
