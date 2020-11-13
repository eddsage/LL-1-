/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.Iterator;
/**
 *
 * @author ferch5003
 */
public class Primero {

    private HashMap<String, Set<String>> primeros;
    private HashMap<String, HashMap<String, Set<String>>> valoresM;
    private HashMap<String, ArrayList<String>> producciones;
    private ArrayList<String> noTerminales;
    private HashMap<String, Queue<String>> nTPrimeros;

    public Primero(Producciones prod) {
        this.primeros = new HashMap<>();
        this.nTPrimeros = new HashMap<>();
        this.valoresM = new HashMap<>();
        this.producciones = new HashMap<>();
        this.noTerminales = prod.getNoTerminales();
        
     
        prod.getProducciones().forEach((noTerminal, producciones) -> {//Recorremos el hashmap para sacar los no terminales y las producciones
            this.producciones.put(noTerminal, producciones);
        });

        construirValoresM(prod);//No trminales a una cadena de valores 

        construirNTPrimeros(prod);

        construirProducciones(prod);

        construirPrimero(prod);

        verifCiclos(prod);

        verifEpsilon(prod);
    }
    

    public HashMap<String, Set<String>> getPrimeros() {
        return primeros;
    }

    public HashMap<String, HashMap<String, Set<String>>> getValoresM() {
        return valoresM;
    }

     
    private void construirValoresM(Producciones prod) {
        for (String noTerminal : prod.getNoTerminales()) {
            HashMap<String, Set<String>> valor = new HashMap<>();
            
            ArrayList<String> producciones = prod.getProducciones().get(noTerminal);
            for (String produccion : producciones) {
                valor.put(produccion, new HashSet<>());
            }
            this.valoresM.put(noTerminal, valor);
        }
    }

    private void construirNTPrimeros(Producciones prod) {
        for (String noTerminal : prod.getNoTerminales()) {
            this.nTPrimeros.put(noTerminal, new LinkedList<>());
        }
    }

    private void construirProducciones(Producciones prod) {
        for (String noTerminal : prod.getNoTerminales()) {
            this.primeros.put(noTerminal, new HashSet<>());
        }
    }

    private void construirPrimero(Producciones prod) {
        for (String noTerminal : prod.getNoTerminales()) {
            ArrayList<String> producciones = prod.getProducciones().get(noTerminal);
            for (String prod : producciones) {
                calcularPrimero(noTerminal, prod);
            }
        }
    }

    private void calcularPrimero(String A, String produccion) {
        String prod = "";
        for (int i = 0; i < produccion.length(); i++) {
            String primeraCad = produccion.substring(i, i + 1);
            if (esTerminal(primeraCad)) {
                if (i == 0) {
                    this.primeros.get(A).add(primeraCad);
                } else {
                    prod += primeraCad;
                }
                Set<String> terminal = new HashSet<>();
                if (!primeraCad.equals("&")) {
                    terminal.add(primeraCad);
                }
                this.valoresM.get(A).put(produccion, terminal);
                break;
            } else {
                prod += primeraCad;
            }
        }
        this.nTPrimeros.get(A).add(prod);
    }

    private void verifCiclos(Producciones prod) {
        for (int j = 0; j < 2; j++) {
            int ultimaPos = this.noTerminales.size() - 1;
            for (int i = ultimaPos; i >= 0; i--) {
                String noTerminal = this.noTerminales.get(i);
                Queue<String> ciclo = this.nTPrimeros.get(noTerminal);
                Set<String> union = new HashSet<>();
                Set<String> A = this.primeros.get(noTerminal);
                union.addAll(A);
                for (String produccion : ciclo) {
                    for (char simb : produccion.toCharArray()) {
                        String simbolo = Character.toString(simb);
                        Set<String> B = new HashSet<>();
                        if (esTerminal(simbolo)) {
                            B.add(simbolo);
                            union.addAll(B);
                            agregarValorM(prod, noTerminal, produccion, B);
                            break;
                        } else {
                            B = this.primeros.get(simbolo);
                            union.addAll(B);
                            agregarValorM(prod, noTerminal, produccion, B);
                            if (!B.contains("&")) {
                                break;
                            }
                        }
                    }
                }
                this.primeros.get(noTerminal).addAll(union);
            }
        }
    }

    private void agregarValorM(Producciones prod, String noTerminal, String produccion, Set<String> B) {
        ArrayList<String> producciones = prod.getProducciones().get(noTerminal);
        for (String prod : producciones) {
            if (prod.contains(produccion)) {
                this.valoresM.get(noTerminal).get(prod).addAll(B);
            }
        }
    }

    private void verifEpsilon(Producciones prod) {
        prod.getProducciones().forEach((noTerminal, producciones) -> {
            int epsilon = 0;
            if (!producciones.contains("&")) {
                for (String prod : producciones) {
                    if (!esTerminal(prod.substring(0, 1))) {
                        for (char a : prod.toCharArray()) {
                            String simbolo = Character.toString(a);
                            if (!esTerminal(simbolo)) {
                                if (this.primeros.get(simbolo).contains("&")) {
                                    epsilon++;
                                }
                            }
                        }
                        if (epsilon < prod.length()) {
                            this.primeros.get(noTerminal).remove("&");
                            this.valoresM.get(noTerminal).get(prod).remove("&");
                        }
                    }
                }
            }
        });
    }

    private boolean esTerminal(String cadena) {
        return Pattern.matches("[A-Z]", cadena) ? false : true;
    }
}
