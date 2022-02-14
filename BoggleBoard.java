/* BoggleBoard.java
 *
 * ====
 * This class creates a valid Boggle board, using the potential values for each letter given in the
 * cubeStrings[] array.
 *
 * You should not need to edit this file. However, it will be very useful to get acquainted with what
 * accessors/mutators are available and how they work.
 *
 * Let me know if there are parts of the interface of this class that feel clunky /
 * counterintuitive. -Ben
 */


import java.util.Random;

public class BoggleBoard {

    public static final int DEFAULT_ROWS = 5;
    public static final int DEFAULT_COLUMNS = 5;

    private Random rng;

    // list of possible cubes swiped from "BigBoggleCubes" as used in Stanford's 106B.
    // isn't too picky about repeating cubes
    private String[] cubeStrings = {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY",
            "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT", "DHHLOR",
            "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};
    private BoggleLetter[][] board;
    private int rows;
    private int columns;

    // BoggleLetter represents the letter currently displayed on a space on the Boggle board.
    // (remember that outer classes can access private members of inner classes and vice-versa)
    private class BoggleLetter {
        private char letter;
        private boolean visited = false;
        private int row;
        private int column;

        public BoggleLetter() {
            String cube = cubeStrings[rng.nextInt(cubeStrings.length)];
            letter = Character.toLowerCase(cube.charAt(rng.nextInt(cube.length())));
        }
        public char getLetter(){ return letter; }
        public String toString(){ return Character.toString(letter); }

    }
    public BoggleBoard() {
        this(DEFAULT_ROWS, DEFAULT_COLUMNS);
    }

    public BoggleBoard(int rows, int columns, long seed){
        rng = new Random(seed);
        makeBoard(rows, columns);
    }

    public BoggleBoard(int rows, int columns){
        rng = new Random();
        makeBoard(rows, columns);
    }

    public BoggleBoard(long seed) {
        this(DEFAULT_ROWS, DEFAULT_COLUMNS, seed);
    }

    public void setVisited(int row, int column){
        board[row][column].visited = true;
    }
    public void setUnvisited(int row, int column){
        board[row][column].visited = false;
    }

    public boolean isVisited(int row, int column){
        return board[row][column].visited;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public char getCharAt(int row, int column){
        return board[row][column].getLetter();
    }

    public String toString(){
        int rows = board.length;
        int columns = board[0].length;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                output.append(board[i][j].toString().toUpperCase());
            }
            output.append("\n");
        }
        return output.toString();
    }

    private void makeBoard(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        board = new BoggleLetter[rows][columns];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                board[i][j] = new BoggleLetter();
            }
        }
    }

    public static void main(String[] args) {
        BoggleBoard board = new BoggleBoard();
        System.out.println(board);
    }

}


