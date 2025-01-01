import java.util.*;
public class Hand {

   protected ArrayList<Card> hand = new ArrayList<>();

   int numCards;
   int numAces;
   int sum;
   boolean blackJack;


   public Hand(){

      numCards = 0;
      numAces = 0;

   }
      public void addCard(Card card){
         hand.add(card);
         numCards++;
         if(card.isAce)
             numAces++;
      }
      public int getNumCards(){return numCards;}
      public Card getCard(int i){
         return hand.get(i);
      }

      public int getSize(){
      return hand.size();
      }
    // iterates through each card in the hand and collects the value of the hand
      public int getVal(){
       int val = 0;
       int numAces = 0;
         for (Card c: hand
              ) {
               if(c.value == 11){
                  numAces++;
                  val+=1;
                  // aces are given the value of 1 by default, but num aces increments
               }else{
                  val += c.value;
                  // gets value of non-ace cards
               }
         }

         while (numAces > 0 && val <= 11) {
            val += 10;
            numAces--;
         }

         return val;
      }



}
