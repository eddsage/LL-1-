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
//leemos el archivo de la gramatica
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

   
//busca los terminales apartir del simbolo > 
    private void buscarTerminales(String linea) {
        int indiceProduce = linea.indexOf(">");
        String cadenaTerminales = linea.substring(indiceProduce + 1, linea.length());
        cadenaTerminales = cadenaTerminales.replaceAll("([A-Z])", "");
        for (int i = 0; i < cadenaTerminales.length(); i++) {
            String simbolo = cadenaTerminales.substring(i, i + 1);
            if (!simbolo.equals("&") && !this.terminales.contains(simbolo)) {
                this.terminales.add(simbolo);
            }
        }
    }
//busca los no terminales liena por linea de la gramatica por el simbolo -
    
    private void buscarNoTerminales(String linea) throws IOException {
        int indiceNTerminal = linea.indexOf("-");
        String cadenaNTerminales = linea.substring(0, indiceNTerminal);
        if (!this.noTerminales.contains(cadenaNTerminales)) {
            this.noTerminales.add(cadenaNTerminales);
        }
    }
//guardamos los terminales y  los noterminales en Producciones por ejemplo T*E en 
    private void buscarProducciones(String linea) throws IOException {
        String[] expresiones = linea.split("->");
        if (!this.producciones.containsKey(expresiones[0])) { //guardamos la -> como llave en el hashmap
            this.producciones.put(expresiones[0], new ArrayList<>());
            this.producciones.get(expresiones[0]).add(expresiones[1]);
            
        } else {
            this.producciones.get(expresiones[0]).add(expresiones[1]);
        }
    }
}
