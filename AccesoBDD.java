import java.sql.*;
import java.util.ArrayList;

public class AccesoBDD {
    static String BDPer = "jdbc:mysql://localhost/casopractico";
    Connection db;
    
    //Carga el driver y establece la conexión
    public void conectarBDD(){
         try{
               Class.forName("com.mysql.jdbc.Driver"); //Pasar la ruta del Drive
               db = DriverManager.getConnection(BDPer, "root", "root");
         }
         catch(ClassNotFoundException e){
               e.printStackTrace();
         }
         catch(SQLException e){
               e.printStackTrace();
         }
    }
    
    //Inserta un departamento tras recibir tres argumentos
    public void insertarDep(int numDep, String nombre, String localidad) {
    	try{
    		conectarBDD();
            db.setAutoCommit(false);
            
            PreparedStatement ps = db.prepareStatement("INSERT INTO DEPT VALUES((?),(?),(?));");
            ps.setInt(1, numDep);
            ps.setString(2, nombre);
            ps.setString(3,localidad);
            ps.executeUpdate();
            
            db.commit();
            db.setAutoCommit(true);
            
            ps.close();
            db.close();
           
    	}catch(SQLException e){
    		try{
    			db.rollback(); // Si algo falla hago rollback para dejarlo como antes
    			
    		}catch(SQLException ex){
    			ex.printStackTrace();
            }
    		
    	}
    	
    }
    
  //Inserta un objeto departamento tras recibir tres argumentos
    public void insertarDep(Departamento dep) {
        try{
        	conectarBDD();
        	Departamento dept = dep;
        	db.setAutoCommit(false);
        	
            PreparedStatement ps = db.prepareStatement("INSERT INTO DEPT VALUES((?), (?), (?));");
            ps.setInt(1, dept.getDept_no());
            ps.setString(2, dept.getDnombre());
            ps.setString(3, dept.getLoc());
            ps.execute();
            
            db.commit();
            db.setAutoCommit(true);
            
            ps.close();
            db.close();
            
        }catch(SQLException e){
              try{
                   db.rollback();
              }
              catch(SQLException ex){
                   ex.printStackTrace();
              }
        }
        
    }
    
    //Devuelve un lista de Departamentos, con todos los departamentos de la base de datos
    public ArrayList<Departamento> departamentos() {
    	conectarBDD();
        ArrayList<Departamento> listaDep = new ArrayList<Departamento>();
       
        try{
        	Statement stmt = db.createStatement();
        	ResultSet rs = stmt.executeQuery("SELECT * FROM DEPT");
        	
        	Departamento dep;
        	while(rs.next()){
        		dep = new Departamento();
        		
        		dep.setDept_no(rs.getInt("DEPTNO"));
                dep.setDnombre(rs.getString("DNAME"));
                dep.setLoc(rs.getString("LOC"));
                   
                listaDep.add(dep);
        	}
        	
        	rs.close();
        	stmt.close();
        	db.close();
        	
        }catch(SQLException e){
              e.printStackTrace();
        }
       
        return listaDep;
    }

    //Devolver Departamento según el número
    public Departamento deptConcreto(int numDep) {
    	conectarBDD();
    	Departamento dep = new Departamento();
    	
        try{
        	Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM DEPT WHERE DEPTNO = " + numDep + ";");
            
            while(rs.next()){
            	dep.setDept_no(rs.getInt("DEPTNO"));
                dep.setDnombre(rs.getString("DNAME"));
                dep.setLoc(rs.getString("LOC"));    
            }
            
            rs.close();
            stmt.close();
            db.close();
            
        }catch(SQLException e){
              e.printStackTrace();
        }
        
        return dep;
    }
    
    //Actualiza departamento por Objeto Departamento aunque lo que tiene realmente en cuenta es el número de Departamento
    public void insertaDep(Departamento dept) {
        try{
        	conectarBDD();
        	Departamento dep = dept;
            db.setAutoCommit(false);
            
            PreparedStatement ps = db.prepareStatement("UPDATE DEPT SET DNAME = ?, LOC = ? WHERE DEPTNO = ?");
            ps.setString(1, dep.getDnombre());
            ps.setString(2, dep.getLoc());
            ps.setInt(3, dep.getDept_no());
            ps.executeUpdate();
            
            db.commit();
            db.setAutoCommit(true);
            
            ps.close();
            db.close();
        }
        catch(SQLException e){
              try{
                   db.rollback();
              }
              catch(SQLException ex){
                   ex.printStackTrace();
              }
        }
        
    }
    
