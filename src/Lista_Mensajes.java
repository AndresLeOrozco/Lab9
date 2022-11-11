import java.util.Date;

public class Lista_Mensajes {
   private int id_lista_mensaje;
   private int id_remitente;
   private int id_receptor;
   private Date fechahora;
   private String mensaje;

    public Lista_Mensajes() {
        this.id_lista_mensaje = 0;
        this.id_remitente = 0;
        this.id_receptor = 0;
        this.fechahora = new Date();
        this.mensaje = "";
    }

    public Lista_Mensajes(int id_lista_mensaje, int id_remitente, int id_receptor, Date fechahora, String mensaje) {
        this.id_lista_mensaje = id_lista_mensaje;
        this.id_remitente = id_remitente;
        this.id_receptor = id_receptor;
        this.fechahora = fechahora;
        this.mensaje = mensaje;
    }
}
