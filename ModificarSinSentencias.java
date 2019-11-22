import java.sql.*;

public class ModificarSinSentencias {
	
	static String BDPer = "jdbc:mysql://localhost/casopractico";
	
	public static void cambiarDep(int numDep, String nombre) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection db = DriverManager.getConnection(BDPer, "root", "root");
			
			int i = 0;
			
			Statement stmt = db.createStatement();
            i = stmt.executeUpdate("UPDATE DEPT SET DNAME = '" + nombre + "' WHERE DEPTNO = " + numDep + ";");
            
            System.out.println("Filas afectadas: " + i);
    		db.close();
    		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
         
	}
	
	public static void main(String[] args) {
		cambiarDep(1, "COMISIONES");
	}

}