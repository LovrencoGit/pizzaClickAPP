package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lovrenco on 26/08/2016.
 */
public class Ordine implements Parcelable {
    private int idOrdine;
    private int idUtente;
    private int quantitaTotale;
    private double prezzoTotale;
    private String indirizzo;
    private String data;
    private boolean annullato;
    private boolean consegnato;
    private int valutazione;

    public Ordine() {
        idOrdine = Integer.MAX_VALUE;
        idUtente = Integer.MAX_VALUE;
        quantitaTotale = 0;
        prezzoTotale = 0.0;
        indirizzo = "";
        data = "";
        annullato = false;
        consegnato = false;
        valutazione = 0;
    }

    public Ordine(int idOrdine, int idUtente, int quantitaTotale, double prezzoTotale, String indirizzo, String data, boolean annullato, boolean consegnato, int valutazione) {
        this.idOrdine = idOrdine;
        this.idUtente = idUtente;
        this.quantitaTotale = quantitaTotale;
        this.prezzoTotale = prezzoTotale;
        this.indirizzo = indirizzo;
        this.data = data;
        this.annullato = annullato;
        this.consegnato = consegnato;
        this.valutazione = valutazione;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getQuantitaTotale() {
        return quantitaTotale;
    }

    public void setQuantitaTotale(int quantitaTotale) {
        this.quantitaTotale = quantitaTotale;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isAnnullato() {
        return annullato;
    }

    public void setAnnullato(boolean annullato) {
        this.annullato = annullato;
    }

    public boolean isConsegnato() {
        return consegnato;
    }

    public void setConsegnato(boolean consegnato) {
        this.consegnato = consegnato;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idOrdine);
        dest.writeInt(this.idUtente);
        dest.writeInt(this.quantitaTotale);
        dest.writeDouble(this.prezzoTotale);
        dest.writeString(this.indirizzo);
        dest.writeString(this.data);
        dest.writeByte(this.annullato ? (byte) 1 : (byte) 0);
        dest.writeByte(this.consegnato ? (byte) 1 : (byte) 0);
        dest.writeInt(this.valutazione);
    }

    protected Ordine(Parcel in) {
        this.idOrdine = in.readInt();
        this.idUtente = in.readInt();
        this.quantitaTotale = in.readInt();
        this.prezzoTotale = in.readDouble();
        this.indirizzo = in.readString();
        this.data = in.readString();
        this.annullato = in.readByte() != 0;
        this.consegnato = in.readByte() != 0;
        this.valutazione = in.readInt();
    }

    public static final Parcelable.Creator<Ordine> CREATOR = new Parcelable.Creator<Ordine>() {
        @Override
        public Ordine createFromParcel(Parcel source) {
            return new Ordine(source);
        }

        @Override
        public Ordine[] newArray(int size) {
            return new Ordine[size];
        }
    };
}
