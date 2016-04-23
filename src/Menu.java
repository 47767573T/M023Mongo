import Gestor.Conflicto;
import Gestor.GrupoArmado;

import com.mongodb.*;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.codehaus.jackson.map.ObjectMapper;
import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.util.*;
import static java.util.Arrays.asList;


public class Menu {

    static MongoClient mongoClient = null;
    static MongoCollection<Document> conflictosCol;
    static MongoCollection <Document> grupoArmadosCol;

    public static void main( String args[] ) throws IOException {

        //Creamos cliente e Instanciamos la data base (si no existe la crea)
        mongoClient = new MongoClient(new ServerAddress("192.168.56.1"));
        MongoDatabase db = mongoClient.getDatabase("Guerras");

        //Definimos e instanciamos las colecciones
        conflictosCol = db.getCollection("conflicto");
        grupoArmadosCol = db.getCollection("grupoArmado");
        System.out.println("conflictosCol: " + conflictosCol);
        System.out.println("grupoArmadosCol: " + grupoArmadosCol);

        // Menu
        Scanner scn = new Scanner(System.in);

        boolean enMenu = true;
        int opcionMenu;

        while (enMenu){

            System.out.println("\nMENU:");
            System.out.println("----------------------------------------------------");
            System.out.println("1 - Alta de grupo armado...........................|");
            System.out.println("2 - Alta de conflicto..............................|");
            System.out.println("3 - Consulta de conflictos con mas de 300 heridos..|");
            System.out.println("4 - Consulta de informacion de conflicto...........|");
            System.out.println("0 - Sortir.........................................|");
            opcionMenu = scn.nextInt();

            switch (opcionMenu){

                case 0:
                    enMenu = false;
                    mongoClient.close();
                    break;

                case 1: //Dar alta grupo armado
                    altaGrupoArmado();
                    break;

                case 2: //Dar alta de conflicto
                    altaConflicto();
                    break;

                case 3: //Consulta de conflictos segun tenga mas de 300 bajas
                    consultaHeridos();
                    break;

                case 4:
                    // Retirar jugador
                    consultaConflictos();
                    break;


            }
        }


    }

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