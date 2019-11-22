import java.sql.*;

public class Consulta {
    static String BDPer = "jdbc:mysql://localhost/casopractico";
	
	public static void main(String[] args) {
		try{
            Class.forName("com.mysql.jdbc.Driver"); //Pasar la ruta del Drive
            Connection db = DriverManager.getConnection(BDPer, "root", "root");
            
            AccesoBDD prueba = new AccesoBDD();
            prueba.infoTabla(db.getSchema(), "bonus");
      }
      catch(ClassNotFoundException e){
            e.printStackTrace();
      }
      catch(SQLException e){
            e.printStackTrace();
      }
	}
}
