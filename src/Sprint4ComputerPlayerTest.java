import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;

public class Sprint4ComputerPlayerTest {
    private Sprint2UI gameUI;

    @Before
    public void setUp() {
        gameUI = new Sprint2UI();
        gameUI.blueSButton.setSelected(true);
        gameUI.redOButton.setSelected(true);
    }

    @Test
    public void testComputerPlayerMakesMove() {
        //setup game for computer play
        gameUI.blueComputerCheckbox.setSelected(true);

        gameUI.startGame();

        //simulate the start of the game which should trigger a computer move
        gameUI.takeTurn(new JButton());

        //check that a move was made
        boolean moveMade = false;
        for (Component comp : gameUI.boardPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (!button.getText().isEmpty()) {
                    moveMade = true;
                    break;
                }
            }
        }

        assertTrue("Computer player should have made a move", moveMade);
    }

}