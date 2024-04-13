//game logic for SOS game
public class Sprint2GameLogic {
    private int boardSize;
    private String gameType;
    private String[][] board;
    private int sosCountBlue;
    private int sosCountRed;

    //constructor to initialize the game logic
    public Sprint2GameLogic(int boardSize, String gameType, String bluePlayerOption, String redPlayerOption) {
        this.boardSize = boardSize;
        this.gameType = gameType;
        this.board = new String[boardSize][boardSize];
        this.sosCountBlue = 0;
        this.sosCountRed = 0;
    }
    //method to check for SOS formation at a specific position on the board
    public boolean checkForSOS(int row, int col, String symbol, boolean isBlueTurn) {
        //place the symbol on the board as part of checking for SOS
        board[row][col] = symbol;

        int count = 0;
        if ("S".equals(symbol)) {
            count += checkHorizontalSOS(row, col, isBlueTurn);
            count += checkVerticalSOS(row, col, isBlueTurn);
            count += checkDiagonalSOS(row, col, isBlueTurn);
        } else if ("O".equals(symbol)) {
            count += checkForO(row, col, isBlueTurn);
        }

        //opdate SOS count based on the player's turn
        if (isBlueTurn) {
            sosCountBlue += count;
        } else {
            sosCountRed += count;
        }

        return count > 0;
    }

    //method to check for SOS formations horizontally on the board
    private int checkHorizontalSOS(int row, int col, boolean isBlueTurn) {
        int count = 0;
        if (col > 1 && "S".equals(board[row][col - 2]) && "O".equals(board[row][col - 1])) {
            count++;
        }
        if (col < boardSize - 2 && "S".equals(board[row][col + 2]) && "O".equals(board[row][col + 1])) {
            count++;
        }
        return count;
    }

    //method to check for SOS formations vertically on the board
    private int checkVerticalSOS(int row, int col, boolean isBlueTurn) {
        int count = 0;
        if (row > 1 && "S".equals(board[row - 2][col]) && "O".equals(board[row - 1][col])) {
            count++;
        }
        if (row < boardSize - 2 && "S".equals(board[row + 2][col]) && "O".equals(board[row + 1][col])) {
            count++;
        }
        return count;
    }

    //method to check for SOS formations diagonally on the board
    private int checkDiagonalSOS(int row, int col, boolean isBlueTurn) {
        int count = 0;
        if (row > 1 && col > 1 && "S".equals(board[row - 2][col - 2]) && "O".equals(board[row - 1][col - 1])) {
            count++;
        }
        if (row < boardSize - 2 && col < boardSize - 2 && "S".equals(board[row + 2][col + 2]) && "O".equals(board[row + 1][col + 1])) {
            count++;
        }
        if (row > 1 && col < boardSize - 2 && "S".equals(board[row - 2][col + 2]) && "O".equals(board[row - 1][col + 1])) {
            count++;
        }
        if (row < boardSize - 2 && col > 1 && "S".equals(board[row + 2][col - 2]) && "O".equals(board[row + 1][col - 1])) {
            count++;
        }
        return count;
    }

    //method to check for 'O' formations on the board
    private int checkForO(int row, int col, boolean isBlueTurn) {
        int count = 0;
        // Check if 'O' is in the middle of 'S' horizontally or vertically
        if (row > 0 && row < boardSize - 1 && "S".equals(board[row - 1][col]) && "S".equals(board[row + 1][col])) {
            count++;
        }
        if (col > 0 && col < boardSize - 1 && "S".equals(board[row][col - 1]) && "S".equals(board[row][col + 1])) {
            count++;
        }
        // Diagonally
        if (row > 0 && col > 0 && row < boardSize - 1 && col < boardSize - 1 && "S".equals(board[row - 1][col - 1]) && "S".equals(board[row + 1][col + 1])) {
            count++;
        }
        if (row > 0 && col < boardSize - 1 && row < boardSize - 1 && col > 0 && "S".equals(board[row - 1][col + 1]) && "S".equals(board[row + 1][col - 1])) {
            count++;
        }
        return count;
    }

    //method to get the board size
    public int getBoardSize() {
        return boardSize;
    }

    //method to check if the board is full
    public boolean isBoardFull() {
        for (String[] row : board) {
            for (String cell : row) {
                if (cell == null) {
                    return false;
                }
            }
        }
        return true;
    }

    //method to get the symbol at a specific position on the board
    public String getSymbolAt(int row, int col) {
        if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            return board[row][col];
        }
        return null;
    }

    //used in the case of a comp player
    public boolean placeMove(int row, int col, String symbol, boolean isBlue) {
        //check if the cell is empty
        if (board[row][col] == null || board[row][col].isEmpty()) {
            return checkForSOS(row, col, symbol, isBlue);
        }
        return false;
    }




    //method to get the SOS count for the blue player
    public int getSosCountBlue() {
        return sosCountBlue;
    }

    //method to get the SOS count for the red player
    public int getSosCountRed() {
        return sosCountRed;
    }

}