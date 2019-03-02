package org.iesalandalus.programacion.reservasaulas.modelo;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;

/**
 *
 * Interfaz de la clase ModeloReservasAulas
 * 
 * @see ModeloReservasAulas
 * @author Juan Antonio Manzano Plaza
 * @version 2
 *
 */
public interface IModeloReservasAulas {

	List<Aula> getAulas();

	int getNumAulas();

	List<String> representarAulas();

	Aula buscarAula(Aula buscar);

	void insertarAula(Aula insertar) throws OperationNotSupportedException, IllegalArgumentException;

	void borrarAula(Aula borrar) throws OperationNotSupportedException, IllegalArgumentException;

	List<Profesor> getProfesores();

	int getNumProfesores();

	List<String> representarProfesores();

	Profesor buscarProfesor(Profesor buscar);

	void insertarProfesor(Profesor insertar) throws OperationNotSupportedException, IllegalArgumentException;

	void borrarProfesor(Profesor borrar) throws OperationNotSupportedException, IllegalArgumentException;

	List<Reserva> getReservas();

	int getNumReservas();

	List<String> representarReservas();

	Reserva buscarReserva(Reserva buscar);

	void realizarReserva(Reserva realizar) throws OperationNotSupportedException, IllegalArgumentException;

	void anularReserva(Reserva anular) throws OperationNotSupportedException, IllegalArgumentException;

	List<Reserva> getReservasAula(Aula aula) throws IllegalArgumentException;

	List<Reserva> getReservasProfesor(Profesor profesor) throws IllegalArgumentException;

	List<Reserva> getReservasPermanencia(Permanencia permanencia) throws IllegalArgumentException;

	boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) throws IllegalArgumentException;

}