package Practiques.PE07_Escacs.demo;

import java.util.Scanner;

/**
 * Classe principal del joc d'escacs per consola.
 * Gestiona el flux del joc, el tauler, la validació de moviments
 * i la interacció amb l'usuari.
 */
public class PE07HugoCarrasco {

    /*
     * =====================================================
     * CONSTANTS I ATRIBUTS
     * =====================================================
     */
    public static final String RESET = "\u001B[0m";
    public static final String ROIG = "\u001B[31m";
    public static final String VERD = "\u001B[32m";
    public static final String GROC = "\u001B[33m";
    public static final String MORAT = "\u001B[35m";

    // Constants del tauler
    public final int midaTauler = 8;
    public final int filaPromocioBlanques = 0;
    public final int filaPromocioNegres = 7;
    public final int filaInicialPeoBlanc = 6;
    public final int filaInicialPeoNegre = 1;
    public final int filaPecesNegres = 0;
    public final int filaPeonsNegres = 1;
    public final int filaPeonsBlancs = 6;
    public final int filaPecesBlanques = 7;
    public final char primeraFila = 'A';
    public final char ultimaFila = 'H';
    public final int minCoordenada = 0;
    public final int maxCoordenada = 7;

    // Constants de peces
    public final char casellaBuida = '.';
    public final char rei = 'k';
    public final char cavall = 'n';
    public final char peoBlanc = 'P';
    public final char peoNegre = 'p';
    public final char reinaBlanca = 'Q';
    public final char reinaNegra = 'q';

    Scanner scanner = new Scanner(System.in);
    public char[][] tauler = new char[midaTauler][midaTauler];
    String jugadorBlanques, jugadorNegres;
    public boolean tornBlanques = true;
    boolean jocActiu = true;

    public static void main(String[] args) {
        PE07HugoCarrasco joc = new PE07HugoCarrasco();
        joc.iniciarJoc();
    }

    /**
     * Inicia el flux principal del joc.
     * Demana noms, assigna colors, inicialitza el tauler
     * i executa el bucle principal de torns.
     */
    public void iniciarJoc() {
        System.out.println(VERD + "Benvingut als escacs!" + RESET);
        introduirNomsJugadors();
        assignarColors();
        inicialitzarTauler();

        while (jocActiu) {
            mostrarTauler();
            gestionarTorn();
        }
    }

    /**
     * Gestiona un torn complet d'un jugador.
     * Demana coordenades, valida el moviment i l'executa si és correcte.
     */
    public void gestionarTorn() {
        String jugadorActual = tornBlanques ? jugadorBlanques : jugadorNegres;

        System.out.println("\nTorn de: " + GROC + jugadorActual + RESET +
                (tornBlanques ? " (MAJÚSCULES)" : " (minúscules)"));

        int filaOrigen = demanarFila("Fila origen (A-H): ");
        int columnaOrigen = demanarCoordenada("Columna origen (0-7): ");
        int filaDesti = demanarFila("Fila destí (A-H): ");
        int columnaDesti = demanarCoordenada("Columna destí (0-7): ");

        char peca = tauler[filaOrigen][columnaOrigen];

        if (validarMoviment(peca, filaOrigen, columnaOrigen, filaDesti, columnaDesti)) {
            executarMoviment(peca, filaOrigen, columnaOrigen, filaDesti, columnaDesti);
            tornBlanques = !tornBlanques;
        }
    }

