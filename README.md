# LL-1

Una analizador LL(1), con una interfaz de usuario basica, factorizando una gramatica independiente del contexto (Recursiva a la izquierda y Factorizable por la izquierda).Ademas de como hallar los conjuntos PRIMEROS y SIGUIENTES de la gramatica, y obtener su respectiva tabla  (tabla de análisis sintáctico).


Una gramatica es LL(1) si:

Dada la gramatica

A->αi|αi+1|αi+2|...|αn

1. No tiene recursividad a la izquierda
2. PRIMERO(αi) ∩ PRIMERO(αj) = ∅ para todo i ≠ j (No es factorizable por la izquierda)
# Funcionamiento

El proyecto se inicializa en la clase LL1.java que se encuentra en ll.pkg1, de manera grafica.



# Uso
Dentro de la interrfaz, existe un boton de "Escoger gramatica" y seleccionamos un archivo de texto con el 
siguiente formato.
File (Archivo .txt o un tipo de documento de texto sencillo)

    E->E+T
    E->T
    T->T*F
    T->F
    F->i
    F->(E)

Las salidas seran respectivamente:

    1. Gramatica original
    3. Conjunto de PRIMEROS
    4. Conjunto de SIGUIENTES
    5. Tabla de análisis sintáctico

Y ademas se dara la opción de poder verificar una cadena.
