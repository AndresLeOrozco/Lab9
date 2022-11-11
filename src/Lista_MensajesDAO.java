import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

public class Lista_MensajesDAO {
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

    public LinkedList<Lista_Mensajes> getListaMensajesPendientes() throws SQLException {

        String sql= "SELECT * from proyecto2_chat.lista_mensaje";
        LinkedList<Lista_Mensajes> lm= new LinkedList<>();
            try{
                conect();
                Statement st;
                st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()) {
                     int id_lista_mensaje= Integer.parseInt(rs.getString("id_lista_mensaje"));
                     int id_remitente= Integer.parseInt(rs.getString("id_remitente"));
                     int id_receptor= Integer.parseInt(rs.getString("id_receptor"));
                     Date fechahora= rs.getDate("fechahora");
                     String mensaje=rs.getString("mensaje");
                     Lista_Mensajes l= new Lista_Mensajes(id_lista_mensaje,id_remitente,id_receptor,fechahora,mensaje);
                     lm.add(l);
                }
                return lm;

            }catch (Exception e){
                System.out.print(e.getMessage());
                return lm;
            }finally {
                close();
            }

    }

}