    /**
     * Mou una peça dins del tauler i comprova promoció o final de partida.
     *
     * @param peca peça que es mou
     * @param filaOrigen fila inicial
     * @param columnaOrigen columna inicial
     * @param filaDesti fila destí
     * @param columnaDesti columna destí
     */
    public void executarMoviment(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {

        // Si es captura el rei, la partida acaba immediatament
        if (Character.toLowerCase(tauler[filaDesti][columnaDesti]) == rei) {
            System.out.println(VERD + "ESCAC I MAT! Partida guanyada." + RESET);
            jocActiu = false;
        }

        // Promoció automàtica a reina quan el peó arriba al final
        if (peca == peoBlanc && filaDesti == filaPromocioBlanques)
            peca = reinaBlanca;
        if (peca == peoNegre && filaDesti == filaPromocioNegres)
            peca = reinaNegra;

        tauler[filaDesti][columnaDesti] = peca;
        tauler[filaOrigen][columnaOrigen] = casellaBuida;
    }

    /*
     * =====================================================
     * MÒDUL 1: GESTIÓ DEL TAULER
     * =====================================================
     */

    /**
     * Inicialitza el tauler amb la configuració estàndard d'escacs.
     */
    public void inicialitzarTauler() {
        for (int fila = minCoordenada; fila < midaTauler; fila++)
            for (int columna = minCoordenada; columna < midaTauler; columna++)
                tauler[fila][columna] = casellaBuida;

        String ordreInicialPeces = "rnbqkbnr";

        for (int columna = minCoordenada; columna < midaTauler; columna++) {
            tauler[filaPecesNegres][columna] = ordreInicialPeces.charAt(columna);
            tauler[filaPeonsNegres][columna] = peoNegre;
            tauler[filaPeonsBlancs][columna] = peoBlanc;
            tauler[filaPecesBlanques][columna] =
                    Character.toUpperCase(ordreInicialPeces.charAt(columna));
        }
    }

    /**
     * Mostra el tauler actual per consola.
     */
    public void mostrarTauler() {
        System.out.println("\n  0 1 2 3 4 5 6 7");

        for (int fila = minCoordenada; fila < midaTauler; fila++) {
            char lletraFila = (char) (primeraFila + fila);
            System.out.print(lletraFila + " ");

            for (int columna = minCoordenada; columna < midaTauler; columna++) {
                System.out.print(tauler[fila][columna] + " ");
            }
            System.out.println();
        }
    }

    /*
     * =====================================================
     * MÒDUL 3: VALIDACIÓ DE MOVIMENTS
     * =====================================================
     */

    /**
     * Calcula la distància absoluta entre dues coordenades.
     *
     * @param origen coordenada inicial
     * @param desti coordenada destí
     * @return distància absoluta
     */
    public int calcularDistancia(int origen, int desti) {
        int distancia = desti - origen;
        if (distancia < 0) distancia *= -1;
        return distancia;
    }

    /**
     * Valida completament un moviment abans d'executar-lo.
     *
     * @param peca peça que es vol moure
     * @return true si el moviment és vàlid
     */
    public boolean validarMoviment(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {

        if (peca == casellaBuida) {
            System.out.println(ROIG + "Error: No hi ha cap peça en aquesta casella." + RESET);
            return false;
        }

        if (!validarMovimentGeometric(peca, filaOrigen, columnaOrigen, filaDesti, columnaDesti))
            return false;

        if (!pecaPertanyAlJugadorActual(peca)) {
            System.out.println(ROIG + "Error: Aquesta peça no és teva." + RESET);
            return false;
        }

        if (!capturaValida(peca, filaDesti, columnaDesti)) {
            System.out.println(ROIG + "Error: No pots capturar una peça aliada." + RESET);
            return false;
        }

        // Validació delicada: comprova si hi ha peces entre origen i destí
        if (!validarCamiLliure(filaOrigen, columnaOrigen, filaDesti, columnaDesti)) {
            System.out.println(ROIG + "Error: Camí bloquejat per altres peces." + RESET);
            return false;
        }

        return true;
    }

    /**
     * Valida si el moviment és correcte segons la geometria de la peça.
     */
    public boolean validarMovimentGeometric(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        char tipusPeça = Character.toLowerCase(peca);
        boolean movimentCorrecte = false;

        switch (tipusPeça) {
            case 'r': movimentCorrecte = esMovimentTorre(filaOrigen, columnaOrigen, filaDesti, columnaDesti); break;
            case 'b': movimentCorrecte = esMovimentAlfil(filaOrigen, columnaOrigen, filaDesti, columnaDesti); break;
            case 'n': movimentCorrecte = esMovimentCavall(filaOrigen, columnaOrigen, filaDesti, columnaDesti); break;
            case 'q': movimentCorrecte = esMovimentReina(filaOrigen, columnaOrigen, filaDesti, columnaDesti); break;
            case 'k': movimentCorrecte = esMovimentRei(filaOrigen, columnaOrigen, filaDesti, columnaDesti); break;
            case 'p': movimentCorrecte = esMovimentPeo(filaOrigen, columnaOrigen, filaDesti, columnaDesti); break;
        }
        return movimentCorrecte;
    }

    /**
     * Comprova si la peça correspon al jugador del torn actual.
     */
    public boolean pecaPertanyAlJugadorActual(char peca) {
        return tornBlanques ? Character.isUpperCase(peca) : Character.isLowerCase(peca);
    }

    /**
     * Valida si la captura és legal segons el color de les peces.
     */
    public boolean capturaValida(char peca, int filaDesti, int columnaDesti) {
        char pecaDesti = tauler[filaDesti][columnaDesti];
        if (pecaDesti == casellaBuida) return true;
        return Character.isUpperCase(peca) != Character.isUpperCase(pecaDesti);
    }

    public boolean esMovimentTorre(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        if (filaOrigen == filaDesti || columnaOrigen == columnaDesti) return true;
        System.out.println(ROIG + "Error: La torre només es pot moure en línia recta." + RESET);
        return false;
    }

    public boolean esMovimentAlfil(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);

        if (distanciaFila == distanciaColumna && distanciaFila > 0) return true;

        System.out.println(ROIG + "Error: L'alfil només es pot moure en diagonal." + RESET);
        return false;
    }

    public boolean esMovimentCavall(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);

        if (distanciaFila * distanciaColumna == 2) return true;

        System.out.println(ROIG + "Error: El cavall s'ha de moure en forma de 'L'." + RESET);
        return false;
    }

