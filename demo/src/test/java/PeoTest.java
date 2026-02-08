package Practiques.PE07_Escacs.demo.src.test.java;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Practiques.PE07_Escacs.demo.PE07HugoCarrasco;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per validar el comportament del peó en el joc d'escacs.
 * Cobreix moviments bàsics, captures i casos invàlids.
 */
@DisplayName("Proves de Peó")
class PeoTest {
    
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
    @DisplayName("Test 1: Peó blanc avança 1 casella endavant - Vàlid")
    void testPeoBlanc_Avanca1Casella_CasellaLliure() {
        // Arrange: Col·locar peó blanc a la posició (6, 3)
        joc.tauler[6][3] = 'P';
        
        // Act: Intentar moure 1 casella endavant
        boolean resultat = joc.validarMoviment('P', 6, 3, 5, 3);
        
        // Assert: El moviment ha de ser vàlid
        assertTrue(resultat, "El peó blanc hauria de poder avançar 1 casella si està buida");
    }
    
    @Test
    @DisplayName("Test 2: Peó blanc avança 2 caselles des de posició inicial - Vàlid")
    void testPeoBlanc_Avanca2Caselles_PosicioInicial() {
        // Arrange: Col·locar peó blanc a la seva fila inicial (6)
        joc.tauler[6][4] = 'P';
        
        // Act: Intentar moure 2 caselles endavant des de la posició inicial
        boolean resultat = joc.validarMoviment('P', 6, 4, 4, 4);
        
        // Assert: El moviment ha de ser vàlid
        assertTrue(resultat, "El peó blanc hauria de poder avançar 2 caselles des de la posició inicial");
    }
    
    @Test
    @DisplayName("Test 3: Peó blanc intenta avançar amb peça al davant - Invàlid")
    void testPeoBlanc_AvancaBloqueiat_PecaDavant() {
        // Arrange: Col·locar peó blanc i una peça negra just davant
        joc.tauler[5][2] = 'P';
        joc.tauler[4][2] = 'p'; // Peó negre bloquejant
        
        // Act: Intentar avançar amb obstacle davant
        boolean resultat = joc.validarMoviment('P', 5, 2, 4, 2);
        
        // Assert: El moviment ha de ser invàlid
        assertFalse(resultat, "El peó blanc no hauria de poder avançar si hi ha una peça davant");
    }
    
    @Test
    @DisplayName("Test 4: Peó blanc captura en diagonal peça rival - Vàlid")
    void testPeoBlanc_CapturaDiagonal_PecaRival() {
        // Arrange: Col·locar peó blanc i peça negra en diagonal
        joc.tauler[5][3] = 'P';
        joc.tauler[4][4] = 'n'; // Cavall negre a capturar
        
        // Act: Intentar captura diagonal
        boolean resultat = joc.validarMoviment('P', 5, 3, 4, 4);
        
        // Assert: El moviment ha de ser vàlid
        assertTrue(resultat, "El peó blanc hauria de poder capturar en diagonal una peça rival");
    }
    
    @Test
    @DisplayName("Test 5: Peó blanc intenta captura diagonal sense peça rival - Invàlid")
    void testPeoBlanc_CapturaDiagonal_SensePeca() {
        // Arrange: Col·locar peó blanc sense cap peça en diagonal
        joc.tauler[5][3] = 'P';
        joc.tauler[4][4] = '.'; // Casella buida
        
        // Act: Intentar moure's en diagonal a casella buida
        boolean resultat = joc.validarMoviment('P', 5, 3, 4, 4);
        
        // Assert: El moviment ha de ser invàlid
        assertFalse(resultat, "El peó blanc no hauria de poder moure's en diagonal si no hi ha peça a capturar");
    }
    
    @Test
    @DisplayName("Test 6: Peó blanc intenta moure enrere - Invàlid")
    void testPeoBlanc_MoureEnrere_Invalida() {
        // Arrange: Col·locar peó blanc
        joc.tauler[4][5] = 'P';
        
        // Act: Intentar retrocedir
        boolean resultat = joc.validarMoviment('P', 4, 5, 5, 5);
        
        // Assert: El moviment ha de ser invàlid
        assertFalse(resultat, "El peó blanc no hauria de poder retrocedir");
    }
    
    @Test
    @DisplayName("Test Extra: Peó negre avança correctament en direcció oposada")
    void testPeoNegre_AvancaCorrectament() {
        // Arrange: Col·locar peó negre a la seva fila inicial
        joc.tauler[1][3] = 'p';
        joc.tornBlanques = false; // Torn de negres
        
        // Act: Intentar moure 2 caselles endavant (cap avall en el tauler)
        boolean resultat = joc.validarMoviment('p', 1, 3, 3, 3);
        
        // Assert: El moviment ha de ser vàlid
        assertTrue(resultat, "El peó negre hauria de poder avançar 2 caselles des de la posició inicial");
    }
}
