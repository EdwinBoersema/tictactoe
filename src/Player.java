import java.util.*;

public class Player {
    private final String name;
    private final Symbol symbol;
    private boolean computer = false;

    // human player constructor
    public Player(Symbol symbol, String name) {
        this.name = name;
        this.symbol = symbol;
    }

    // computer player constructor
    public Player(Symbol symbol, String name, boolean computer) {
        this.name = name;
        this.symbol = symbol;
        this.computer = computer;
    }

    // computer logic

    // determines the move that the computer should take
    public int determineCpuMove(ArrayList<Symbol> grid, int lastMove) {
        // set symbol to check against
        Symbol opponentSymbol = this.symbol == Symbol.O ? Symbol.X : Symbol.O;
        // check if middle square is available
        if (grid.get(4) == null) {
            return 4;
        }
        int index;
        // get empty cells, loop through empty cells and check their rows/columns and diagonals
        ArrayList<Integer> testCells = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (grid.get(i) == null) {
                testCells.add(i);
            }
        }
        // check rows/columns/diagonals for 2 of own symbol and a null
        for (Integer key: testCells) {
            index = cpuCheckMatrixIndexes(symbol, grid, key, 2);
            if (index != -1) {
                return index;
            }
        }
        // check last move >> check all empty cells
        index = cpuCheckMatrixIndexes(opponentSymbol, grid, lastMove, 2);
        if (index != -1) {
            return index;
        }
        // if no threat is found, find optimal move
        // check rows/columns/diagonals for 2 nulls and own symbol >> move to own method, move to last
        for (Integer key: testCells) {
            index = cpuCheckMatrixIndexes(symbol, grid, key, 1);
            if (index != -1) {
                return index;
            }
        }
        // get random valid cell
        int randomIndex = (int) (Math.random() * testCells.size());
        return testCells.get(randomIndex);
    }

    /*
     * checks the row, column and if present diagonal of provided index
     * returns -1 if no valid option is found
     */
    private int cpuCheckMatrixIndexes(Symbol symbol, ArrayList<Symbol> grid, int x, int frequency) {
        int[] matrixIndexes = getMatrixIndex(x);
        int index;
        index = cpuCheckRow(symbol, grid, matrixIndexes[0], frequency);
        if (index != -1) {
            return index;
        }
        index = cpuCheckColumn(symbol, grid, matrixIndexes[1], frequency);
        if (index != -1) {
            return index;
        }
        index = cpuCheckDiagonal(symbol, grid, matrixIndexes[2], frequency);
        return index;
    }

    // check the last move's row
    private int cpuCheckRow(Symbol symbol, ArrayList<Symbol> grid, int matrixIndex, int frequency) {
        HashMap<Integer, Symbol> row = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            if (i / 3 == matrixIndex) {
                row.put(i, grid.get(i));
            }
        }
        // check for same values and return values
        return cpuCheckFrequency(symbol, row, frequency);

    }

    // checks the last move's column
    private int cpuCheckColumn(Symbol symbol, ArrayList<Symbol> grid, int matrixIndex, int frequency) {
        HashMap<Integer, Symbol> column = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            if (i % 3 == matrixIndex) {
                column.put(i, grid.get(i));
            }
        }
        // check for same values and return values
        return cpuCheckFrequency(symbol, column, frequency);
    }

    // determines what diagonals to check of the last played move
    private int cpuCheckDiagonal(Symbol symbol, ArrayList<Symbol> grid, int matrixIndex, int frequency) {
        int index = -1;
        switch (matrixIndex) {
            case 1 -> index = cpuCheckPrimaryDiagonal(symbol, grid, frequency);
            case 2 -> index = cpuCheckSecondaryDiagonal(symbol, grid, frequency);
            case 3 -> {
                index = cpuCheckPrimaryDiagonal(symbol, grid, frequency);
                if (index == -1) {
                    index = cpuCheckSecondaryDiagonal(symbol, grid, frequency);
                }
            }
            default -> {}
        }
        return index;
    }

    // checks the primary diagonal
    private int cpuCheckPrimaryDiagonal(Symbol symbol, ArrayList<Symbol> grid, int frequency) {
        HashMap<Integer, Symbol> diagonal = new HashMap<>();
        diagonal.put(0, grid.get(0));
        diagonal.put(4, grid.get(4));
        diagonal.put(8, grid.get(8));
        return cpuCheckFrequency(symbol, diagonal, frequency);
    }

    // checks the secondary diagonal
    private int cpuCheckSecondaryDiagonal(Symbol symbol, ArrayList<Symbol> grid, int frequency) {
        HashMap<Integer, Symbol> diagonal = new HashMap<>();
        diagonal.put(2, grid.get(2));
        diagonal.put(4, grid.get(4));
        diagonal.put(6, grid.get(6));
        return cpuCheckFrequency(symbol, diagonal, frequency);
    }

    /*
     * checks the Hashmap for occurrences of the provided symbol and null values
     * returns the index if a valid cell is found
     * returns -1 if no valid cell is found
     */
    private int cpuCheckFrequency(Symbol symbol, HashMap<Integer, Symbol> map, int frequency) {
        if (frequency == 2) {
            // check for two of the same values and a null in the HashMap
            if ((Collections.frequency(map.values(), symbol) == 2) && map.containsValue(null)) {
                // return the available index
                for (Map.Entry<Integer, Symbol> entry : map.entrySet()) {
                    if (entry.getValue() == null) {
                        return entry.getKey();
                    }
                }
            }
        } else {
            // check for one value and two nulls in the HashMap
            if ((Collections.frequency(map.values(), null) == 2) && map.containsValue(symbol)) {
                // return the available index
                for (Map.Entry<Integer, Symbol> entry : map.entrySet()) {
                    if (entry.getValue() == null) {
                        return entry.getKey();
                    }
                }
            }
        }
        return -1;
    }

    // returns true if int x is present in the array
    private boolean contains(int[] array, int x) {
        for (int i = 0; i < array.length; i++) {
            if (i == x) {
                return true;
            }
        }
        return false;
    }

    // getters

    // returns the matrix indexes of the proved integer
    private int[] getMatrixIndex(int x) {
        int width = 3;
        int[] primaryDiagonal = {0,4,8};
        int[] secondaryDiagonal = {2,4,6};

        int row = x / width;
        int column = x % width;
        int diagonal;

        // for diagonals
        if (contains(primaryDiagonal, x) && contains(secondaryDiagonal, x)) {
            diagonal = 3;
        } else if (contains(primaryDiagonal, x)) {
            diagonal = 1;
        } else if (contains(secondaryDiagonal, x)) {
            diagonal = 2;
        } else {
            diagonal = -1;
        }
        
        return new int[]{row, column, diagonal};
    }

    public boolean isComputer() {
        return computer;
    }

    public String getName() {
        return name;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}
