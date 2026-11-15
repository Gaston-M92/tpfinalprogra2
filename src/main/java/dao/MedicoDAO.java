package dao;

import Enums.Especialidad;
import Interfaces.DAO;
import modelo.Medico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla medicos.
 * Traduce objetos Medico a filas de la base y viceversa.
 * Los horarios de atencion no se persisten: se manejan en memoria.
 */

public class MedicoDAO implements DAO<Medico> {
    /**
     * Guarda un medico nuevo en la base.
     *
     * @param medico medico a guardar
     * @throws SQLException si el DNI o la matricula ya existen, o falla la conexion
     */

    @Override
    public void insertar(Medico medico) throws SQLException {
        String sql = "INSERT INTO medicos (dni, nombre, apellido, telefono, matricula, especialidad, duracion_turno_min)" + "VALUES (?, ?, ?, ?, ? ,?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, medico.getDni());
            ps.setString(2, medico.getNombre());
            ps.setString(3, medico.getApellido());
            ps.setString(4, medico.getTelefono());
            ps.setString(5, medico.getMatricula());
            ps.setString(6, medico.getEspecialidad().name());
            ps.setInt(7, medico.getDuracionTurnoMin());
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza los datos de un medico existente, identificado por su DNI.
     *
     * @param medico medico con los datos actualizados
     * @throws SQLException si falla la conexion o la consulta
     */

    @Override
    public void actualizar(Medico medico) throws SQLException {
        String sql = "UPDATE medicos SET nombre = ?, apellido = ?, telefono  = ?, matricula = ?, "
                + "especialidad = ?, duracion_turno_min = ? WHERE dni = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getApellido());
            ps.setString(3, medico.getTelefono());
            ps.setString(4, medico.getMatricula());
            ps.setString(5, medico.getEspecialidad().name());
            ps.setInt(6, medico.getDuracionTurnoMin());
            ps.setString(7, medico.getDni());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un medico por su DNI.
     *
     * @param dni DNI del medico a eliminar
     * @throws SQLException si el medico tiene turnos asociados (clave foranea) o falla la conexion
     */

    @Override
    public void eliminar(String dni) throws SQLException {
        String sql = "DELETE FROM medicos WHERE dni = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un medico por su DNI.
     *
     * @param dni DNI del medico
     * @return el medico encontrado, o null si no existe
     * @throws SQLException si falla la conexion o la consulta
     */
    @Override
    public Medico buscarPorId(String dni) throws SQLException {
        String sql = "SELECT * FROM medicos WHERE dni = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearMedico(rs);
                }
                return null;
            }
        }
    }

    /**
     * Devuelve todos los medicos, ordenados por apellido y nombre.
     *
     * @return lista de medicos (vacia si no hay ninguno)
     * @throws SQLException si falla la conexion o la consulta
     */
    @Override
    public List<Medico> listarTodos() throws SQLException{
        String sql = "SELECT * FROM medicos ORDER BY apellido, nombre";
        List<Medico> medicos = new ArrayList<>();
        try(Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                medicos.add(crearMedico(rs));
            }
        }
        return medicos;
    }

    /**
     * Arma un objeto Medico a partir de la fila actual del ResultSet.
     */
    private Medico crearMedico(ResultSet rs) throws SQLException{
        return new Medico(
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni"),
                rs.getString("telefono"),
                rs.getString("matricula"),
                Especialidad.valueOf(rs.getString("especialidad")),
                rs.getInt("duracion_turno_min"),
                new ArrayList<>()
        );
    }
}