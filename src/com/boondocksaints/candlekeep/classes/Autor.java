/**
 * 
 */
package com.boondocksaints.candlekeep.classes;

/**
 * @author Kaleb
 *
 */
public class Autor {
	
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
	
}
