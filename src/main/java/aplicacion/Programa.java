/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;


import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import modelo.Conexion;
import modelo.FacturaDAO;
import modelo.FacturaVO;

/**
 *
 * @author J. Carlos F. Vico <jcarlosvico@maralboran.es>
 */
public class Programa {

    public static void main(String[] args) throws SQLException {
        
//        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/p81JuanDiegoMarinMorales?zeroDateTimeBehavior=CONVERT_TO_NULL");
        Connection con= Conexion.getInstance();
        
        FacturaDAO daoFactura = new FacturaDAO();
        List<FacturaVO> listaFacturas = new ArrayList<>();
        
        String linea = "";
        String[]tokens;
        try (Scanner datosFichero = new Scanner(new File("facturas.csv"), "UTF-8")) {
            // hasNextLine devuelve true mientras haya líneas por leer
            while (datosFichero.hasNextLine()) {
                // Guarda la línea completa en un String
                linea = datosFichero.nextLine();
                
                linea=linea.replaceAll("Factura[{]", "");
                linea=linea.replaceAll("[}]", "");
                linea=linea.replaceAll("fechaEmision=", "");
                linea=linea.replaceAll("codigoUnico=", "");
                linea=linea.replaceAll("descripcion=", "");
                linea=linea.replaceAll("totalImporte=", "");
                linea=linea.replaceAll(" ", "");
                
                
                
                tokens=linea.split(";");
                
                FacturaVO f= new FacturaVO();
                
                f.setPk(Integer.valueOf(tokens[0]));
                
                if (tokens[1].equals("")) {

                        f.setFechaEmision(null);
                    } else {

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate fecha = LocalDate.parse(tokens[1], formatter);

                        f.setFechaEmision(fecha);
                    }
                
                f.setDescripcion(tokens[2]);
                f.setTotalImporte(Double.valueOf(tokens[3]));
                
//                for (int i = 0; i < tokens.length; i++) {
//                    System.out.println(i+" "+tokens[i]);
//                }
                // Se guarda en el array de String cada elemento de la
                // línea en función del carácter separador de campos del fichero CSV
                
                listaFacturas.add(f);
                System.out.println(linea);

            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println("--------------------------------------------------");
        listaFacturas.forEach(System.out::println);
         System.out.println("--------------------------------------------------");
         
         
        
        try {
            
            System.out.println("Nº facturas insertadas " + daoFactura.insertFactura(listaFacturas));
            System.out.println("-----------------------------------------");
            System.out.println("Comprobamos en una nueva lista que se recogen los datos desde la tabla.");
            List<FacturaVO> nuevaLista = daoFactura.getAll();
            System.out.println("-------- Lista con datos recogidos desde la B.D -------------");
            nuevaLista.forEach(System.out::println);
            System.out.println("-----------------------------------------");
            System.out.println("Factura con primary key 1: ");
            System.out.println(daoFactura.findByPk(1));
            System.out.println("-----------------------------------------");
            System.out.println("Se va a borrar la factura con pk 3");
            System.out.println("Nº personas borradas " + 
                    daoFactura.deleteFactura(new FacturaVO(3, LocalDate.of(2023, 5, 8), "VbZHjTHEgR", 746.7096430266851)));
            System.out.println("-----------------------------------------");
            nuevaLista = daoFactura.getAll();
            System.out.println("-------- Lista con datos recogidos desde la B.D despues de borrar una persona -------------");
            nuevaLista.forEach(System.out::println);
            System.out.println("-----------------------------------------");
            System.out.println("Modificación de la persona con pk 5");
            System.out.println("Nº Personas modificadas " + 
                    daoFactura.updateFactura(5, new FacturaVO(5, LocalDate.now(), "", 0)));
            System.out.println("-----------------------------------------");
            nuevaLista = daoFactura.getAll();
            System.out.println("-------- Lista con datos recogidos desde la B.D despues de modificar una persona -------------");
            nuevaLista.forEach(System.out::println);
        } catch (SQLException sqle) {
            System.out.println("No se ha podido realizar la operación:");
            System.out.println(sqle.getMessage());
        }
        System.out.println("-------- Lista original --------------------");
        listaFacturas.forEach(System.out::println);

    }

}
