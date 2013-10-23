/**
 * 
 */
package com.boondocksaints.candlekeep.classes;

/**
 * @author Kaleb
 *
 */
public class Categoria {

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

}
