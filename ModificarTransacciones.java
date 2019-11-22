import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModificarTransacciones {
	static String BDPer = "jdbc:mysql://localhost/casopractico";
	
	public static void cambiarDep(int numDep, String nombre) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection db = DriverManager.getConnection(BDPer, "root", "root");
			
			int i = 0;
			
			db.setAutoCommit(false); //Desactivo el autoCommit
			PreparedStatement stmt = db.prepareStatement("UPDATE DEPT SET DNAME = (?) WHERE DEPTNO = (?);");
			stmt.setString(1,  nombre);
			stmt.setInt(2,  numDep);
            i = stmt.executeUpdate();
            
            db.commit(); //Hago commit manualmente
            db.setAutoCommit(true); //Lo activo para el resto de ejercicios
            
            System.out.println("Filas afectadas: " + i);
    		db.close();
    		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
         
	}
	
	public static void main(String[] args) {
		cambiarDep(1, "COMPRAS");
	}

}
