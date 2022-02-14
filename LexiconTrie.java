import structure5.*;
import structure5.Set;
import structure5.Stack;
import structure5.Vector;

import java.util.*;

public class LexiconTrie {

    LexiconNode start = new LexiconNode('#', false);
    private int counter = 0;

    LexiconNode _current = null;
    Stack<LexiconNode> _lastNode = new StackList<>();

    public void current_move(char a) {
        if(_current == null) {
            _current = start;
        }
        _lastNode.push(_current); // Push the last node to the stack of backwrd nodes
        _current = _current.getChild(a);
    }

    public void current_move_back() {
        _current = (_lastNode.empty()) ? null : _lastNode.pop();
    }

    public boolean is_path(char a) {
        if(_current == null) {
            _current = start;
        }
        if(_current.hasChild(a)) return true;
        return false;
    }

    public boolean is_word() {
        if(_current == null) {
            _current = start;
        }
        return _current.isWord();
    }

    public boolean addWord(String word) {

        if(word.equals(null)) return false;

        word = word.toLowerCase();

        LexiconNode c = start;
        char ch;

        for (int x = 0; x < word.length(); x++){
            ch = word.charAt(x);

            if (c.hasChild(ch) == false) c.addChild(new LexiconNode(ch, false));

            c = c.getChild(ch);
        }


        if (c.isWord() == true) return false;
        c.setIsWord(true);
        counter++;
        return true;
    }


    public int addWordsFromFile(String filename) {

        int count = 0;

        try{
            Scanner in = new Scanner(new FileStream(filename));

            String s;

            while(in.hasNextLine()){
                s = in.nextLine();
                if (addWord(s)) count++;
            }

            in.close();

        } catch (Exception e){
            System.out.println("Invalid Filename. Couldn't add words to lexicon");
        }

        return count;
    }


    public boolean removeWord(String word) {

        if(word.equals(null)) return false;

        word = word.toLowerCase();

        if(!containsWord(word)) return false;

        StackList<LexiconNode> s = stackify(word);

        s.get().setIsWord(false);

        counter--;
        int size = s.size();


        for(int x = 1; x < size; x++){

            if(!s.get().isWord() && !s.get().hasChildren()){
                char l = s.pop().getLetter();
                s.get().removeChild(l);
            } else{
                break;
            }
        }

        return true; }

    private StackList<LexiconNode> stackify(String word){

        StackList<LexiconNode> s = new StackList<LexiconNode>();

        LexiconNode c = start;

        s.push(c);

        for(int x = 0; x < word.length(); x++){
            c = c.getChild(word.charAt(x));
            s.push(c);
        }
        return s;
    }

    public int numWords() { return counter;}


    public boolean containsWord(String word){

        if(word.equals(null)) return false;

        word = word.toLowerCase();

        LexiconNode c = start;

        char letter;


        for (int x = 0; x < word.length(); x++){

            letter = word.charAt(x);
            if (c.hasChild(letter) == false) return false;
            c = c.getChild(letter);
        }

        if (c.isWord()) return true;

        return false;
    }


    public boolean containsPrefix(String prefix){


        prefix = prefix.toLowerCase();

        LexiconNode c = start;

        for (int x = 0; x < prefix.length(); x++){
            if (c.hasChild(prefix.charAt(x)) == false) return false;
            c = c.getChild(prefix.charAt(x));
        }

        return true;
    }

    public Iterator<String> iterator() {

        Vector<String> words = new Vector<String>(counter);
        iteratorHelper(new String(""), start, words);
        return words.iterator();
    }


    private void iteratorHelper(String s, LexiconNode n, Vector<String> words){
        if(!n.equals(start)) s += n.getLetter();

        if (n.isWord()) words.add(s);

        if(n.hasChildren()){
            Iterator<LexiconNode> i = n.iterator();
            while(i.hasNext()){
                iteratorHelper(s, i.next(), words);
            }
        }
    }


    public Set<String> suggestCorrections(String target, int maxDistance) {

        Set<String> corrections = new SetList<String>();

        if (target.equals(null) || maxDistance == 0) return corrections;

        target = target.toLowerCase();

        LexiconNode n;

        Iterator<LexiconNode> i = start.iterator();

        while(i.hasNext()){
            n = i.next();
            suggestCorrectionsHelper(target, new String("" + n.getLetter()), maxDistance, n, corrections);
        }
        return corrections;
    }

    private void suggestCorrectionsHelper(String target, String running, int maxDistance, LexiconNode n, Set<String> corrections) {

        if(n.isWord() && running.length() == target.length() && maxDistance > 0) corrections.add(running);

        if(running.length() < target.length() && maxDistance >= 0){
            Iterator<LexiconNode> it = n.iterator();
            while(it.hasNext()){
                n = it.next();
                if(n.getLetter() == target.charAt(running.length())) suggestCorrectionsHelper(target, running
                        + n.getLetter(), maxDistance, n, corrections);
                else suggestCorrectionsHelper(target, running + n.getLetter(), maxDistance - 1, n, corrections);
            }
        }
    }


    public Set<String> matchRegex(String pattern){

        Set<String> regex = new SetList<String>();

        if(pattern.equals(null)) return regex;

        pattern = pattern.toLowerCase();

        matchRegexHelper(pattern, new String(""), start, regex);

        return regex;
    }

    private void matchRegexHelper(String expression, String running, LexiconNode n, Set<String> regex){

        if ((expression.length() == 0 && n.isWord())) regex.add(running);

        if(expression.length() > 0 && n.hasChildren()){

            char l = expression.charAt(0);

            if (l == '?' || l == '*'){

                Iterator<LexiconNode> i = n.iterator();

                matchRegexHelper(expression.substring(1), running, n, regex);
                while (i.hasNext()){
                    n = i.next();

                    if (l == '*') matchRegexHelper(expression, running + n.getLetter(), n, regex);

                    else matchRegexHelper(expression.substring(1), running + n.getLetter(), n, regex);
                }
            } else if (n.hasChild(l)) matchRegexHelper(expression.substring(1), running
                    + n.getChild(l).getLetter(), n.getChild(l), regex);
        }
    }

    public static void main(String[] args) {
        System.out.println("EZZPZ");
    }

    public void reset_current() {
        _current = start;
    }
}

class LexiconNode implements Comparable<LexiconNode> {

    char letter;
    boolean isWord;
    OrderedVector<LexiconNode> v = new OrderedVector<LexiconNode>();

    LexiconNode(char a, boolean b) {
        letter = a;
        isWord = b;
    }

    @Override
    public int compareTo(LexiconNode o) {
        if(o.equals(null)) return 0;
        return this.letter - o.getLetter();
    }
    public char getLetter() {
        return letter;
    }
    public void addChild(LexiconNode ln) {
        if(!ln.equals(null)) v.add(ln);
    }

    public LexiconNode getChild(char ch) {
        for(LexiconNode n : v){
            if (n.getLetter() == ch) return n;
        }
        return null;
    }

    public boolean hasChild(char ch){
        for(LexiconNode n : v){
            if (n.getLetter() == ch) return true;
        }
        return false;
    }

    public void removeChild(char ch) {
        for(LexiconNode n : v){
            if (n.getLetter() == ch) v.remove(n);
        }
    }

    public Iterator<LexiconNode> iterator() {
        return v.iterator();
    }
    public boolean hasChildren(){return v.size() > 0;}

    protected boolean setIsWord(boolean b){
        this.isWord = b;
        return true;
    }

    protected boolean isWord() {return isWord;}
}
