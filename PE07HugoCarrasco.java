package Practiques.PE7_Escacs;

import java.util.Scanner;

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
    Scanner scanner = new Scanner(System.in);
    char[][] tauler = new char[8][8];
    String jugadorBlanques, jugadorNegres;
    boolean tornBlanques = true;
    boolean jocActiu = true;

    public static void main(String[] args) {
        PE07HugoCarrasco joc = new PE07HugoCarrasco();
        joc.iniciarJoc();
    }

    private void iniciarJoc() {
        System.out.println(VERD + "Benvingut als escacs!" + RESET);
        introduirNomsJugadors();
        assignarColors();
        inicialitzarTauler();
        while (jocActiu) {
            mostrarTauler();
            gestionarTorn();
        }
    }

    /*
     * =====================================================
     * MÒDUL: GESTIÓ DE TORN I EXECUCIÓ
     * =====================================================
     */
    private void gestionarTorn() {
        String jugadorActual;
        if (tornBlanques) {
            jugadorActual = jugadorBlanques;
        } else {
            jugadorActual = jugadorNegres;
        }
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

    private void executarMoviment(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        if (Character.toLowerCase(tauler[filaDesti][columnaDesti]) == 'k') {
            System.out.println(VERD + "ESCAC I MAT! Partida guanyada." + RESET);
            jocActiu = false;
        }
        if (peca == 'P' && filaDesti == 0)
            peca = 'Q';
        if (peca == 'p' && filaDesti == 7)
            peca = 'q';
        tauler[filaDesti][columnaDesti] = peca;
        tauler[filaOrigen][columnaOrigen] = '.';
    }

    /*
     * =====================================================
     * MÒDUL: VALIDACIONS GEOMÈTRIQUES
     * =====================================================
     */
    private boolean validarMoviment(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        if (peca == '.') {
            System.out.println(ROIG + "Error: No hi ha cap peça en aquesta casella." + RESET);
            return false;
        }
        char tipusPeça = Character.toLowerCase(peca);
        boolean movimentCorrecte = false;
        switch (tipusPeça) {
            case 'r':
                movimentCorrecte = esMovimentTorre(filaOrigen, columnaOrigen, filaDesti, columnaDesti);
                break;
            case 'b':
                movimentCorrecte = esMovimentAlfil(filaOrigen, columnaOrigen, filaDesti, columnaDesti);
                break;
            case 'n':
                movimentCorrecte = esMovimentCavall(filaOrigen, columnaOrigen, filaDesti, columnaDesti);
                break;
            case 'q':
                movimentCorrecte = esMovimentReina(filaOrigen, columnaOrigen, filaDesti, columnaDesti);
                break;
            case 'k':
                movimentCorrecte = esMovimentRei(filaOrigen, columnaOrigen, filaDesti, columnaDesti);
                break;
            case 'p':
                movimentCorrecte = esMovimentPeo(filaOrigen, columnaOrigen, filaDesti, columnaDesti);
                break;
        }
        if (!movimentCorrecte)
            return false;
        if ((tornBlanques && !Character.isUpperCase(peca)) || (!tornBlanques && !Character.isLowerCase(peca))) {
            System.out.println(ROIG + "Error: Aquesta peça no és teva." + RESET);
            return false;
        }
        if (tauler[filaDesti][columnaDesti] != '.'
                && Character.isUpperCase(peca) == Character.isUpperCase(tauler[filaDesti][columnaDesti])) {
            System.out.println(ROIG + "Error: No pots capturar una peça aliada." + RESET);
            return false;
        }
        if (!validarCamiLliure(filaOrigen, columnaOrigen, filaDesti, columnaDesti)) {
            System.out.println(ROIG + "Error: Camí bloquejat per altres peces." + RESET);
            return false;
        }
        return true;
    }

    private boolean esMovimentTorre(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        if (filaOrigen == filaDesti || columnaOrigen == columnaDesti) {
            return true;
        }
        System.out.println(ROIG + "Error: La torre només es pot moure en línia recta (files o columnes)." + RESET);
        return false;
    }

    private boolean esMovimentAlfil(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = filaDesti - filaOrigen;
        if (distanciaFila < 0)
            distanciaFila *= -1;
        int distanciaColumna = columnaDesti - columnaOrigen;
        if (distanciaColumna < 0)
            distanciaColumna *= -1;
        if (distanciaFila == distanciaColumna && distanciaFila > 0) {
            return true;
        }
        System.out.println(ROIG + "Error: L'alfil només es pot moure en diagonal." + RESET);
        return false;
    }

    private boolean esMovimentCavall(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = filaDesti - filaOrigen;
        if (distanciaFila < 0)
            distanciaFila *= -1;
        int distanciaColumna = columnaDesti - columnaOrigen;
        if (distanciaColumna < 0)
            distanciaColumna *= -1;
        if (distanciaFila * distanciaColumna == 2) {
            return true;
        }
        System.out.println(ROIG + "Error: El cavall s'ha de moure en forma de 'L' (2x1 o 1x2)." + RESET);
        return false;
    }

    private boolean esMovimentReina(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = filaDesti - filaOrigen;
        if (distanciaFila < 0) {
            distanciaFila = -distanciaFila;
        }
        int distanciaColumna = columnaDesti - columnaOrigen;
        if (distanciaColumna < 0) {
            distanciaColumna = -distanciaColumna;
        }
        boolean comoTorre = (filaOrigen == filaDesti || columnaOrigen == columnaDesti);
        boolean comoAlfil = (distanciaFila == distanciaColumna && distanciaFila > 0);
        if (comoTorre || comoAlfil) {
            return true;
        }
        System.out.println(ROIG + "Error: La reina només es pot moure en línia recta o diagonal." + RESET);
        return false;
    }

    private boolean esMovimentRei(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = filaDesti - filaOrigen;
        if (distanciaFila < 0)
            distanciaFila *= -1;
        int distanciaColumna = columnaDesti - columnaOrigen;
        if (distanciaColumna < 0)
            distanciaColumna *= -1;
        if (distanciaFila <= 1 && distanciaColumna <= 1 && (distanciaFila + distanciaColumna > 0)) {
            return true;
        }
        System.out.println(ROIG + "Error: El rei només es pot moure una casella en qualsevol direcció." + RESET);
        return false;
    }

    private boolean esMovimentPeo(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila;
        if (tornBlanques) {
            distanciaFila = filaOrigen - filaDesti;
        } else {
            distanciaFila = filaDesti - filaOrigen;
        }
        int distanciaColumna = columnaDesti - columnaOrigen;
        if (distanciaColumna < 0)
            distanciaColumna *= -1;
        if (distanciaFila <= 0) {
            System.out.println(ROIG + "Error: El peó no pot retrocedir." + RESET);
            return false;
        }
        if (distanciaColumna == 0) {
            if (tauler[filaDesti][columnaDesti] != '.') {
                System.out.println(ROIG + "Error: El peó no pot avançar si la casella està ocupada." + RESET);
                return false;
            }
            if (distanciaFila == 1)
                return true;
            boolean filaInicial;
            if (tornBlanques) {
                filaInicial = (filaOrigen == 6);
            } else {
                filaInicial = (filaOrigen == 1);
            }
            if (distanciaFila == 2 && filaInicial)
                return true;
        } else if (distanciaColumna == 1 && distanciaFila == 1) {
            if (tauler[filaDesti][columnaDesti] != '.')
                return true;
            System.out.println(ROIG + "Error: El peó només es mou en diagonal per capturar una peça." + RESET);
            return false;
        }
        System.out.println(ROIG + "Error: Moviment de peó no permès." + RESET);
        return false;
    }

    /*
     * =====================================================
     * MÒDUL: CAMÍ LLIURE I AUXILIARS
     * =====================================================
     */
    private boolean validarCamiLliure(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        if (Character.toLowerCase(tauler[filaOrigen][columnaOrigen]) == 'n')
            return true;
        int direccioFila = 0;
        if (filaDesti > filaOrigen)
            direccioFila = 1;
        else if (filaDesti < filaOrigen)
            direccioFila = -1;
        int direccioColumna = 0;
        if (columnaDesti > columnaOrigen)
            direccioColumna = 1;
        else if (columnaDesti < columnaOrigen)
            direccioColumna = -1;
        int filaActual = filaOrigen + direccioFila;
        int columnaActual = columnaOrigen + direccioColumna;
        while (filaActual != filaDesti || columnaActual != columnaDesti) {
            if (tauler[filaActual][columnaActual] != '.')
                return false;
            filaActual += direccioFila;
            columnaActual += direccioColumna;
        }
        return true;
    }

    /*
     * =====================================================
     * MÒDUL: ENTRADA DE DADES
     * =====================================================
     */
    private int demanarFila(String missatge) {
        while (true) {
            System.out.print(missatge);
            String entrada = scanner.next().toUpperCase();
            if (entrada.length() == 1) {
                char lletra = entrada.charAt(0);
                if (lletra >= 'A' && lletra <= 'H') {
                    return lletra - 'A';
                }
            }
            System.out.println(ROIG + "Error: Lletra entre A i H." + RESET);
        }
    }

    private int demanarCoordenada(String missatge) {
        int num;
        while (true) {
            System.out.print(missatge);
            if (scanner.hasNextInt()) {
                num = scanner.nextInt();
                if (num >= 0 && num <= 7)
                    return num;
            } else {
                scanner.next();
            }
            System.out.println(ROIG + "Error: Número entre 0 i 7." + RESET);
        }
    }

    private void introduirNomsJugadors() {
        System.out.print("Jugador 1: ");
        jugadorBlanques = scanner.next();
        System.out.print("Jugador 2: ");
        jugadorNegres = scanner.next();
    }

    private void assignarColors() {
        if (Math.random() > 0.5) {
            String temp = jugadorBlanques;
            jugadorBlanques = jugadorNegres;
            jugadorNegres = temp;
        }
        System.out.println("Blanques: " + jugadorBlanques + " | Negres: " + jugadorNegres);
    }

    private void inicialitzarTauler() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                tauler[i][j] = '.';
        String peces = "rnbqkbnr";
        for (int i = 0; i < 8; i++) {
            tauler[0][i] = peces.charAt(i);
            tauler[1][i] = 'p';
            tauler[6][i] = 'P';
            tauler[7][i] = Character.toUpperCase(peces.charAt(i));
        }
    }

    private void mostrarTauler() {
        System.out.println("\n  0 1 2 3 4 5 6 7");
        for (int i = 0; i < 8; i++) {
            char lletraFila = (char) ('A' + i);
            System.out.print(lletraFila + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(tauler[i][j] + " ");
            }
            System.out.println();
        }
    }
}