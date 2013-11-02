/**
 * 
 */
package com.boondocksaints.candlekeep.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Kaleb
 *
 */
public class Autor implements Parcelable {
	
// Propiedades y Atributos ****************************************************
	private Integer idAutor;
	private String nombre;
	
	public Integer getIdAutor() {
		return idAutor;
	}
	public void setIdAutor(Integer idAutor) {
		this.idAutor = idAutor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
// Constructores **************************************************************
	public Autor() {
		super();
		this.idAutor = -1;
		this.nombre = "";
	}

	public Autor(Integer idAutor, String nombre) {
		super();
		this.idAutor = idAutor;
		this.nombre = nombre;
	}
	
	public Autor(Parcel in)
	{
		this.idAutor = in.readInt();
		this.nombre = in.readString();
	}

// Implementacion de Parcelable ***********************************************
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.idAutor);
		dest.writeString(this.nombre);
	}
	
	public static final Parcelable.Creator<Autor> CREATOR = new Parcelable.Creator<Autor>() {
        public Autor createFromParcel(Parcel in) {
            return new Autor(in);
        }

        public Autor[] newArray(int size) {
            return new Autor[size];
        }
    };

	
}
