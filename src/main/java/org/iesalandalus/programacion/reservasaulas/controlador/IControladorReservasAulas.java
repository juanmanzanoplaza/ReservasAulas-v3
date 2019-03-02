package org.iesalandalus.programacion.reservasaulas.controlador;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Aula;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Profesor;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.Reserva;
import org.iesalandalus.programacion.reservasaulas.modelo.dominio.permanencia.Permanencia;

/**
 *
 * Interfaz de la clase ControladorReservasAulas
 * 
 * @see ControladorReservasAulas
 * @author Juan Antonio Manzano Plaza
 * @version 2
 *
 */
public interface IControladorReservasAulas {

	void comenzar();

	void salir();

	void insertarAula(Aula insertar) throws OperationNotSupportedException, IllegalArgumentException;

	void borrarAula(Aula borrar) throws OperationNotSupportedException, IllegalArgumentException;

	Aula buscarAula(Aula buscar);

	List<String> representarAulas();

	void insertarProfesor(Profesor insertar) throws OperationNotSupportedException, IllegalArgumentException;

	void borrarProfesor(Profesor borrar) throws OperationNotSupportedException, IllegalArgumentException;

	Profesor buscarProfesor(Profesor buscar);

	List<String> representarProfesores();

	void realizarReserva(Reserva realizada) throws OperationNotSupportedException, IllegalArgumentException;

	void anularReserva(Reserva anulada) throws OperationNotSupportedException, IllegalArgumentException;

	List<String> representarReservas();

	List<Reserva> getReservasAula(Aula aula) throws IllegalArgumentException;

	List<Reserva> getReservasProfesor(Profesor profesor) throws IllegalArgumentException;

	List<Reserva> getReservasPermanencia(Permanencia permanencia) throws IllegalArgumentException;

	boolean consultarDisponibilidad(Aula aula, Permanencia permanencia) throws IllegalArgumentException;

}