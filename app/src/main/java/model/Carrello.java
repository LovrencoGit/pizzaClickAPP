package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Lovrenco on 26/08/2016.
 */
public class Carrello implements Parcelable {
    private ArrayList<Pizza> elencoPizze;
    private double prezzoTotale;

    public Carrello() {
        elencoPizze = new ArrayList<>();
        prezzoTotale = 0.0;
    }

    public Carrello(ArrayList<Pizza> carrello, double prezzoTotale) {
        this.elencoPizze = carrello;
        this.prezzoTotale = prezzoTotale;

    }

    public ArrayList<Pizza> getElencoPizze() {
        return elencoPizze;
    }

    public void setElencoPizze(ArrayList<Pizza> carrello) {
        this.elencoPizze = carrello;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    /**
     * ******************************************************************
     */
    public boolean addCarrello(Pizza p, int quantity) {
        if (p == null) {
            return false;
        }
        for (int i=0 ; i<quantity ; i++) {
            prezzoTotale += p.getPrezzoPizza();
            elencoPizze.add(p);
        }
        return true;
    }

    public boolean removeCarrello(Pizza p) {
        for (Pizza i : elencoPizze) {
            if (i.getIdPizza() == p.getIdPizza()) {
                prezzoTotale -= p.getPrezzoPizza();
                return elencoPizze.remove(i);
            }
        }
        return false;
    }

    public void svuota(){
        elencoPizze.clear();
        prezzoTotale = 0.0;
    }

    public int getQuantityOfPizzaById(int idPizza){
        int qty = 0;
        for(Pizza item : elencoPizze){
            qty += ( item.getIdPizza() == idPizza ? 1 : 0 );
        }
        return qty;
    }

    /**
     * ******************************************************************
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.elencoPizze);
        dest.writeDouble(this.prezzoTotale);
    }

    protected Carrello(Parcel in) {
        this.elencoPizze = in.createTypedArrayList(Pizza.CREATOR);
        this.prezzoTotale = in.readDouble();
    }

    public static final Parcelable.Creator<Carrello> CREATOR = new Parcelable.Creator<Carrello>() {
        @Override
        public Carrello createFromParcel(Parcel source) {
            return new Carrello(source);
        }

        @Override
        public Carrello[] newArray(int size) {
            return new Carrello[size];
        }
    };
}
