import javax.swing.*;
import java.awt.*;
import java.util.Random;


//UI for SOS
public class Sprint2UI extends JFrame {
    //UI elements (had private, but switched them all to protected to allow JUnit testing)
    protected JComboBox<Integer> sizeComboBox;
    protected JRadioButton blueSButton;
    protected JRadioButton blueOButton;
    protected JRadioButton redSButton;
    protected JRadioButton redOButton;
    protected JLabel blueLabel;
    protected JLabel redLabel;
    protected JPanel boardPanel;
    protected JPanel mainPanel;
    protected JRadioButton simpleGameRadioButton;
    protected JRadioButton generalGameRadioButton;
    protected JCheckBox blueComputerCheckbox;
    protected JCheckBox redComputerCheckbox;

    protected JButton startButton;
    private Sprint2GameLogic game;
    private boolean isBlueTurn = true;
    private boolean isGameStarted = false;
    private String gameType;

    //constructor used to initialize UI
    public Sprint2UI() {
        //setting up the JFrame
        setTitle("SOS");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //panel to hold the other components
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //panel for game selection
        JPanel gameTypePanel = new JPanel();
        gameTypePanel.setLayout(new FlowLayout());
        ButtonGroup gameTypeGroup = new ButtonGroup();

        //radio buttons for game selection
        simpleGameRadioButton = new JRadioButton("Simple Game");
        simpleGameRadioButton.setSelected(true);
        generalGameRadioButton = new JRadioButton("General Game");

        //group for the radio buttons
        gameTypeGroup.add(simpleGameRadioButton);
        gameTypeGroup.add(generalGameRadioButton);

        //adding the buttons to the top panel
        simpleGameRadioButton.addActionListener(e -> preventGameTypeChange());
        generalGameRadioButton.addActionListener(e -> preventGameTypeChange());

        //adding the buttons to the top panel
        gameTypePanel.add(simpleGameRadioButton);
        gameTypePanel.add(generalGameRadioButton);
        mainPanel.add(gameTypePanel, BorderLayout.NORTH);

        //creating panel for player options
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new GridLayout(2, 2));

        //options for blue player
        JPanel bluePlayerPanel = new JPanel();
        bluePlayerPanel.setLayout(new BorderLayout());
        blueLabel = new JLabel("Blue Player (Your Turn)");
        bluePlayerPanel.add(blueLabel, BorderLayout.NORTH);