    //Borra Departamento por número
    public void borraDep(int numDep) {
    	try{
    		conectarBDD();
    		db.setAutoCommit(false);
    		
    		PreparedStatement ps = db.prepareStatement("DELETE FROM DEPT WHERE DEPTNO = (?)");
            ps.setInt(1, numDep);
            ps.executeUpdate();
            
            db.commit();
            db.setAutoCommit(true);
            
            ps.close();
            db.close();
            
    	}catch(SQLException e){
            try{
                 db.rollback();
                 
            }catch(SQLException ex){
                 ex.printStackTrace();
            }
            
    	}
    	
    }
    
    //Borra departamento por número y devuelve las filas afectadas
    public int borraDepConFilas(int numDep) {
    	int i = 0;
    	
    	try{
    		conectarBDD();
    		db.setAutoCommit(false);
    		
    		PreparedStatement ps = db.prepareStatement("DELETE FROM DEPT WHERE DEPTNO = (?)");
            ps.setInt(1, numDep);
            i = ps.executeUpdate();
            
            db.commit();
            db.setAutoCommit(true);
            
            ps.close();
            db.close();
            
    	}catch(SQLException e){
            try{
                 db.rollback();
                 
            }catch(SQLException ex){
                 ex.printStackTrace();
            }
            
    	}
    	
    	return i;
    }
    
    //Sube el sueldo a todas las personas del departamento
    public void subirSueldoSet(int suma, int numDep) {
    	try{
    		conectarBDD();
            db.setAutoCommit(false);
            
            Statement stmt = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM EMP WHERE DEPTNO = " + numDep + ";");
            
            while (rs.next()){
            	String nombre = rs.getString("ENAME");
            	int dep = rs.getInt("DEPTNO");
            	double sal = rs.getDouble("SAL");
            	
            	System.out.println("Antes, " + nombre + ": " + sal + " --> " + dep);
            	
            	double nuevoSalario = sal + suma;
                rs.updateDouble("SAL", nuevoSalario);
                rs.updateRow();
                
                sal = rs.getDouble("SAL");
                System.out.println("Despues, " + nombre + ": " + sal + " --> " + dep + "\n");
            }
            
            db.commit();
            db.setAutoCommit(true);
            
            rs.close();
            stmt.close();
            db.close();
            
    	}catch(SQLException e){
    		try{
    			db.rollback();
    			
            }catch(SQLException ex){
                 e.printStackTrace();
            }
    		
    	}
    }
    
    //Muestra el gestor de Base deDatos, el usuario y el driver
    public void verDatos() {
    	try{
    		conectarBDD();
    		
    		DatabaseMetaData datos = (DatabaseMetaData)db.getMetaData();
            System.out.println("Gestor: " + datos.getDatabaseProductName());
            System.out.println("Usuario conectado: " + datos.getUserName());
            System.out.println("Driver utilizado: " + datos.getDriverVersion());
            
            db.close();
             
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    }
    
    //SHOW TABLES, pero diciendo si es una tabla o una vista
    public void showTables() {
    	try{
    		conectarBDD();
    		
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW FULL TABLES;");
            while (rs.next()){
            	
                 if (rs.getString(2).equals("BASE TABLE")) //Si es una tabla
                      System.out.println("TABLA: " + rs.getString(1));
                 else
                	 if (rs.getString(2).equals("VIEW")) //Si es una vista
                      System.out.println("VISTA: " + rs.getString(1));
            }
            
            rs.close();
            stmt.close();
            db.close();
            
    	}catch(SQLException e){
            e.printStackTrace();
    	}
    	
    }
    
    //Imprimir columnas, tipo, tamaño y si admiten nulos. Pasando un Esquema y una tabla
    public void infoTabla(String esquema, String tabla){
        try{
        	conectarBDD();
        	
        	DatabaseMetaData d = (DatabaseMetaData)db.getMetaData();
        	ResultSet rs = d.getColumns(db.getCatalog(), esquema, tabla, null);
        	
        	System.out.println("Esquema\t" + tabla + "\n");
        	
        	while(rs.next())
        		System.out.println("Nombre: " + rs.getString(4) + "\tTamaño: " + rs.getString(7) +
        				"\tTipo: " + rs.getString(6) + "\tValores nulos: " + rs.getString(18));

        	rs.close();
        }catch(SQLException e){
        	e.printStackTrace();
        }
        
    }
    
    //A partir de un esquema y una tabla, imprime las columnas de la clave primaria
    public void infoTablasPrimarias(String esquema, String tabla){
        try{
        	conectarBDD();
        	DatabaseMetaData d = (DatabaseMetaData)db.getMetaData();
        	ResultSet rs = d.getPrimaryKeys(db.getCatalog(), esquema, tabla);
        	
        	System.out.println("Esquema\tTabla\tClave Primaria");
        	
        	while (rs.next())
        		System.out.println(rs.getString(1) + "\t" + rs.getString(3) + "\t" + rs.getString(4));
        	
        	rs.close();
        	
        }catch(SQLException e){
        	e.printStackTrace();
        	
        }
        
    }
    
}