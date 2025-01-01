

import java.util.LinkedList;
import java.util.Stack;
import java.util.Random;

public class Deck extends Stack<Card> implements Iterable<Card> {

    private Stack<Card> deck = new Stack<>();
   // UC is a temporary holder for newly created cards
    private LinkedList<Card> UC = new LinkedList<>();
    public java.util.Iterator<Card> iterator() {
        return deck.iterator();
    }

public Deck(int decks){
    shuffle(decks);
}

public int getSize(){
    // returns number of cards in the deck
    return this.deck.size();
}


    // creates 52 cards per deck and adds them to UC
    private void addCards( int numDecks){
        // UC is a temporary holder of all the new cards that are created, then they are randomly selected
        // and placed into the stack creating a deck with a random order of cards
        for(int d = 0; d < numDecks;d++) {
            for (int v = 2; v < 15; v++) {
                for(int s = 1; s < 5; s++) {
                   UC.add( new Card(resetValues(v),setFace(v), setSuite(s),isAce(v)) );

                }

            }
        }

    }
    // sets the value of aces to 11 and face cards to 10 after the face has been set
    private int resetValues(int val) {
        if (val == 14) {
            val = 11;
        } else if (val >= 10) {
            val = 10;
        }
        return val;
    }


    //returns the suit of the card val from addCards
    private char setSuite(int val){
        if(val == 1){
            return 'H';
        }else if( val == 2){
            return 'C';
        }else if( val == 3){
            return 'D';
        }else{
            return 'S';
        }

    }

    private boolean isAce(int val){return val == 14;}//checks if a card is an ace, value of 14 signifies an ace
   // each kind of card gets its own value so it can be represented as a unique card instead of how many points its worth
    private char setFace(int input) {
        if (input == 14) {
            return 'A';
        } else if (input == 13) {
            return 'K';
        } else if (input == 12) {
            return 'Q';
        } else if (input == 11) {
            return 'J';
        } else if (input == 10) {
            return 'T';
        } else {
            return Integer.toString(input).charAt(0);
        }

    }
    public void shuffle(int numDecks){
        // clears the deck and creates a new deck
       if( !deck.empty() ){
           deck.clear();
       }
        addCards(numDecks);
        Random rand = new Random();
       while( !UC.isEmpty() ) {
           deck.push( getCardUC( rand.nextInt( 0,UC.size() ) ) );
       }

    }
    public Card getCardUC(int index) {
        try {
            //gets a card from the list of new cards,
            Card tempCard = UC.get(index);
            UC.remove(index);
            return tempCard;
        } catch (Exception e) {
            throw new ArrayIndexOutOfBoundsException();
        }

    }

    public Card getCard(){return deck.pop();} // returns top card on the deck, draws a card

}