        //radio buttons for blue player
        blueSButton = new JRadioButton("S");
        blueOButton = new JRadioButton("O");
        ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueSButton);
        blueGroup.add(blueOButton);
        bluePlayerPanel.add(blueSButton, BorderLayout.WEST);
        bluePlayerPanel.add(blueOButton, BorderLayout.EAST);

        //options for red player
        JPanel redPlayerPanel = new JPanel();
        redPlayerPanel.setLayout(new BorderLayout());
        redLabel = new JLabel("Red Player");
        redPlayerPanel.add(redLabel, BorderLayout.NORTH);

        //radio buttons for blue player (pretty much identical to the blue player)
        redSButton = new JRadioButton("S");
        redOButton = new JRadioButton("O");
        ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redSButton);
        redGroup.add(redOButton);
        redPlayerPanel.add(redSButton, BorderLayout.WEST);
        redPlayerPanel.add(redOButton, BorderLayout.EAST);

        //checkboxes for comp player
        blueComputerCheckbox = new JCheckBox("Computer");
        redComputerCheckbox = new JCheckBox("Computer");

        //add checkboxes to the player panels
        bluePlayerPanel.add(blueComputerCheckbox, BorderLayout.SOUTH);
        redPlayerPanel.add(redComputerCheckbox, BorderLayout.SOUTH);

        //adding both players' panels to the player panel
        playerPanel.add(bluePlayerPanel);
        playerPanel.add(redPlayerPanel);

        //start game button
        startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(120, 30));
        startButton.addActionListener(e -> startGame());

        //start button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(startButton, BorderLayout.CENTER);

        //board size panel
        JPanel sizePanel = new JPanel();
        JLabel label = new JLabel("Select Board Size:");
        sizePanel.add(label);

        //updated combo box code to be a bit easier to change
        sizeComboBox = new JComboBox<>();
        for (int i = 3; i <= 8; i++) {
            sizeComboBox.addItem(i);
        }
        sizePanel.add(sizeComboBox);

        //adding all the sub panels to the main panel
        mainPanel.add(sizePanel, BorderLayout.EAST);
        mainPanel.add(playerPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        //once the main panel is put together, it is being added to the frame
        add(mainPanel);
        setVisible(true);

        //below here is things necessary to make lines over the grid
        BoardGlassPane glass = new BoardGlassPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //method will be used to draw lines
            }
        };

        glass.setOpaque(false); //makes the panel transparent
        setGlassPane(glass);
        glass.setVisible(true);

    }

    //method to start the game
    void startGame() {
        //clear any existing lines on the board
        BoardGlassPane glass = (BoardGlassPane) getGlassPane();
        glass.clearLines();

        //check if blue player has selected 'S' or 'O'
        if (!blueSButton.isSelected() && !blueOButton.isSelected()) {
            JOptionPane.showMessageDialog(this, "Blue Player must select 'S' or 'O'.");
            return;
        }

        //check if red player has selected 'S' or 'O'
        if (!redSButton.isSelected() && !redOButton.isSelected()) {
            JOptionPane.showMessageDialog(this, "Red Player must select 'S' or 'O'.");
            return;
        }

        //indicate that the game has started
        isGameStarted = true;

        //disabling certain controls if computer is selected
        if (blueComputerCheckbox.isSelected()) {
            blueSButton.setEnabled(false);
            blueOButton.setEnabled(false);
        } else {
            blueSButton.setEnabled(true);
            blueOButton.setEnabled(true);
        }
        if (redComputerCheckbox.isSelected()) {
            redSButton.setEnabled(false);
            redOButton.setEnabled(false);
        } else {
            redSButton.setEnabled(true);
            redOButton.setEnabled(true);
        }

        //whats the game type (Simple or General)
        gameType = simpleGameRadioButton.isSelected() ? "Simple" : "General";

        //get the selected board size
        int boardSize = (int) sizeComboBox.getSelectedItem();

        //get the symbols chosen by blue and red players
        String bluePlayerOption = blueSButton.isSelected() ? "S" : "O";
        String redPlayerOption = redSButton.isSelected() ? "S" : "O";


        //initialize the game logic
        game = new Sprint2GameLogic(boardSize, gameType, bluePlayerOption, redPlayerOption);

        //disable game mode selection
        simpleGameRadioButton.setEnabled(false);
        generalGameRadioButton.setEnabled(false);

        //shows the game board
        displayBoard(boardSize);

        //updates player labels at the start of a game
        updatePlayerLabels();
    }



    //method to display the game board
    private void displayBoard(int size) {
        //if there is already a board panel, remove it from the main panel
        if (boardPanel != null) {
            mainPanel.remove(boardPanel);
        }

        //create a new board panel
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(size, size));

        //create buttons for each square on the board
        for (int i = 0; i < size * size; i++) {
            JButton button = new JButton();
            //add action listener to handle button clicks
            button.addActionListener(e -> takeTurn(button));
            boardPanel.add(button);
        }

        //set the preferred size of the board panel
        int boardSize = Math.min(300, getSize().height - 100);
        boardPanel.setPreferredSize(new Dimension(boardSize, boardSize));

        //update the bounds of the glass pane to match the board panel
        BoardGlassPane glass = (BoardGlassPane) getGlassPane();
        glass.updateBounds(boardPanel);

        //add the board panel to the main panel and update the UI
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }


    protected void takeTurn(JButton button) {
        //check if the game is started and a turn can be taken
        if (game == null || !isGameStarted) {
            JOptionPane.showMessageDialog(this, "Please start the game first.");
            return;
        }
        if (isComputerTurn()) {
            makeComputerMove();
            return;
        }

        //get the row and column of the button clicked
        int row = boardPanel.getComponentZOrder(button) / game.getBoardSize();
        int col = boardPanel.getComponentZOrder(button) % game.getBoardSize();

        //determine the symbol based on the current player's turn and selection
        String symbol = isBlueTurn ? (blueSButton.isSelected() ? "S" : "O") : (redSButton.isSelected() ? "S" : "O");

        //check if the button is already occupied
        if (!button.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "This spot is taken. Find a new place.");
            return;
        }

        //set the symbol on the button
        button.setText(symbol);

        //check if an SOS sequence is formed
        boolean sosFormed = game.checkForSOS(row, col, symbol, isBlueTurn);

        //draw a line if an SOS sequence is formed
        if (sosFormed) {
            drawLineForSOS(row, col, symbol, isBlueTurn);
        }


        //check for game over in Simple game
        if (sosFormed && gameType.equals("Simple")) {
            JOptionPane.showMessageDialog(this, isBlueTurn ? "Blue Player wins!" : "Red Player wins!");
            resetGame();
            return;
        }

        //update turn based on game mode and game progress
        if (!sosFormed || gameType.equals("General")) {
            isBlueTurn = !isBlueTurn;
        }

        //check for winner or draw in General game
        if (game.isBoardFull() && gameType.equals("General")) {
            determineWinnerOrDraw();
        }
        //checking to see if the board filled
        if (game.isBoardFull()) {
            if (gameType.equals("Simple") || gameType.equals("General")) {
                if (game.getSosCountBlue() == game.getSosCountRed()) {
                    JOptionPane.showMessageDialog(this, "It's a draw!");
                    resetGame();
                }
            }
        }



        updatePlayerLabels();
    }

    private void drawLineForSOS(int row, int col, String symbol, boolean isBlueTurn) {
        //get the glass pane to draw the line
        BoardGlassPane glass = (BoardGlassPane) getGlassPane();
        int cellSize = boardPanel.getWidth() / game.getBoardSize();

        //calculate line start and end coordinates based on SOS direction
        int startX = col * cellSize;
        int startY = row * cellSize;
        int endX = startX;
        int endY = startY;

        //adjust line coordinates for different SOS directions
        //horizontal
        if (symbol.equals("S")) {
            //check left and right for SOS sequence
            if (col > 1 && "O".equals(game.getSymbolAt(row, col - 1)) && "S".equals(game.getSymbolAt(row, col - 2))) {
                startX = (col - 2) * cellSize;
                endX = (col + 1) * cellSize;
            } else if (col < game.getBoardSize() - 2 && "O".equals(game.getSymbolAt(row, col + 1)) && "S".equals(game.getSymbolAt(row, col + 2))) {
                startX = col * cellSize;
                endX = (col + 3) * cellSize;
            }

            //vertical
            if (row > 1 && "O".equals(game.getSymbolAt(row - 1, col)) && "S".equals(game.getSymbolAt(row - 2, col))) {
                startY = (row - 2) * cellSize;
                endY = (row + 1) * cellSize;
                endX = startX;
            } else if (row < game.getBoardSize() - 2 && "O".equals(game.getSymbolAt(row + 1, col)) && "S".equals(game.getSymbolAt(row + 2, col))) {
                startY = row * cellSize;
                endY = (row + 3) * cellSize;
                endX = startX;
            }

            //diagonal
            if (row > 1 && col > 1 && "O".equals(game.getSymbolAt(row - 1, col - 1)) && "S".equals(game.getSymbolAt(row - 2, col - 2))) {
                startX = (col - 2) * cellSize;
                startY = (row - 2) * cellSize;
                endX = (col + 1) * cellSize;
                endY = (row + 1) * cellSize;
            } else if (row < game.getBoardSize() - 2 && col < game.getBoardSize() - 2 && "O".equals(game.getSymbolAt(row + 1, col + 1)) && "S".equals(game.getSymbolAt(row + 2, col + 2))) {
                startX = col * cellSize;
                startY = row * cellSize;
                endX = (col + 3) * cellSize;
                endY = (row + 3) * cellSize;
            }

            if (row < game.getBoardSize() - 2 && col > 1 && "O".equals(game.getSymbolAt(row + 1, col - 1)) && "S".equals(game.getSymbolAt(row + 2, col - 2))) {
                startX = (col - 2) * cellSize;
                startY = (row + 2) * cellSize;
                endX = (col + 1) * cellSize;
                endY = (row - 1) * cellSize;
            } else if (row > 1 && col < game.getBoardSize() - 2 && "O".equals(game.getSymbolAt(row - 1, col + 1)) && "S".equals(game.getSymbolAt(row - 2, col + 2))) {
                startX = col * cellSize;
                startY = (row - 2) * cellSize;
                endX = (col + 3) * cellSize;
                endY = (row + 1) * cellSize;
            }
        }

        //center line
        int x1 = startX + cellSize / 2;
        int y1 = startY + cellSize / 2;
        int x2 = endX + cellSize / 2;
        int y2 = endY + cellSize / 2;

        //adjust position of the line if necessary (still working on this a bit)
        int offsetX = boardPanel.getLocationOnScreen().x - getGlassPane().getLocationOnScreen().x;
        x1 += offsetX;
        x2 += offsetX;

        x1 -= 25;  //shift pixels left
        x2 -= 25;  //shift pixels left
        y1 += 30;  //shift pixels down
        y2 += 30;  //shift pixels down

        //determine line color based on player turn
        Color lineColor = isBlueTurn ? Color.BLUE : Color.RED;

        //add the line to the glass pane
        glass.addLine(x1, y1, x2, y2, lineColor);
    }





    public void updateBounds(JPanel boardPanel) {
        //get bounds of the board panel
        Rectangle boardBounds = boardPanel.getBounds();

        //convert location of the board panel on the glass pane
        Point locationOnGlass = SwingUtilities.convertPoint(boardPanel.getParent(), boardBounds.getLocation(), this);

        //set the bounds of the glass pane to match the board panel
        setBounds(locationOnGlass.x, locationOnGlass.y, boardBounds.width, boardBounds.height);
    }

    private void determineWinnerOrDraw() {
        //get the SOS counts for blue and red players
        int sosBlue = game.getSosCountBlue();
        int sosRed = game.getSosCountRed();

        //check and display the winner or draw
        if (sosBlue > sosRed) {
            JOptionPane.showMessageDialog(this, "Blue Player wins!");
        } else if (sosRed > sosBlue) {
            JOptionPane.showMessageDialog(this, "Red Player wins!");
        } else {
            JOptionPane.showMessageDialog(this, "It's a draw!");
        }


        //reset game state
        resetGame();
    }

    private void resetGame() {
        //reset the text on all buttons in the board panel
        for (Component comp : boardPanel.getComponents()) {
            if (comp instanceof JButton) {
                ((JButton) comp).setText("");
            }
        }

        //clear lines on the glass pane and reset game variables
        BoardGlassPane glass = (BoardGlassPane) getGlassPane();
        glass.clearLines();
        game = null;
        isGameStarted = false;

        //enable game type selection again
        simpleGameRadioButton.setEnabled(true);
        generalGameRadioButton.setEnabled(true);

        //enable player controls again
        blueComputerCheckbox.setEnabled(true);
        redComputerCheckbox.setEnabled(true);
        blueSButton.setEnabled(true);
        blueOButton.setEnabled(true);
        redSButton.setEnabled(true);
        redOButton.setEnabled(true);
    }

    //method which changes the player labels to display whose turn it is
    private void updatePlayerLabels() {
        //update player labels based on the current turn
        if (isBlueTurn) {
            blueLabel.setText("Blue Player (Your Turn)");
            redLabel.setText("Red Player");
        } else {
            redLabel.setText("Red Player (Your Turn)");
            blueLabel.setText("Blue Player");
        }
    }

    //method to prevent changing game type during an ongoing game
    private void preventGameTypeChange() {
        if (isGameStarted) {
            //display a message indicating game type cannot be changed
            JOptionPane.showMessageDialog(null, "You cannot change the game type during an ongoing game.");

            //restore the selected game type radio button based on the current game type
            simpleGameRadioButton.setSelected(gameType.equals("Simple"));
            generalGameRadioButton.setSelected(gameType.equals("General"));
        }
    }

    //checking to see if it is a computer turn
    private boolean isComputerTurn() {
        return (isBlueTurn && blueComputerCheckbox.isSelected()) ||
                (!isBlueTurn && redComputerCheckbox.isSelected());
    }

    //used for a computer player to make a move
    private void makeComputerMove() {
        Random random = new Random();
        int boardSize = game.getBoardSize();
        boolean moveMade = false;

        while (!moveMade) {
            int row = random.nextInt(boardSize);
            int col = random.nextInt(boardSize);
            JButton button = (JButton) boardPanel.getComponent(row * boardSize + col);
            if (button.getText().isEmpty()) {
                String symbol = random.nextBoolean() ? "S" : "O"; //randomly choose S or O
                game.placeMove(row, col, symbol, isBlueTurn);
                button.setText(symbol);
                drawLineForSOS(row, col, symbol, isBlueTurn);
                moveMade = true;

                //check for game end conditions
                if (gameType.equals("Simple") && game.getSosCountBlue() + game.getSosCountRed() > 0) {
                    determineWinnerOrDraw();
                } else if (game.isBoardFull()) {
                    determineWinnerOrDraw();
                } else {
                    isBlueTurn = !isBlueTurn; //change turn if the game hasn't ended
                }
            }
        }
        updatePlayerLabels();
    }

    //method to get game logic (used for junit testing)
    public Sprint2GameLogic getGameLogic() {
        return game;
    }


    //method which starts the UI
    public static void main(String[] args) {SwingUtilities.invokeLater(Sprint2UI::new);}

}