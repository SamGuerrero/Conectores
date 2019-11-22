import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Insertar {

	public static void main(String[] args) {
		//Creamos la conexión para MySql
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection ("jdbc:mysql://localhost/ejemplo", "root", "root");
			
			String idLibro = "4";
			String titulo = "La Colmena";
			String precio = "7";
			
			String sql = String.format("INSERT INTO Libros VALUES (%s, '%s', %s)", idLibro, titulo, precio);
			System.out.println(sql);
			
			String idAutor = "3";
			String nombre = "José Cela";
			String nacionalidad = "Español";
			
			String sql2 = String.format("INSERT INTO Autores VALUES (%s, '%s', '%s')", idAutor, nombre, nacionalidad);
			System.out.println(sql2);
			
			String sql3 = String.format("INSERT INTO relacionLibroAutor VALUES (%s, %s)", idLibro, idAutor);
			System.out.println(sql3);
			
			Statement sentencia = conexion.createStatement();
			int filas=0;
			try {
			  filas = sentencia.executeUpdate(sql.toString());
			  System.out.println("Filas afectadas: " + filas);
			  filas = sentencia.executeUpdate(sql2.toString());
			  System.out.println("Filas afectadas: " + filas);
			  filas = sentencia.executeUpdate(sql3.toString());
			  System.out.println("Filas afectadas: " + filas);
			} catch (SQLException e) {
				//e.printStackTrace();
				   System.out.printf("HA OCURRIDO UNA EXCEPCIÓN:%n"); 
				   System.out.printf("Mensaje   : %s %n", e.getMessage()); 
				   System.out.printf("SQL estado: %s %n", e.getSQLState()); 
				   System.out.printf("Cód error : %s %n", e.getErrorCode());	    	
			}
			
			

			sentencia.close(); // Cerrar Statement
			conexion.close(); // Cerrar conexión
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

}
