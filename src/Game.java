import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final Board board;
    private final Player player1;
    private final Player player2;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Player> playerList;

    // setup new board and players
    public Game() {
        // initialize new board
        this.board = new Board();
        // print welcome message
        printWelcomeMessage();
        /*
         * ask whether it's a game between human and computer (1)
         * humans (2)
         * or computers (3)
         * and set gameMode accordingly
         */
        int gameMode = setGameMode();
        if (gameMode == 1) {
            // ask for player's name
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            this.player1 = new Player(Symbol.X, name);
            this.player2 = new Player(Symbol.O, "Computer", true);
        } else if (gameMode == 2) {
            // ask for first player's name
            System.out.print("Player one enter your name: ");
            String playerOneName = scanner.nextLine();
            //ask for second player's name
            System.out.print("Player two enter your name: ");
            String playerTwoName = scanner.nextLine();

            this.player1 = new Player(Symbol.X, playerOneName);
            this.player2 = new Player(Symbol.O, playerTwoName);
        } else {
            this.player1 = new Player(Symbol.X, "Computer One", true);
            this.player2 = new Player(Symbol.O, "Computer Two", true);
        }

        // add players to arraylist
        playerList = List.of(player1, player2);
    }

    /*
     * alternate between the human and computer player
     * exit program after the game has concluded
     */
    public void play() {
        int turn = 1;
        int boardStateResult = -1;
        int lastMove = -1;
        while (boardStateResult == -1) {
            boolean isFirstMove = turn == 1;
            if (!(turn % 2 == 0)) {
                // player 1
                lastMove = playMove(player1, isFirstMove, lastMove);
            } else {
                // player 2
                lastMove = playMove(player2, false, lastMove);
            }
            // validate game state
            boardStateResult = board.validateBoardState();
            // increase turn counter
            turn++;
        }
        // print out the result
        printResult(boardStateResult);
        //exit program
        System.exit(0);
    }

    /*
     * determines whether the player is a computer
     * calls playCpu if true
     * asks for player's input otherwise
     */
    private int playMove(Player player, boolean isFirstMove, int lastMove) {
        int move = -1;
        if (player.isComputer()) {
            int cpuMove = player.determineCpuMove(board.getGrid(), lastMove);
            if (cpuMove == -1) {
                throw new RuntimeException("Error while trying to calculate computer move;");
            }
            move = board.playCpu(cpuMove, player.getSymbol(), player.getName());
        } else {
            if (isFirstMove) {
                System.out.print(player.getName() + " enter the first move: ");
            } else {
                System.out.print(player.getName() + " enter your move: ");
            }
            try {
                // ask for new input
                String input = scanner.nextLine();
                // exit if input == exit
                if (input.equals("exit")) {
                    System.exit(0);
                }
                // check if input is valid
                // if invalid, print out valid inputs and call playMove()
                if (board.isValidEntry(input)) {
                    // calls the playPlayer function with the player's symbol and input
                    move = board.playPlayer(player.getSymbol(), input);
                } else {
                    // give valid choices and call playMove()
                    System.out.println("Invalid input.");
                    System.out.println("Valid inputs are: " + board.getValidChoices());
                    playMove(player, isFirstMove, lastMove);
                }
            } catch (InputMismatchException e) {
                // give valid choices and call playMove()
                System.out.println("Invalid input.");
                System.out.println("Valid inputs are: " + board.getValidChoices());
                playMove(player, isFirstMove, lastMove);
            }
        }
        return move;
    }

    // validates the gameMode input at the start of the game
    private int setGameMode() {
        int gameMode = 2;
        // ask for input
        System.out.print("Is it a game between computer/human (1), between humans (2), or computers (3)? : ");
        try {
            gameMode = Integer.parseInt(scanner.nextLine());
            if (!(gameMode == 1 || gameMode == 2 ||gameMode == 3)) {
                System.out.println("Invalid input, choose either 1, 2 or 3.");
                setGameMode();
            }
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input, choose either 1, 2 or 3.");
            setGameMode();
        }
        return gameMode;
    }

    // prints out the result of the game to the console
    private void printResult(int result) {
        if (result == 2) {
            System.out.println("It's a draw!");
        } else {
            // retrieve player name
            String name = playerList.get(result).getName();
            System.out.println("Player " + name + " won!");
        }
    }

    // prints welcome message and board to the console
    private void printWelcomeMessage() {
        System.out.println("Welcome to Tic-Tac-Toe!");
        board.displayBoard(false);
    }
}