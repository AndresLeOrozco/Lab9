import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUD {
    Connection cn;
    public Connection conect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto2_chat?allowPublicKeyRetrieval=true&useSSL=false", "root", "hola123");
            System.out.println("conexion establecida");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return cn;
    }
    public void close() throws SQLException {
        cn.close();
    }
    public boolean UsuarioRepetido(String n){

        String sql = "select * from usuario where nombre_usuario = '"+n+"'";
        try {
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }

        return false;
    }
    public boolean IdRepetido(Integer n){

        String sql = "select * from usuario where id_usuario = " +n;
        try {
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }

        return false;
    }


    public void InsertaUsuario(String n,String c) throws Exception {
        conect();
        if(!UsuarioRepetido(n)) {
            String sql = "INSERT INTO usuario (nombre_usuario,clave_usuario) " +
                    "VALUES " +
                    "(?,?)";
            //getting input from user

            try {
                PreparedStatement statement = cn.prepareStatement(sql);
                //setting parameter values
                statement.setString(1, n);
                statement.setString(2, c);

                //executing query which will return an integer value
                int rowsInserted = statement.executeUpdate();
                //if rowInserted is greater than 0 mean rows are inserted
                if (rowsInserted > 0) {
                    System.out.println("Usuario Insertado");
                }
            } catch (Exception e) {
                System.out.println("Exception in connection: " + e.toString());
            }
            finally {
                close();
            }
        }else{
            close();
            throw new Exception("Usuario Repetido");
        }

    }

    public boolean ContactoRepetido(Integer n,Integer Co){

        String sql = "select * from contacto where dueno_lista = "+n+" and contacto_lista = "+Co;
        try {
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }

        return false;
    }

    public User Buscar(String Bus){
        conect();
        String sql = "select * from usuario where nombre_usuario like '%"+Bus+"%'";
        try {
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

           if(rs.next()){
               String id = rs.getString("id_usuario");
               String nom = rs.getString("nombre_usuario");
               String clav = rs.getString("clave_usuario");
               User u= new User(id,clav,nom);
               close();
               return u;
           }
            close();
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }
        return null;

    }

    public List<User> retornaContactos(Integer id){
        conect();
        List<User> contactos = new ArrayList<>();
        String sql = "SELECT *\n" +
                "FROM usuario\n" +
                "LEFT JOIN contacto\n" +
                "ON usuario.id_usuario = contacto.contacto_lista\n" +
                "where contacto.dueno_lista ="+id;
        try {
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                String idU = rs.getString("id_usuario");
                String nom = rs.getString("nombre_usuario");
                String clav = rs.getString("clave_usuario");
                User u= new User(idU,clav,nom);
                contactos.add(u);
            }
            close();
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }
        return contactos;
    }

    public void AgregaContacto(Integer idUsuario,Integer idContacto){
        conect();
        if(IdRepetido(idUsuario) && IdRepetido(idContacto)){
            if(!ContactoRepetido(idUsuario,idContacto)) {
                String sql = "INSERT INTO contacto (dueno_lista,contacto_lista) " +
                        "VALUES " +
                        "(?,?)";
                //getting input from user

                try {
                    PreparedStatement statement = cn.prepareStatement(sql);
                    //setting parameter values
                    statement.setInt(1, idUsuario);
                    statement.setInt(2,idContacto);

                    //executing query which will return an integer value
                    int rowsInserted = statement.executeUpdate();
                    //if rowInserted is greater than 0 mean rows are inserted
                    if (rowsInserted > 0) {
                        System.out.println("Usuario Insertado");
                        close();
                    }
                } catch (Exception e) {
                    System.out.println("Exception in connection: " + e.toString());
                } finally {
                    try {
                        close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        CRUD cr = new CRUD();
        cr.AgregaContacto(4,2);
    }
}
