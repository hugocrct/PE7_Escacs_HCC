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
    
    // Constants del tauler
    private final int midaTauler = 8;
    private final int filaPromocioBlanques = 0;
    private final int filaPromocioNegres = 7;
    private final int filaInicialPeoBlanc = 6;
    private final int filaInicialPeoNegre = 1;
    private final int filaPecesNegres = 0;
    private final int filaPeonsNegres = 1;
    private final int filaPeonsBlancs = 6;
    private final int filaPecesBlanques = 7;
    private final char primeraFila = 'A';
    private final char ultimaFila = 'H';
    private final int minCoordenada = 0;
    private final int maxCoordenada = 7;
    
    // Constants de peces
    private final char casellaBuida = '.';
    private final char rei = 'k';
    private final char cavall = 'n';
    private final char peoBlanc = 'P';
    private final char peoNegre = 'p';
    private final char reinaBlanca = 'Q';
    private final char reinaNegra = 'q';
    
    Scanner scanner = new Scanner(System.in);
    char[][] tauler = new char[midaTauler][midaTauler];
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
        if (Character.toLowerCase(tauler[filaDesti][columnaDesti]) == rei) {
            System.out.println(VERD + "ESCAC I MAT! Partida guanyada." + RESET);
            jocActiu = false;
        }
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
     * Crear el tauler, llegir/escriure caselles, imprimir-lo
     * =====================================================
     */
    private void inicialitzarTauler() {
        for (int fila = minCoordenada; fila < midaTauler; fila++)
            for (int columna = minCoordenada; columna < midaTauler; columna++)
                tauler[fila][columna] = casellaBuida;
        String ordreInicialPeces = "rnbqkbnr";
        for (int columna = minCoordenada; columna < midaTauler; columna++) {
            tauler[filaPecesNegres][columna] = ordreInicialPeces.charAt(columna);
            tauler[filaPeonsNegres][columna] = peoNegre;
            tauler[filaPeonsBlancs][columna] = peoBlanc;
            tauler[filaPecesBlanques][columna] = Character.toUpperCase(ordreInicialPeces.charAt(columna));
        }
    }

    private void mostrarTauler() {
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
     * MÒDUL 2: GESTIÓ DE PECES
     * Representació d'una peça (tipus + color) i utilitats
     * =====================================================
     */
    // Aquest mòdul actualment està integrat amb el sistema de char
    // Les funcions d'utilitat per detectar color i tipus estan
    // dins de validarMoviment i altres funcions de validació

    /*
     * =====================================================
     * MÒDUL 3: VALIDACIÓ DE MOVIMENTS
     * Validar si el moviment és vàlid i per què no ho és
     * =====================================================
     */
    private int calcularDistancia(int origen, int desti) {
        int distancia = desti - origen;
        if (distancia < 0) {
            distancia *= -1;
        }
        return distancia;
    }
    
    private boolean validarMoviment(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
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
        
        if (!validarCamiLliure(filaOrigen, columnaOrigen, filaDesti, columnaDesti)) {
            System.out.println(ROIG + "Error: Camí bloquejat per altres peces." + RESET);
            return false;
        }
        
        return true;
    }
    
    private boolean validarMovimentGeometric(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
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
        return movimentCorrecte;
    }
    
    private boolean pecaPertanyAlJugadorActual(char peca) {
        if (tornBlanques) {
            return Character.isUpperCase(peca);
        } else {
            return Character.isLowerCase(peca);
        }
    }
    
    private boolean capturaValida(char peca, int filaDesti, int columnaDesti) {
        char pecaDesti = tauler[filaDesti][columnaDesti];
        if (pecaDesti == casellaBuida) {
            return true;
        }
        return Character.isUpperCase(peca) != Character.isUpperCase(pecaDesti);
    }

    private boolean esMovimentTorre(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        if (filaOrigen == filaDesti || columnaOrigen == columnaDesti) {
            return true;
        }
        System.out.println(ROIG + "Error: La torre només es pot moure en línia recta (files o columnes)." + RESET);
        return false;
    }

    private boolean esMovimentAlfil(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        if (distanciaFila == distanciaColumna && distanciaFila > 0) {
            return true;
        }
        System.out.println(ROIG + "Error: L'alfil només es pot moure en diagonal." + RESET);
        return false;
    }

    private boolean esMovimentCavall(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        if (distanciaFila * distanciaColumna == 2) {
            return true;
        }
        System.out.println(ROIG + "Error: El cavall s'ha de moure en forma de 'L' (2x1 o 1x2)." + RESET);
        return false;
    }

    private boolean esMovimentReina(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        boolean comoTorre = (filaOrigen == filaDesti || columnaOrigen == columnaDesti);
        boolean comoAlfil = (distanciaFila == distanciaColumna && distanciaFila > 0);
        if (comoTorre || comoAlfil) {
            return true;
        }
        System.out.println(ROIG + "Error: La reina només es pot moure en línia recta o diagonal." + RESET);
        return false;
    }

    private boolean esMovimentRei(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
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
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        if (distanciaFila <= 0) {
            System.out.println(ROIG + "Error: El peó no pot retrocedir." + RESET);
            return false;
        }
        if (distanciaColumna == 0) {
            if (tauler[filaDesti][columnaDesti] != casellaBuida) {
                System.out.println(ROIG + "Error: El peó no pot avançar si la casella està ocupada." + RESET);
                return false;
            }
            if (distanciaFila == 1)
                return true;
            boolean filaInicial;
            if (tornBlanques) {
                filaInicial = (filaOrigen == filaInicialPeoBlanc);
            } else {
                filaInicial = (filaOrigen == filaInicialPeoNegre);
            }
            if (distanciaFila == 2 && filaInicial)
                return true;
        } else if (distanciaColumna == 1 && distanciaFila == 1) {
            if (tauler[filaDesti][columnaDesti] != casellaBuida)
                return true;
            System.out.println(ROIG + "Error: El peó només es mou en diagonal per capturar una peça." + RESET);
            return false;
        }
        System.out.println(ROIG + "Error: Moviment de peó no permès." + RESET);
        return false;
    }

    private boolean validarCamiLliure(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        if (Character.toLowerCase(tauler[filaOrigen][columnaOrigen]) == cavall)
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
            if (tauler[filaActual][columnaActual] != casellaBuida)
                return false;
            filaActual += direccioFila;
            columnaActual += direccioColumna;
        }
        return true;
    }

    /*
     * =====================================================
     * MÒDUL 4: ENTRADA/SORTIDA (UI CONSOLA)
     * Parsejar entrada, gestionar errors de format, mostrar missatges
     * =====================================================
     */
    private int demanarFila(String missatge) {
        while (true) {
            System.out.print(missatge);
            String lletraIntroduida = scanner.next().toUpperCase();
            if (lletraIntroduida.length() == 1) {
                char lletra = lletraIntroduida.charAt(0);
                if (lletra >= primeraFila && lletra <= ultimaFila) {
                    return lletra - primeraFila;
                }
            }
            System.out.println(ROIG + "Error: Lletra entre A i H." + RESET);
        }
    }

    private int demanarCoordenada(String missatge) {
        int coordenadaIntroduida;
        while (true) {
            System.out.print(missatge);
            if (scanner.hasNextInt()) {
                coordenadaIntroduida = scanner.nextInt();
                if (coordenadaIntroduida >= minCoordenada && coordenadaIntroduida <= maxCoordenada)
                    return coordenadaIntroduida;
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
            String jugadorTemporal = jugadorBlanques;
            jugadorBlanques = jugadorNegres;
            jugadorNegres = jugadorTemporal;
        }
        System.out.println("Blanques: " + jugadorBlanques + " | Negres: " + jugadorNegres);
    }
}