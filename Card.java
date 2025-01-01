public class Card {
    public int value;
    public char face;
    public char suite;
    public boolean isAce;

    public Card() {
        value = 11;
        face = 'A';
        suite = 'H';
        isAce = true;
    }

    public Card(int val, char f, char s,boolean isA){
        value = val;
        face = f;
        suite = s;
        isAce = isA;
    }

}
