package dao;

import Interfaces.DAO;
import modelo.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 * Acceso a datos de la tabla pacientes.
 * Traduce objetos paciente a filas de la base y viceversa.
 */

public class PacienteDAO implements DAO<Paciente> {
    /**
     * Guarda un paciente nuevo en la base.
     */

    @Override
    public void insertar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO pacientes (dni, nombre, apellido, telefono, historia_clinica, obra_social, fecha_nacimiento) " + "VALUES (?, ?, ?, ?, ? ,?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, paciente.getDni());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellido());
            ps.setString(4, paciente.getTelefono());
            ps.setInt(5, paciente.getHistoriaClinica());
            ps.setString(6, paciente.getObraSocial());
            ps.setDate(7, Date.valueOf(paciente.getFechaNacimiento()));
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza los datos de un paciente existente, identificado por su DNI.
     */

    @Override
    public void actualizar(Paciente paciente) throws SQLException {
        String sql = "UPDATE pacientes SET nombre = ?, apellido = ?, telefono  = ?, historia_clinica = ?, "
                + "obra_social = ?, fecha_nacimiento = ? WHERE dni = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getTelefono());
            ps.setInt(4, paciente.getHistoriaClinica());
            ps.setString(5, paciente.getObraSocial());
            ps.setDate(6, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setString(7, paciente.getDni());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un paciente por su DNI.
     */

    @Override
    public void eliminar(String dni) throws SQLException {
        String sql = "DELETE FROM pacientes WHERE dni = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un paciente por su DNI.
     */
    @Override
    public Paciente buscarPorId(String dni) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE dni = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearPaciente(rs);
                }
                return null;
            }
        }
    }

    /**
     * Devuelve todos los pacientes, ordenados por apellido y nombre.
     */
    @Override
    public List<Paciente> listarTodos() throws SQLException{
        String sql = "SELECT * FROM pacientes ORDER BY apellido, nombre";
        List<Paciente> pacientes = new ArrayList<>();
        try(Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                pacientes.add(crearPaciente(rs));
            }
        }
        return pacientes;
    }

    /**
     * Arma un objeto paciente a partir de la fila actual del ResultSet.
     */
    private Paciente crearPaciente(ResultSet rs) throws SQLException{
        return new Paciente(
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("telefono"),
                rs.getInt("historia_clinica"),
                rs.getString("obra_social"),
                rs.getDate("fecha_nacimiento").toLocalDate()
        );
    }
}