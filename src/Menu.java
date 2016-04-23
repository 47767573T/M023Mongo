import Gestor.Conflicto;
import Gestor.GrupoArmado;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.codehaus.jackson.map.ObjectMapper;
import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Filters.gt;
import static java.util.Arrays.asList;


public class Menu {

    static MongoClient mongoClient = null;
    static MongoCollection<Document> conflictosCol;
    static MongoCollection <Document> grupoArmadosCol;

    public static void main( String args[] ) throws IOException {

        //Creamos cliente e Instanciamos la data base (si no existe la crea)
        mongoClient = new MongoClient(new ServerAddress("192.168.56.1"));
        MongoDatabase db = mongoClient.getDatabase("Guerras");

        //Definimos e instanciamos las colecciones (si no existe la crea)
        conflictosCol = db.getCollection("conflicto");
        grupoArmadosCol = db.getCollection("grupoArmado");

        // Menu
        Scanner scn = new Scanner(System.in);

        boolean enMenu = true;
        int opcionMenu;

        while (enMenu){

            System.out.println("\nMENU:");
            System.out.println("-----------------------------------------------------");
            System.out.println("|.1 - Alta de grupo armado..........................|");
            System.out.println("|.2 - Alta de conflicto.............................|");
            System.out.println("|.3 - Consulta de conflictos con mas de 300 heridos.|");
            System.out.println("|.4 - Consulta de informacion de conflicto..........|");
            System.out.println("|.0 - Sortir........................................|");
            System.out.println("-----------------------------------------------------");
            opcionMenu = scn.nextInt();

            switch (opcionMenu){

                case 0:     //Salir del menu y cerrar cliente de mongo
                    enMenu = false;
                    mongoClient.close();
                    break;

                case 1:     //Dar alta grupo armado
                    altaGrupoArmado();
                    break;

                case 2:     //Dar alta de conflicto
                    altaConflicto();
                    break;

                case 3:     //Consulta de conflictos segun tenga mas de 300 bajas
                    consultaHeridos();
                    break;

                case 4:     // Consulta los datos de un conflicto solicitado
                    mostrarConflicto();
                    break;
            }
        }
    }

    /**
     * Ingresa una tupla en la coleccion de grupo armado
     */
    private static void altaGrupoArmado() {

        //Preparamos el alta
        Document grupoDoc = new Document();

        //Preguntamos los datos del alta
        grupoDoc.put("_id", leerInteger("Código:"));
        grupoDoc.put("nombre", leerString("Nombre:"));
        grupoDoc.put("bajas", leerInteger("bajas:"));

        //Insertamos el grupo armado en la db
        try{
            grupoArmadosCol.insertOne(grupoDoc);
        }catch (Exception e){
            System.out.println("Grupo armado existente. Cambie los datos");
        }
    }

    /**
     * Ingresa una tupla en la coleccion de conflicto
     */
    private static void altaConflicto() throws IOException {

        //Preparamos el alta
        Document conflictoDoc = new Document();

        //Preguntamos los datos del alta
        conflictoDoc.put("_id", leerInteger("Código:"));
        conflictoDoc.put("nombre", leerString("Nombre:"));
        conflictoDoc.put("zona", leerString("Zona:"));
        conflictoDoc.put("heridos", leerInteger("Heridos:"));
        conflictoDoc.put("grupoArmado", asList());

        boolean hayAgregar = true;

        //Guardamos grupos armados en el conflicto
        while(hayAgregar) {
            int opcionGrupoArmado =  leerInteger("Añadir grupo armado?\n1.- si\n2.- no");

            if (opcionGrupoArmado == 1){
                System.out.println("Listado de grupos armados: ");
                for (Document doc: grupoArmadosCol.find()) {
                    System.out.println(doc.toJson());
                }

                //Guardamos grupo armado nuevo o ya existente
                int opcionNuevo = leerInteger("\nEs nuevo?\n1.- si\n2.- no");
                    if (opcionNuevo == 2){
                        int idGA = leerInteger("introduzca id: ");
                        Document grupoArmado = grupoArmadosCol.find(eq("_id", idGA)).first();
                        conflictoDoc.put("grupoArmado", grupoArmado);

                    }else if (opcionNuevo == 1) altaGrupoArmado();

                    else System.out.println("Opcion de si es nuevo incorrecta!");

            }else if (opcionGrupoArmado == 2) hayAgregar = false;

            else System.out.println("Opcion de añadir grupo incorrecta!");
        }

        //Una vez determinemos todos los datos del conflicto (incluidos los grupos armados implicados)
        //, insertamos el conflicto
        conflictosCol.insertOne(conflictoDoc);
    }

    /**
     * Realiza y muestra una consulta de conflictos con mas de 300 heridos
     */
    public static void consultaHeridos() {

        //Creamos un BD Objecto definido para busqueda
        BasicDBObject DBObj = new BasicDBObject("$gt", 300);

        //Realizamos la busqueda
        FindIterable<Document> iterator = conflictosCol.find(new BasicDBObject("heridos", DBObj));

        //Mostramos el resultado de busqueda
        for (Document doc: iterator){
            System.out.println(doc.toJson());
        }
    }

    /**
     * Realiza y muestra informacion de un conflicto solicitado
     */
    public static void mostrarConflicto(){

        //Preguntamos por nombre de conflicto a buscar
        String nombre = leerString("Nombre del conflicto: ");

        //Realizamos la busqueda
        FindIterable<Document> iterator = conflictosCol.find(new BasicDBObject("nombre", nombre));

        //Mostramos el resultado de la busqueda
        for (Document doc: iterator){
            System.out.println(doc.toJson());
        }
    }

    /**
     * Metodo para leer y validar entradas de usuario de tipo String
     * @param pregunta la pregunta que se realiza al usuario
     * @return respuesta de usuario
     */
    public static int leerInteger(String pregunta) {
        Scanner in = new Scanner(System.in);
        int res = -1;
        while (res == -1) {
            System.out.print(pregunta + ": ");
            try {
                res = in.nextInt();
            } catch (Exception ex) {
                System.out.println("Entrada invalida.");
            }
            in.nextLine();
        }
        return res;
    }

    /**
     * Metodo para leer y validar entradas de usuario de tipo Integer
     * @param pregunta la pregunta que se realiza al usuario
     * @return respuesta de usuario
     */
    public static String leerString(String pregunta) {
        Scanner in = new Scanner(System.in);
        String res = null;
        while (res == null) {
            System.out.print(pregunta + ": ");
            res = in.next();
            if ("".equals(res))
                res = null;
            in.nextLine();
        }
        return res;
    }



}