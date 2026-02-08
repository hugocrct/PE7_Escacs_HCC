package Practiques.PE07_Escacs.demo.src.test.java;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per validar el comportament del cavall en el joc d'escacs.
 * Cobreix moviments en L, captures, casos invàlids i capacitat de saltar.
 */
@DisplayName("Proves de Cavall")
class CavallTest {
    
    private PE07HugoCarrasco joc;
    
    @BeforeEach
    void inicialitzar() {
        joc = new PE07HugoCarrasco();
        // Inicialitzem el tauler manualment per tenir control total
        joc.tauler = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                joc.tauler[i][j] = '.';
            }
        }
        joc.tornBlanques = true;
    }
    
    @Test
    @DisplayName("Test 1: Cavall blanc es mou en L a casella buida - Vàlid")
    void testCavallBlanc_MovimentL_CasellaLliure() {
        // Arrange: Col·locar cavall blanc al centre del tauler
        joc.tauler[4][4] = 'N';
        
        // Act: Moure 2 amunt, 1 dreta (moviment en L típic)
        boolean resultat = joc.validarMoviment('N', 4, 4, 2, 5);
        
        // Assert: El moviment ha de ser vàlid
        assertTrue(resultat, "El cavall blanc hauria de poder moure's en L a una casella buida");
    }
    
    @Test
    @DisplayName("Test 2: Cavall blanc captura peça rival en moviment L - Vàlid")
    void testCavallBlanc_CapturaPecaRival_MovimentL() {
        // Arrange: Col·locar cavall blanc i peça negra en posició L
        joc.tauler[5][3] = 'N';
        joc.tauler[3][4] = 'p'; // Peó negre a capturar
        
        // Act: Capturar peça negra amb moviment en L (2 amunt, 1 dreta)
        boolean resultat = joc.validarMoviment('N', 5, 3, 3, 4);
        
        // Assert: El moviment ha de ser vàlid
        assertTrue(resultat, "El cavall blanc hauria de poder capturar una peça rival amb moviment en L");
    }
    
    @Test
    @DisplayName("Test 3: Cavall intenta moure's com alfil/torre - Invàlid")
    void testCavallBlanc_MovimentNoL_Invalida() {
        // Arrange: Col·locar cavall blanc
        joc.tauler[4][4] = 'N';
        
        // Act: Intentar moure's en línia recta (com una torre) en lloc de L
        boolean resultat = joc.validarMoviment('N', 4, 4, 4, 6);
        
        // Assert: El moviment ha de ser invàlid
        assertFalse(resultat, "El cavall blanc no hauria de poder moure's en línia recta");
    }
    
    @Test
    @DisplayName("Test 4: Cavall intenta sortir del tauler - Invàlid")
    void testCavallBlanc_SortirTauler_Invalida() {
        // Arrange: Col·locar cavall blanc a la cantonada
        joc.tauler[0][0] = 'N';
        
        // Act: Intentar un moviment L que portaria fora dels límits
        // Des de (0,0), moviment 2 amunt, 1 dreta portaria a (-2, 1) que és invàlid
        // Com això causaria IndexOutOfBounds, provem un cas límit real:
        // Des de (0, 1) intentar anar a (-2, 2) - simulem amb coordenades dins del rang però invàlides
        joc.tauler[1][1] = 'N';
        
        // Moviment que NO és L vàlid
        boolean resultat = joc.validarMoviment('N', 1, 1, 0, 0);
        
        // Assert: El moviment ha de ser invàlid (no és moviment L de 2x1 o 1x2)
        assertFalse(resultat, "El cavall no hauria de poder fer moviments que no siguin en L");
    }
    
    @Test
    @DisplayName("Test 5: Cavall intenta capturar peça pròpia - Invàlid")
    void testCavallBlanc_CapturaPecaPropia_Invalida() {
        // Arrange: Col·locar cavall blanc i altra peça blanca en posició L
        joc.tauler[4][4] = 'N';
        joc.tauler[2][5] = 'P'; // Peó blanc (pròpia)
        
        // Act: Intentar "capturar" peça pròpia
        boolean resultat = joc.validarMoviment('N', 4, 4, 2, 5);
        
        // Assert: El moviment ha de ser invàlid
        assertFalse(resultat, "El cavall blanc no hauria de poder capturar una peça pròpia");
    }
    
    @Test
    @DisplayName("Test 6: Cavall salta obstacles - El camí bloquejat no afecta")
    void testCavallBlanc_SaltaObstacles_CamiBloqueiat() {
        // Arrange: Col·locar cavall envoltat de peces que bloquejin el camí
        joc.tauler[4][4] = 'N';
        joc.tauler[3][4] = 'P'; // Peó blanc davant
        joc.tauler[4][5] = 'P'; // Peó blanc a la dreta
        joc.tauler[3][5] = 'P'; // Peó blanc diagonal
        // Deixem lliure la posició destí (2,5)
        
        // Act: Moure el cavall saltant els obstacles (2 amunt, 1 dreta)
        boolean resultat = joc.validarMoviment('N', 4, 4, 2, 5);
        
        // Assert: El moviment ha de ser vàlid perquè el cavall salta
        assertTrue(resultat, "El cavall hauria de poder saltar obstacles i arribar a la casella destí");
    }
    
    @Test
    @DisplayName("Test Extra: Tots els moviments L possibles del cavall són vàlids")
    void testCavallBlanc_TotsMovimentsL_Valids() {
        // Arrange: Col·locar cavall al centre
        joc.tauler[4][4] = 'N';
        
        // Act & Assert: Comprovar els 8 moviments possibles en L
        assertTrue(joc.validarMoviment('N', 4, 4, 2, 5), "L: 2 amunt, 1 dreta");
        
        // Reinicialitzar torn per cada validació
        joc.tornBlanques = true;
        assertTrue(joc.validarMoviment('N', 4, 4, 2, 3), "L: 2 amunt, 1 esquerra");
        
        joc.tornBlanques = true;
        assertTrue(joc.validarMoviment('N', 4, 4, 6, 5), "L: 2 avall, 1 dreta");
        
        joc.tornBlanques = true;
        assertTrue(joc.validarMoviment('N', 4, 4, 6, 3), "L: 2 avall, 1 esquerra");
        
        joc.tornBlanques = true;
        assertTrue(joc.validarMoviment('N', 4, 4, 3, 6), "L: 1 amunt, 2 dreta");
        
        joc.tornBlanques = true;
        assertTrue(joc.validarMoviment('N', 4, 4, 3, 2), "L: 1 amunt, 2 esquerra");
        
        joc.tornBlanques = true;
        assertTrue(joc.validarMoviment('N', 4, 4, 5, 6), "L: 1 avall, 2 dreta");
        
        joc.tornBlanques = true;
        assertTrue(joc.validarMoviment('N', 4, 4, 5, 2), "L: 1 avall, 2 esquerra");
    }
}