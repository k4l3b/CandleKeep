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
public class Categoria implements Parcelable {

// Propiedades y Atributos ****************************************************
	private String idCategoria;
	private String descripcion;
	
	public String getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

// Constructores **************************************************************

	public Categoria(String idCategoria, String descripcion) {
		super();
		this.idCategoria = idCategoria;
		this.descripcion = descripcion;
	}
	
	public Categoria() {
		super();
		this.idCategoria = "";
		this.descripcion = "";
	}
	
	public Categoria(Parcel in)
	{
		this.idCategoria = in.readString();
		this.descripcion = in.readString();
	}

// Implementacion de Parcelable ***********************************************

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.idCategoria);
		dest.writeString(this.descripcion);
	}
	
	public static final Parcelable.Creator<Categoria> CREATOR = new Parcelable.Creator<Categoria>() {
        public Categoria createFromParcel(Parcel in) {
            return new Categoria(in);
        }

        public Categoria[] newArray(int size) {
            return new Categoria[size];
        }
    };

}
