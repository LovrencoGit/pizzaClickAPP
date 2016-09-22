package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author Lovrenco
 */
public class Pizza implements Parcelable {

    private int idPizza;
    private String nomePizza;
    private String ingredienti;
    private double prezzoPizza;
    private boolean disponibile;
    private int quantita;


    public Pizza() {
        idPizza = Integer.MAX_VALUE;
        nomePizza = "";
        ingredienti = "";
        prezzoPizza = 0.0;
        disponibile = false;
    }

    public Pizza(int idPizza, String nomePizza, String ingredienti, double prezzoPizza, boolean disponibile) {
        this.idPizza = idPizza;
        this.nomePizza = nomePizza;
        this.ingredienti = ingredienti;
        this.prezzoPizza = prezzoPizza;
        this.disponibile = disponibile;
    }

    public int getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }

    public String getNomePizza() {
        return nomePizza;
    }

    public void setNomePizza(String nomePizza) {
        this.nomePizza = nomePizza;
    }

    public String getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(String ingredienti) {
        this.ingredienti = ingredienti;
    }

    public double getPrezzoPizza() {
        return prezzoPizza;
    }

    public void setPrezzoPizza(double prezzoPizza) {
        this.prezzoPizza = prezzoPizza;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pizza pizza = (Pizza) o;

        if (idPizza != pizza.idPizza) return false;
        else return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idPizza;
        result = 31 * result + (nomePizza != null ? nomePizza.hashCode() : 0);
        result = 31 * result + (ingredienti != null ? ingredienti.hashCode() : 0);
        temp = Double.doubleToLongBits(prezzoPizza);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (disponibile ? 1 : 0);
        //result = 31 * result + quantita;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idPizza);
        dest.writeString(this.nomePizza);
        dest.writeString(this.ingredienti);
        dest.writeDouble(this.prezzoPizza);
        dest.writeByte(this.disponibile ? (byte) 1 : (byte) 0);
    }

    protected Pizza(Parcel in) {
        this.idPizza = in.readInt();
        this.nomePizza = in.readString();
        this.ingredienti = in.readString();
        this.prezzoPizza = in.readDouble();
        this.disponibile = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Pizza> CREATOR = new Parcelable.Creator<Pizza>() {
        @Override
        public Pizza createFromParcel(Parcel source) {
            return new Pizza(source);
        }

        @Override
        public Pizza[] newArray(int size) {
            return new Pizza[size];
        }
    };
}
