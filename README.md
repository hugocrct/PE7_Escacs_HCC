# Joc d’Escacs — Documentació funcional

## 🎯 Descripció general

Aquest projecte implementa un joc d’escacs per consola en Java. El programa gestiona un tauler, permet introduir moviments per teclat i aplica validacions bàsiques abans d’actualitzar l’estat del joc.

L’objectiu principal és practicar lògica, estructuració del codi i validació d’entrada, no construir un motor d’escacs complet.

---

## ▶️ Funcionament del programa

Quan s’executa la classe principal:

1. Es crea el tauler d’escacs.
2. Es mostren les peces per consola amb colors.
3. L’usuari introdueix coordenades de moviment.
4. El sistema valida:

   * Que les coordenades siguin dins del tauler.
   * Que la casella origen contingui una peça vàlida.
   * Que el moviment compleixi les regles implementades.
5. Si el moviment és correcte, el tauler s’actualitza.
6. Si és incorrecte, es mostra un missatge i no es modifica l’estat.

El joc continua fins que es decideix finalitzar manualment.

---

## 🧩 Estructura interna del codi

El codi està organitzat principalment en mètodes dins d’una única classe principal:

### Inicialització

* Creació del tauler.
* Assignació de peces inicials.

### Visualització

* Impressió del tauler per consola.
* Ús de codis ANSI per mostrar colors.

### Entrada d’usuari

* Lectura de dades mitjançant `Scanner`.
* Conversió d’entrada a coordenades de matriu.

### Validació

* Comprovació de límits del tauler.
* Verificació de moviments permesos segons la peça.
* Control d’errors abans d’aplicar canvis.

### Actualització d’estat

* Moviment de peces dins la matriu.
* Neteja de la casella origen.

---

## ♟️ Representació del tauler

El tauler es representa amb una **matriu bidimensional**.

* Files i columnes corresponen a posicions del tauler.
* Cada cel·la guarda el símbol d’una peça o una casella buida.
* Aquesta decisió simplifica l’accés directe i la visualització.

---

## ✅ Validació de moviments

Abans de moure una peça es comprova:

* Coordenades dins del rang.
* Existència d’una peça a l’origen.
* Que el moviment no trenqui les regles implementades.

Si alguna validació falla:

* No s’actualitza el tauler.
* Es mostra un missatge d’error per consola.

Això evita inconsistències a la partida.

---

## 🧪 Tests

Els mètodes rellevants són públics per permetre proves unitàries.

Els tests poden verificar:

* Validacions de moviment.
* Canvis d’estat del tauler.
* Funcions de suport independents.

---

## 📁 Estructura de carpetes

```
Practiques/
└── PE07_Escacs/
    └── demo/
        └── src/
            └── main/
                └── java/
                    └── PE07HugoCarrasco.java
```

---

## 📚 Documentació interna

Els mètodes públics inclouen Javadoc amb:

* Descripció del comportament.
* Paràmetres d’entrada.
* Valor de retorn.
* Condicions d’error o casos límit.

Els comentaris dins del codi només apareixen quan expliquen una validació complexa o una decisió que no és evident llegint el codi.
