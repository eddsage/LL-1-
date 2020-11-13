/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Gramatica {

    private ArrayList<String> terminales;
    private ArrayList<String> noTerminales;
    private String nTInicial;
    private HashMap<String, ArrayList<String>> producciones;
    //Lectura del Archivo con la gramatica 
    public Gramatica(File archivo) throws FileNotFoundException, IOException {

        this.terminales = new ArrayList<>();
        this.noTerminales = new ArrayList<>();
        this.producciones = new HashMap<>();

        FileReader fr = new FileReader(archivo);
        BufferedReader gramatica = new BufferedReader(fr);

        String linea;
        while ((linea = gramatica.readLine()) != null) {
            buscarTerminales(linea);
            buscarNoTerminales(linea);
            buscarProducciones(linea);
        }

        this.nTInicial = this.noTerminales.get(0);
    }

    public Gramatica(String gramatica) throws FileNotFoundException, IOException {

        this.terminales = new ArrayList<>();
        this.noTerminales = new ArrayList<>();
        this.producciones = new HashMap<>();
//separar gramatica por salto de linea 
        String[] lineas = gramatica.split("\n");

        for (String linea : lineas) {
            buscarTerminales(linea);
            buscarNoTerminales(linea);
            buscarProducciones(linea);
        }

        this.nTInicial = this.noTerminales.get(0);
    }
    
    public ArrayList<String> getTerminales() {
        return terminales;
    }

    public ArrayList<String> getNoTerminales() {
        return noTerminales;
    }

    public String getnTInicial() {
        return nTInicial;
    }

    public HashMap<String, ArrayList<String>> getProducciones() {
        return producciones;
    }

   
//Separacion de lo los terminales
    private void buscarTerminales(String linea) {
        int indiceProduce = linea.indexOf(">");
        String cadenaTerminales = linea.substring(indiceProduce + 1, linea.length());//Lectura a partir  del >
        cadenaTerminales = cadenaTerminales.replaceAll("([A-Z])", "");//reesmplazamos los no terminales por una cadena vacia y nos quedamos con los terminales 
        for (int i = 0; i < cadenaTerminales.length(); i++) {               
            String simbolo = cadenaTerminales.substring(i, i + 1);
            if (!simbolo.equals("&") && !this.terminales.contains(simbolo)) {
                this.terminales.add(simbolo);
            }
        }
    }

    private void buscarNoTerminales(String linea) throws IOException {
        int indiceNTerminal = linea.indexOf("-"); //TE
        String cadenaNTerminales = linea.substring(0, indiceNTerminal);
        if (!this.noTerminales.contains(cadenaNTerminales)) {
            this.noTerminales.add(cadenaNTerminales);
        }
    }

    private void buscarProducciones(String linea) throws IOException {
        String[] expresiones = linea.split("->");//Separa la linea para obtener la produccion 
        if (!this.producciones.containsKey(expresiones[0])) {
            this.producciones.put(expresiones[0], new ArrayList<>());
            this.producciones.get(expresiones[0]).add(expresiones[1]);//guarda los terniales y losnno terminales dentro de producciones
        } else {
            this.producciones.get(expresiones[0]).add(expresiones[1]);
        }
    }
}
