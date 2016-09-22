package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lovrenco on 26/08/2016.
 */
public class PizzaPrenotata implements Parcelable {
    private int idPizzaPrenotata;
    private int idOrdine;
    private String nomePizzaPrenotata;
    private double prezzoPizzaPrenotata;
    private int quantita;

    public PizzaPrenotata() {
        idPizzaPrenotata= Integer.MAX_VALUE;
        idOrdine= Integer.MAX_VALUE;
        nomePizzaPrenotata="";
        prezzoPizzaPrenotata=0.0;
    }

    public PizzaPrenotata(int idPizzaPrenotata, int idOrdine, String nomePizzaPrenotata, double prezzoPizzaPrenotata) {
        this.idPizzaPrenotata = idPizzaPrenotata;
        this.idOrdine = idOrdine;
        this.nomePizzaPrenotata = nomePizzaPrenotata;
        this.prezzoPizzaPrenotata = prezzoPizzaPrenotata;
    }

    public int getIdPizzaPrenotata() {
        return idPizzaPrenotata;
    }

    public void setIdPizzaPrenotata(int idPizzaPrenotata) {
        this.idPizzaPrenotata = idPizzaPrenotata;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public String getNomePizzaPrenotata() {
        return nomePizzaPrenotata;
    }

    public void setNomePizzaPrenotata(String nomePizzaPrenotata) {
        this.nomePizzaPrenotata = nomePizzaPrenotata;
    }

    public double getPrezzoPizzaPrenotata() {
        return prezzoPizzaPrenotata;
    }

    public void setPrezzoPizzaPrenotata(double prezzoPizzaPrenotata) {
        this.prezzoPizzaPrenotata = prezzoPizzaPrenotata;
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

        PizzaPrenotata that = (PizzaPrenotata) o;

        if (idOrdine != that.idOrdine) return false;
        if (Double.compare(that.prezzoPizzaPrenotata, prezzoPizzaPrenotata) != 0) return false;
        return nomePizzaPrenotata != null ? nomePizzaPrenotata.equals(that.nomePizzaPrenotata) : that.nomePizzaPrenotata == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idOrdine;
        result = 31 * result + (nomePizzaPrenotata != null ? nomePizzaPrenotata.hashCode() : 0);
        temp = Double.doubleToLongBits(prezzoPizzaPrenotata);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idPizzaPrenotata);
        dest.writeInt(this.idOrdine);
        dest.writeString(this.nomePizzaPrenotata);
        dest.writeDouble(this.prezzoPizzaPrenotata);
    }

    protected PizzaPrenotata(Parcel in) {
        this.idPizzaPrenotata = in.readInt();
        this.idOrdine = in.readInt();
        this.nomePizzaPrenotata = in.readString();
        this.prezzoPizzaPrenotata = in.readDouble();
    }

    public static final Parcelable.Creator<PizzaPrenotata> CREATOR = new Parcelable.Creator<PizzaPrenotata>() {
        @Override
        public PizzaPrenotata createFromParcel(Parcel source) {
            return new PizzaPrenotata(source);
        }

        @Override
        public PizzaPrenotata[] newArray(int size) {
            return new PizzaPrenotata[size];
        }
    };
}
