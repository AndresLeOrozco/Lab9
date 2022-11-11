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

    //Registrar Usuario
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

    //Filtrar
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

    //Retorna Contactos
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

    //Agregar Usuario Lista Contacto
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

    //Crear Sala
    public void CrearSala(String nombre, Integer admin, String descripcion) throws Exception {
        conect();
        String sql = "INSERT INTO sala (nombre_sala, admin_sala, descripcion_sala)" + "VALUES " + "(?,?,?)";

        try {
            PreparedStatement statement = cn.prepareStatement(sql);

            statement.setString(1, nombre);
            statement.setInt(2, admin);
            statement.setString(3,descripcion);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Sala Creada");
            }
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }
        finally {
            close();
        }
    }

    //Afiliar Usuario a Sala
    public void AgregaMiembroSala(Integer idSala,Integer idUsuario) throws Exception {
        conect();
        String sql = "INSERT INTO miembros_sala (id_sala, id_usuario)" + "VALUES " + "(?,?)";
        //getting input from user

        try {
            PreparedStatement statement = cn.prepareStatement(sql);
            //setting parameter values
            statement.setInt(1, idSala);
            statement.setInt(2, idUsuario);

            //executing query which will return an integer value
            int rowsInserted = statement.executeUpdate();
            //if rowInserted is greater than 0 mean rows are inserted
            if (rowsInserted > 0) {
                System.out.println("Usuario insertado a la sala");
            }
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }
        finally {
            close();
        }
    }

    //Desafiliar Usuario a Sala
    public void EliminarMiembroSala(Integer idSala,Integer idUsuario) throws Exception {
        conect();
        String sql = "DELETE FROM miembros_sala WHERE id_sala = ? and id_usuario = ?";

        try {
            PreparedStatement statement = cn.prepareStatement(sql);
            statement.setInt(1, idSala);
            statement.setInt(2, idUsuario);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Usuario elimnado de la sala");
            }
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }
        finally {
            close();
        }
    }

    public int GetIDUser(String userName) throws Exception{
        conect();
        String sql = "SELECT id_usuario FROM usuario where nombre_usuario = "+"'"+userName+"'";

        try {
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if(rs.next()){
                return rs.getInt("id_usuario");
            }
            close();
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }
        return 0;
    }

    public int GetIDSala(String sala) throws Exception{
        conect();
        String sql = "SELECT id_sala FROM sala where nombre_sala = "+"'"+sala+"'";

        try {
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if(rs.next()){
                return rs.getInt("id_sala");
            }
            close();
        } catch (Exception e) {
            System.out.println("Exception in connection: " + e.toString());
        }
        return 0;
    }

    public static void main(String[] args) {
        CRUD cr = new CRUD();
        cr.AgregaContacto(4,2);

        /*
        cr.InsertaUsuario("Jequiros","cacahuate");
        cr.InsertaUsuario("MariaJJ", "amarillo");
        cr.InsertaUsuario("Juan", "skittles");

        cr.CrearSala("Memes",cr.GetIDUser("MariaJJ"),"Pasar memes");

        cr.AgregaMiembroSala(cr.GetIDSala("Memes"),cr.GetIDUser("Jequiros"));
        cr.AgregaMiembroSala(cr.GetIDSala("Memes"),cr.GetIDUser("Juan"));
        cr.AgregaMiembroSala(cr.GetIDSala("Memes"),cr.GetIDUser("MariaJJ"));

        cr.EliminarMiembroSala(cr.GetIDSala("Memes"),cr.GetIDUser("Jequiros"));
        */
    }
}
