package dao;

import Enums.EstadoTurno;
import Interfaces.DAO;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla turnos.
 * En la base, un turno guarda solo los DNI del paciente y del medico;
 * al leerlo, se reconstruyen los objetos completos usando PacienteDAO y MedicoDAO.
 */

public class TurnoDAO implements DAO<Turno>{
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();

    /**
     * Guarda un turno nuevo en la base.
     *
     * @param turno turno a guardar
     * @throws SQLException si el id ya existe, el paciente o el medico no existen, o falla la conexion
     */

    @Override
    public void insertar(Turno turno) throws SQLException{
        String sql = "INSERT INTO turnos (id_turno, dni_paciente, dni_medico, fecha_hora, motivo_consulta, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, turno.getId());
            ps.setString(2, turno.getPaciente().getDni());
            ps.setString(3, turno.getMedico().getDni());
            ps.setTimestamp(4, Timestamp.valueOf(turno.getFechaHora()));
            ps.setString(5, turno.getMotivoConsulta());
            ps.setString(6, turno.getEstado().name());
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza un turno existente, identificado por su id.
     * Se usa, por ejemplo, para cancelarlo o marcarlo como atendido.
     *
     * @param turno turno con los datos actualizados
     * @throws SQLException si falla la conexion o la consulta
     */
    @Override
    public void actualizar (Turno turno) throws SQLException{
        String sql = "UPDATE turnos SET dni_paciente = ?, dni_medico = ?, fecha_hora = ?, "
                + "motivo_consulta = ?, estado = ? WHERE id_turno = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, turno.getPaciente().getDni());
            ps.setString(2, turno.getMedico().getDni());
            ps.setTimestamp(3, Timestamp.valueOf(turno.getFechaHora()));
            ps.setString(4, turno.getMotivoConsulta());
            ps.setString(5, turno.getEstado().name());
            ps.setString(6, turno.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un turno por su id
     *
     * @param id id del turno a eliminar
     * @throws SQLException si falla la conexion o la consulta
     */
    @Override
    public void eliminar(String id) throws SQLException{
        String sql = "DELETE FROM turnos WHERE id_turno = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un turno por su id.
     *
     * @param id id del turno
     * @return el turno encontrado, o null si no existe
     * @throws SQLException si falla la conexion o la consulta
     */
    @Override
    public Turno buscarPorId(String id) throws SQLException{
      String sql = "SELECT * FROM turnos WHERE id_turno = ?";
      try (Connection con = ConexionBD.getConexion();
           PreparedStatement ps = con.prepareStatement(sql)){
          ps.setString(1, id);
          try (ResultSet rs = ps.executeQuery()){
              if(rs.next()){
                  return crearTurno(rs);
              }
              return null;
          }
      }
    }

    /**
     * Devuelve todos los turnos, ordenados por fecha y hora.
     *
     * @return lista de turnos (vacia si no hay ninguno)
     * @throws SQLException si falla la conexion o la consulta
     */
    @Override
    public List<Turno> listarTodos() throws SQLException{
        String sql = "SELECT * FROM turnos ORDER BY fecha_hora";
        List<Turno> turnos = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){
    while(rs.next()){
        turnos.add(crearTurno(rs));
    }
        }
        return turnos;
    }

    /**
     * Arma un objeto Turno a partir de la fila actual del ResultSet.
     * Busca el paciente y el medico completos a partir de sus DNI.
     */

    private Turno crearTurno(ResultSet rs) throws SQLException{
        Paciente paciente = pacienteDAO.buscarPorId(rs.getString("dni_paciente"));
        Medico medico = medicoDAO.buscarPorId(rs.getString("dni_medico"));
        return new Turno(
                rs.getString("id_turno"),
                paciente,
                medico,
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                rs.getString("motivo_consulta"),
                EstadoTurno.valueOf(rs.getString("estado"))
        );
    }

}