    public boolean esMovimentReina(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);

        boolean comoTorre = (filaOrigen == filaDesti || columnaOrigen == columnaDesti);
        boolean comoAlfil = (distanciaFila == distanciaColumna && distanciaFila > 0);

        if (comoTorre || comoAlfil) return true;

        System.out.println(ROIG + "Error: Moviment de reina no vàlid." + RESET);
        return false;
    }

    public boolean esMovimentRei(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);

        if (distanciaFila <= 1 && distanciaColumna <= 1 && (distanciaFila + distanciaColumna > 0))
            return true;

        System.out.println(ROIG + "Error: El rei només es pot moure una casella." + RESET);
        return false;
    }

    public boolean esMovimentPeo(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {

        int distanciaFila = tornBlanques ? filaOrigen - filaDesti : filaDesti - filaOrigen;
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);

        if (distanciaFila <= 0) {
            System.out.println(ROIG + "Error: El peó no pot retrocedir." + RESET);
            return false;
        }

        if (distanciaColumna == 0) {

            if (tauler[filaDesti][columnaDesti] != casellaBuida) {
                System.out.println(ROIG + "Error: Casella ocupada." + RESET);
                return false;
            }

            if (distanciaFila == 1) return true;

            boolean filaInicial = tornBlanques ?
                    (filaOrigen == filaInicialPeoBlanc) :
                    (filaOrigen == filaInicialPeoNegre);

            if (distanciaFila == 2 && filaInicial) return true;

        } else if (distanciaColumna == 1 && distanciaFila == 1) {

            if (tauler[filaDesti][columnaDesti] != casellaBuida)
                return true;

            System.out.println(ROIG + "Error: El peó només captura en diagonal." + RESET);
            return false;
        }

        System.out.println(ROIG + "Error: Moviment de peó no permès." + RESET);
        return false;
    }

    /**
     * Comprova si el camí entre origen i destí està lliure.
     * El cavall queda exclòs perquè pot saltar peces.
     */
    public boolean validarCamiLliure(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {

        if (Character.toLowerCase(tauler[filaOrigen][columnaOrigen]) == cavall)
            return true;

        int direccioFila = Integer.compare(filaDesti, filaOrigen);
        int direccioColumna = Integer.compare(columnaDesti, columnaOrigen);

        int filaActual = filaOrigen + direccioFila;
        int columnaActual = columnaOrigen + direccioColumna;

        while (filaActual != filaDesti || columnaActual != columnaDesti) {
            if (tauler[filaActual][columnaActual] != casellaBuida)
                return false;

            filaActual += direccioFila;
            columnaActual += direccioColumna;
        }

        return true;
    }

    /*
     * =====================================================
     * MÒDUL 4: ENTRADA/SORTIDA
     * =====================================================
     */

    /**
     * Demana una fila en format lletra (A-H).
     *
     * @return índex de fila dins la matriu
     */
    public int demanarFila(String missatge) {
        while (true) {
            System.out.print(missatge);
            String lletraIntroduida = scanner.next().toUpperCase();

            if (lletraIntroduida.length() == 1) {
                char lletra = lletraIntroduida.charAt(0);
                if (lletra >= primeraFila && lletra <= ultimaFila)
                    return lletra - primeraFila;
            }

            System.out.println(ROIG + "Error: Lletra entre A i H." + RESET);
        }
    }

    /**
     * Demana una coordenada numèrica entre 0 i 7.
     *
     * @return coordenada vàlida
     */
    public int demanarCoordenada(String missatge) {

        int coordenadaIntroduida;

        while (true) {
            System.out.print(missatge);

            if (scanner.hasNextInt()) {
                coordenadaIntroduida = scanner.nextInt();

                if (coordenadaIntroduida >= minCoordenada &&
                        coordenadaIntroduida <= maxCoordenada)
                    return coordenadaIntroduida;
            } else {
                scanner.next();
            }

            System.out.println(ROIG + "Error: Número entre 0 i 7." + RESET);
        }
    }

    /**
     * Demana els noms dels jugadors.
     */
    public void introduirNomsJugadors() {
        System.out.print("Jugador 1: ");
        jugadorBlanques = scanner.next();
        System.out.print("Jugador 2: ");
        jugadorNegres = scanner.next();
    }

    /**
     * Assigna aleatòriament quin jugador porta blanques o negres.
     */
    public void assignarColors() {
        if (Math.random() > 0.5) {
            String jugadorTemporal = jugadorBlanques;
            jugadorBlanques = jugadorNegres;
            jugadorNegres = jugadorTemporal;
        }
        System.out.println("Blanques: " + jugadorBlanques + " | Negres: " + jugadorNegres);
    }
}
