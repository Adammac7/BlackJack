import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JFrame;

public class Main {

    public static void main( String[] args){
        //set up screen
        JFrame parent = new JFrame();
        GameSetupGUI game = new GameSetupGUI(parent);
        // sets fields in main with inputs
        final int money = game.getBuyIn();
        final boolean hitOnSoft17 = game.isHitOnSoft17();
        final String name = game.getName();
        final int numDecks = game.getNumDecks();

        //sets up deck
        Deck deck = new Deck(numDecks);
        int deckSize = deck.getSize();

        //plays game
            GUI gui = new GUI(name,numDecks,money,hitOnSoft17,deck,deckSize);


    }

}
