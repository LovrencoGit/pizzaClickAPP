package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Lovrenco on 23/08/2016.
 */

public class Utente implements Parcelable {
    private int idUtente;
    private String username;
    private String password;
    private String indirizzo;
    private String ruolo;
    private String attivo;

    public Utente() {
        idUtente = Integer.MAX_VALUE;
        username = "";
        password = "";
        ruolo = "";
        attivo = "T";
        indirizzo = "";
    }

    public Utente(int idUtente, String username, String password, String indirizzo, String ruolo, String attivo) {
        this.idUtente = idUtente;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
        this.attivo = attivo;
        this.indirizzo = indirizzo;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String isAttivo() {
        return attivo;
    }

    public void setAttivo(String attivo) {
        this.attivo = attivo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idUtente);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.indirizzo);
        dest.writeString(this.ruolo);
        dest.writeString(this.attivo);
    }

    protected Utente(Parcel in) {
        this.idUtente = in.readInt();
        this.username = in.readString();
        this.password = in.readString();
        this.indirizzo = in.readString();
        this.ruolo = in.readString();
        this.attivo = in.readString();
    }

    public static final Parcelable.Creator<Utente> CREATOR = new Parcelable.Creator<Utente>() {
        @Override
        public Utente createFromParcel(Parcel source) {
            return new Utente(source);
        }

        @Override
        public Utente[] newArray(int size) {
            return new Utente[size];
        }
    };
}
