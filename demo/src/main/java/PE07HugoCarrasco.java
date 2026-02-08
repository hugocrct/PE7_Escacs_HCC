package Practiques.PE07_Escacs.demo.src.main.java;
import java.util.Scanner;

/**
 * Versió per testing del joc d'escacs PE07.
 * Atributs i mètodes necessaris són públics per permetre proves unitàries.
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

    public void gestionarTorn() {
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

    public void executarMoviment(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
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
     * =====================================================
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
            tauler[filaPecesBlanques][columna] = Character.toUpperCase(ordreInicialPeces.charAt(columna));
        }
    }

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
    public int calcularDistancia(int origen, int desti) {
        int distancia = desti - origen;
        if (distancia < 0) {
            distancia *= -1;
        }
        return distancia;
    }
    
    public boolean validarMoviment(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        if (peca == casellaBuida) {
            return false;
        }
        
        if (!validarMovimentGeometric(peca, filaOrigen, columnaOrigen, filaDesti, columnaDesti))
            return false;
            
        if (!pecaPertanyAlJugadorActual(peca)) {
            return false;
        }
        
        if (!capturaValida(peca, filaDesti, columnaDesti)) {
            return false;
        }
        
        if (!validarCamiLliure(filaOrigen, columnaOrigen, filaDesti, columnaDesti)) {
            return false;
        }
        
        return true;
    }
    
    public boolean validarMovimentGeometric(char peca, int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
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
    
    public boolean pecaPertanyAlJugadorActual(char peca) {
        if (tornBlanques) {
            return Character.isUpperCase(peca);
        } else {
            return Character.isLowerCase(peca);
        }
    }
    
    public boolean capturaValida(char peca, int filaDesti, int columnaDesti) {
        char pecaDesti = tauler[filaDesti][columnaDesti];
        if (pecaDesti == casellaBuida) {
            return true;
        }
        return Character.isUpperCase(peca) != Character.isUpperCase(pecaDesti);
    }

    public boolean esMovimentTorre(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        return (filaOrigen == filaDesti || columnaOrigen == columnaDesti);
    }

    public boolean esMovimentAlfil(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        return (distanciaFila == distanciaColumna && distanciaFila > 0);
    }

    public boolean esMovimentCavall(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        return (distanciaFila * distanciaColumna == 2);
    }

    public boolean esMovimentReina(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        boolean comoTorre = (filaOrigen == filaDesti || columnaOrigen == columnaDesti);
        boolean comoAlfil = (distanciaFila == distanciaColumna && distanciaFila > 0);
        return (comoTorre || comoAlfil);
    }

    public boolean esMovimentRei(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila = calcularDistancia(filaOrigen, filaDesti);
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        return (distanciaFila <= 1 && distanciaColumna <= 1 && (distanciaFila + distanciaColumna > 0));
    }

    public boolean esMovimentPeo(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
        int distanciaFila;
        if (tornBlanques) {
            distanciaFila = filaOrigen - filaDesti;
        } else {
            distanciaFila = filaDesti - filaOrigen;
        }
        int distanciaColumna = calcularDistancia(columnaOrigen, columnaDesti);
        if (distanciaFila <= 0) {
            return false;
        }
        if (distanciaColumna == 0) {
            if (tauler[filaDesti][columnaDesti] != casellaBuida) {
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
            return false;
        }
        return false;
    }

    public boolean validarCamiLliure(int filaOrigen, int columnaOrigen, int filaDesti, int columnaDesti) {
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
     * MÒDUL 4: ENTRADA/SORTIDA
     * =====================================================
     */
    public int demanarFila(String missatge) {
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

    public int demanarCoordenada(String missatge) {
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

    public void introduirNomsJugadors() {
        System.out.print("Jugador 1: ");
        jugadorBlanques = scanner.next();
        System.out.print("Jugador 2: ");
        jugadorNegres = scanner.next();
    }

    public void assignarColors() {
        if (Math.random() > 0.5) {
            String jugadorTemporal = jugadorBlanques;
            jugadorBlanques = jugadorNegres;
            jugadorNegres = jugadorTemporal;
        }
        System.out.println("Blanques: " + jugadorBlanques + " | Negres: " + jugadorNegres);
    }
}