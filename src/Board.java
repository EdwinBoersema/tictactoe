import java.util.*;

public class Board {
    private final String[] BOARD_VALUES = {"A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3"};
    private final int WIDTH = 3;
    private ArrayList<Symbol> grid;

    // initialize a new board
    public Board() {
        initialiseBoard();
    }

    // handles the computer's move
    public int playCpu(int move,Symbol symbol, String name) {
        // printing out what move the computer makes
        System.out.println(name + " chooses: " + BOARD_VALUES[move]);
        updateBoard(move, symbol);
        // print out board
        displayBoard(true);
        // return move
        return move;
    }

    /*
     * converts the player's input to an Integer
     * and sets the player's symbol in the grid at the chosen position
     */
    public int playPlayer(Symbol symbol, String input) {
        // convert input to uppercase, then search for index of input
        input = input.toUpperCase();
        int position = Arrays.asList(BOARD_VALUES).indexOf(input);
        // update the board with player's chosen position
        updateBoard(position, symbol);
        // print out board
        displayBoard(true);
        // return move
        return position;
    }

    // prints out the board to the console
    public void displayBoard(boolean gameStarted) {
        for (int i = 0; i < 9; i++) {
            if (gameStarted) {
                // check if grid value is null before printint
                if (grid.get(i) == null) {
                    System.out.print("    ");
                } else {
                    System.out.print(" " + grid.get(i).value + " ");
                }
            } else {
                System.out.print(" " + BOARD_VALUES[i] + " ");
            }
            // add vertical lines
            if (i == 0 || i == 1 || i == 3 || i == 4 || i == 6 || i == 7) {
                System.out.print("|");
                // add horizontal lines
            } else if (i == 2 || i == 5) {
                System.out.println();
                System.out.println("---------------");
            } else {
                // new line after last board cell
                System.out.println();
                System.out.println();
            }
        }
    }

    // put player or computer move into the grid
    private void updateBoard(int position, Symbol symbol) {
        grid.set(position, symbol);
    }

    // checks player input vs list of valid options
    public boolean isValidEntry(String input) {
        // convert characters in input to uppercase
        input = input.toUpperCase();
        // check input against List of valid options
        return getValidChoices().contains(input);
    }

    // checks if grid does not contain "  "
    private boolean isFull() {
        return !grid.contains(null);
    }

    /*
     * returns an integer regarding the board's state
     * -1 = valid
     * 0 = player 1 has won
     * 1 = player 2 has won
     * 2 = draw
     */
    public int validateBoardState() {
        // check rows
        if (checkRows() != -1) {
            return checkRows();
        }
        // check columns
        if (checkColumns() != -1) {
            return checkColumns();
        }
        // check diagonals
        if (checkDiagonals() != -1) {
            return checkDiagonals();
        }
        // check if the board is full
        if (isFull()) {
            return 2;
        }
        // otherwise, return -1
        return -1;
    }

    // checks rows for the same values
    private int checkRows() {
        for (int i = 0; i <= 7; i += 3) {
            if (grid.get(i) != null) {
                // make a list of the row
                ArrayList<Symbol> row = new ArrayList<>();
                row.add(grid.get(i));
                row.add(grid.get(i+1));
                row.add(grid.get(i+2));
                // check if row all elements are the same
                if (Collections.frequency(row, row.get(1)) == WIDTH) {
                    // return integer depending on the symbol in the row
                    return row.get(1) == Symbol.X ? 0 : 1;
                }
            }
        }
        // return -1 if there's no matching row
        return -1;
    }

    // checks columns for the same values
    private int checkColumns() {
        for (int i = 0; i < 3; i++) {
            if (grid.get(i) != null) {
                // make a list of the column
                ArrayList<Symbol> column = new ArrayList<>();
                column.add(grid.get(i));
                column.add(grid.get(i+3));
                column.add(grid.get(i+6));
                // check if row all elements are the same
                if (Collections.frequency(column, column.get(1)) == WIDTH) {
                    // return integer depending on the symbol in the row
                    return column.get(1) == Symbol.X ? 0 : 1;
                }
            }
        }
        // return -1 if there's no matching column
        return -1;
    }

    // checks diagonals for the same values
    private int checkDiagonals() {
        if (grid.get(4) != null) {
            // top-left to bottom-right diagonal
            ArrayList<Symbol> diagonal1 = new ArrayList<>();
            diagonal1.add(grid.get(0));
            diagonal1.add(grid.get(4));
            diagonal1.add(grid.get(8));
            // top-right to bottom-left
            ArrayList<Symbol> diagonal2 = new ArrayList<>();
            diagonal2.add(grid.get(2));
            diagonal2.add(grid.get(4));
            diagonal2.add(grid.get(6));
            //check for same values
            if ((Collections.frequency(diagonal1, diagonal1.get(1)) == WIDTH)) {
                return diagonal1.get(1) == Symbol.X ? 0 : 1;
            } else if ((Collections.frequency(diagonal2, diagonal2.get(1)) == WIDTH)) {
                return diagonal2.get(1) == Symbol.X ? 0 : 1;
            }
        }
        // return -1 if there's no matching diagonal
        return -1;
    }

    // returns the remaining available choices
    public List<String> getValidChoices() {
        List<String> validChoices = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (grid.get(i) == null) {
                validChoices.add(BOARD_VALUES[i]);
            }
        }
        return validChoices;
    }

    // sets up a new list for tracking the options chosen
    private void initialiseBoard() {
        grid = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            grid.add(i, null);
        }
    }

    public ArrayList<Symbol> getGrid() {
        return grid;
    }
}