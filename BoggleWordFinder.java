/* This is where you'll write your code to use a canonical graph-traversal algorithm to
 * solve a problem that at first may not seem like it's a graph problem at all.
 *
 * A note: This really is a fun one. If it gets to feel frustrating instead of fun, or if you feel
 * like you're  completely stuck, step back a bit and ask some questions.
 *
 * Spend a lot of time sketching out a plan, and figuring out which data structures might be
 * best for the various tasks, before you write any code at all.
 *
 * If you're not sure about how to approach this as a graph problem, feel free to ask questions.
 * (I won't give you all the answer, but...)
 * -Ben
 *
 */
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BoggleWordFinder {

    public static final String WORD_LIST = "words";
    public static final int ROWS = 100;
    public static final int COLUMNS = 100;
    public static final int SEED = 137;

    public static LexiconTrie trie = new LexiconTrie();

    static int counter = 0;

    static TreeSet<String> answer = new TreeSet<>();

    public static void main(String[] args) throws IOException {

        BoggleBoard board = new BoggleBoard(ROWS, COLUMNS, SEED);

        ReadFile.readFile(WORD_LIST, trie);

        String outFileName = "output_optimized_large_board";
        FileOutputStream outStream = new FileOutputStream(outFileName);

        double startTime = System.currentTimeMillis();
        solver(board);
        double endTime = System.currentTimeMillis();

        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getColumns(); j++) {
                System.out.print(Character.toUpperCase(board.getCharAt(i,j)));
                outStream.write(Character.toUpperCase(board.getCharAt(i,j)));
            }
            System.out.println();
            outStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        }

        for(String word : answer) {
            System.out.println(word);
            outStream.write(word.getBytes(StandardCharsets.UTF_8));
            outStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        }

        String temp = "Found "+counter+" words found in "+(endTime - startTime)+" milliseconds";
        System.out.println(temp);

        outStream.write(temp.getBytes(StandardCharsets.UTF_8));

    }


    static void solver(BoggleBoard board) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {

                trie.reset_current();

                processSolver(board, i, j, ""+board.getCharAt(i,j));
            }
        }
    }

    private static void processSolver(BoggleBoard board, int x, int y, String word) {

        if(!trie.is_path(board.getCharAt(x,y))) {
            return;
        }

        trie.current_move(board.getCharAt(x,y));

        if(trie.is_word() && !answer.contains(word)) {
            answer.add(word);
            counter++;
        }



        if (0 <= x - 1 && 0 <= y - 1 && !board.isVisited(x - 1,y - 1)){
            if(trie.is_path(board.getCharAt(x-1,y-1)))
            {
                processSolver(board, x-1, y-1, word+board.getCharAt(x-1,y-1));

                trie.current_move_back();
            }
        }


        if (0 <= y - 1 && !board.isVisited(x,y - 1)){
            if(trie.is_path(board.getCharAt(x,y-1)))
            {
                processSolver(board, x, y-1, word + board.getCharAt(x,y-1));
                trie.current_move_back();
            }
        }

        if (x + 1 < board.getRows() && 0 <= y - 1 && !board.isVisited(x + 1,y - 1)){
            if(trie.is_path(board.getCharAt(x+1,y-1)))
            {
                processSolver(board, x+1, y-1, word+board.getCharAt(x+1,y-1));
                trie.current_move_back();
            }
        }

        if (x + 1 < board.getRows() && !board.isVisited(x + 1,y)){
            if(trie.is_path(board.getCharAt(x+1,y)))
            {
                processSolver(board, x+1, y, word + board.getCharAt(x+1,y));
                trie.current_move_back();
            }

        }

        if (x+1 < board.getRows() && y+1 < board.getColumns() && !board.isVisited(x+1,y+1)){
            if(trie.is_path(board.getCharAt(x+1,y+1)))
            {
                processSolver(board, x+1, y+1, word+board.getCharAt(x+1,y+1));
                trie.current_move_back();
            }
        }

        if (y + 1 < board.getColumns() && !board.isVisited(x,y + 1)){
            if(trie.is_path(board.getCharAt(x,y+1)))
            {
                processSolver(board, x, y+1, word + board.getCharAt(x,y+1));
                trie.current_move_back();
            }
        }

        if (0 <= x - 1 && y + 1 < board.getColumns() && !board.isVisited(x - 1,y + 1)) {
            if (trie.is_path(board.getCharAt(x - 1, y + 1)))
            {
                processSolver(board, x-1, y+1, word+board.getCharAt(x-1,y+1));
                trie.current_move_back();
            }
        }

        if (0 <= x - 1 && !board.isVisited(x - 1,y)){
            if(trie.is_path(board.getCharAt(x-1,y)))
            {
                processSolver(board, x-1, y, word + board.getCharAt(x-1,y));
                trie.current_move_back();
            }
        }


    }

}