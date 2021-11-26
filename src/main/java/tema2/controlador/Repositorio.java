package tema2.controlador;

import tema2.modelo.Empleado;
import tema2.modelo.EmpleadoDNIException;

import java.sql.*;
import java.util.Optional;

//Implementación del repositorio, nos ofrece un Singleton que mantiene la conexión con la base de datos y nos permite operar directamente usando objetos de tipo Empleado.
//Nos permite hacer un CRUD completo sobre Empleado
public final class Repositorio {

    private static String usuario = "usuario";
    private static String pass = "pass";
    private static Repositorio repositorio = null;
    private static String inicio = "CREATE TABLE IF NOT EXISTS empleados (dni varchar(45) NOT NULL, nombre varchar(45) NOT NULL, salario INT NOT NULL, PRIMARY KEY (dni))";
    private static PreparedStatement deleteStatement;
    private String createSQL = "INSERT INTO empleados (dni, nombre, salario) VALUES (?, ?, ?)";
    private String readSQL = "SELECT * FROM empleados WHERE dni = ?";
    private String updateSQL = "UPDATE empleados SET nombre=?, salario=? WHERE dni=?";
    private String deleteSQL = "DELETE FROM empleados WHERE dni=?";
    private Connection connection;

    public Repositorio(){ //Constructor por defecto
        String driver = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:postgresql://localhost/empresa";
        try {
            connection = DriverManager.getConnection(jdbcUrl, usuario, pass);
            connection.createStatement().execute(inicio);
            deleteStatement = connection.prepareStatement(deleteSQL);
        } catch (SQLException e) {
            System.err.println("Se ha producido un error conectando con la BBDD");
            System.exit(0);
        }
    }

    //Devuelve el singleton
    public static Repositorio empleadoService() {
        if(repositorio == null)
            repositorio = new Repositorio();
        return repositorio;
    }

    public static void setCredenciales(String user, String password) {
        usuario=user;
        pass=password;
    }

    //Close
    public void finalizaEmpleados(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Se ha producido un error en el cierre de conexión de la BBDD");
            e.printStackTrace();
        }
    }

    public void registra(Empleado empleado) throws SQLException {
        try(PreparedStatement createStatement = connection.prepareStatement(createSQL)){
            createStatement.setString(1,empleado.getDni());
            createStatement.setString(2,empleado.getNombre());
            createStatement.setInt(3,empleado.getSalario());
            createStatement.executeUpdate();
        }
    }

    public boolean compruebaDni(String dni) throws SQLException, EmpleadoDNIException {
        boolean res;
        return buscaEmpleadoPorDni(dni).isPresent();
    }

    public Optional<Empleado> buscaEmpleadoPorDni(String dni) throws SQLException, EmpleadoDNIException {
        Optional<Empleado> emp = Optional.empty();
        try(PreparedStatement readStatement = connection.prepareStatement(readSQL)){
            readStatement.setString(1,dni);
            ResultSet rs = readStatement.executeQuery();
            if(rs.next()){
                emp = Optional.of(new Empleado(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getInt("salario")));
                rs.close();
            }
        }
        return emp;
    }

    public void eliminaPorDni(String dni) throws SQLException {
        deleteStatement.setString(1,dni);
        deleteStatement.executeUpdate();
    }

    public void eliminaTodo() throws SQLException {
        connection.prepareStatement("DELETE FROM empleados").executeUpdate();
    }

    public void sobreescribe(Empleado empleado) throws SQLException {
        try(PreparedStatement updateStatement = connection.prepareStatement(updateSQL)){
            updateStatement.setString(3,empleado.getDni());
            updateStatement.setString(1,empleado.getNombre());
            updateStatement.setInt(2,empleado.getSalario());
            updateStatement.executeUpdate();
        }
    }
}
