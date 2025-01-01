

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;


public class GUI {

    private final boolean hitOnSoft17;
    private int money;
    private final int numDecks;
    private final String name;
    public static LinkedList<Hand> handsToEval = new LinkedList<>();
    private Deck deck;
    public boolean canSplit;
    public boolean canDouble;
    int betAmount = 0;
    int deckSize;
    boolean hasSplit = false;
    public boolean playingTopHand = true;
    private Hand splitHand1 = new Hand();
    private Hand splitHand2 = new Hand();
    boolean lostFirstHand = false;
    boolean pushed = false;


    public GUI(String name, int numDecks, int money, boolean hitOn17 ,Deck deck,int deckSize) {
        this.name = name;
        this.numDecks = numDecks;
        this.money = money;
        this.hitOnSoft17 = hitOn17;
        this.deckSize= deckSize;
        this.deck = deck;

        GameGUI();

    }

    public void GameGUI() {


        // sets up player and dealer hands, deals cards to both
        Hand playerCards = new Hand();
        DealerHand dealerCards = new DealerHand();
        playerCards.addCard(deck.getCard());
        dealerCards.addCard(deck.getCard());
        playerCards.addCard(deck.getCard());
        dealerCards.addCard(deck.getCard());
        // creates the JFrame that displays the game and sets the corner icon to cards.png
        JFrame frame = new JFrame("Blackjack Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        Image image = new ImageIcon("cards.png").getImage();
        frame.setIconImage(image);


        // create
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Creates the top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE); // Set the background color to white
        topPanel.setLayout(new BorderLayout());

        // Create left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // creates the labels to display information at the top of the JFrame
        JLabel nameLabel = new JLabel(name);
        leftPanel.add(nameLabel);

        JLabel moneyLabel = new JLabel(" $" + money);
        leftPanel.add(moneyLabel);

        topPanel.add(leftPanel, BorderLayout.WEST);

        // Create right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel decksLabel = new JLabel("Decks: " + numDecks);
        rightPanel.add(decksLabel);

        JLabel soft17Label = new JLabel("  Hit on Soft 17: " + ((hitOnSoft17) ? "Yes" : "No"));
        rightPanel.add(soft17Label);

        topPanel.add(rightPanel, BorderLayout.EAST);

        // Create a main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel betPanel = new JPanel();
        betPanel.setLayout(new FlowLayout());

        JLabel betLabel = new JLabel("Enter your bet:");
        betPanel.add(betLabel);

        JPanel betFieldPanel = new JPanel();
        betFieldPanel.setLayout(new BorderLayout());

        JTextField betField = new JTextField(10);
        betFieldPanel.add(betField, BorderLayout.CENTER);

        betPanel.add(betFieldPanel);

        JButton betButton = new JButton("Place Bet");
        betPanel.add(betButton);

        mainPanel.add(betPanel, BorderLayout.CENTER);
        // setting the size ensures the formatting does not have to be dynamic
        frame.add(mainPanel);
        frame.setSize(600, 400);
        frame.setVisible(true);

        // shuffles the deck whenever it gets to 1/3 its original size
        if (deck.getSize() < deckSize / 3) {

            deck.shuffle(numDecks);
            JOptionPane.showMessageDialog(frame, "Shuffling.");

        }

        // Add an action listener to the bet button
        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the bet amount from the text field
                String betText = betField.getText();
                    // try catch to handle invalid bets such as non integer characters
                try {
                    betAmount = Integer.parseInt(betText);


                    if (betAmount > money) {
                        throw new NumberFormatException();
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid bet.");
                    return;
                }
                // logic for what happens to the players money after a bet
                money -= betAmount;
                moneyLabel.setText(" $" + money);
                // Remove the bet panel
                betPanel.setVisible(false);
                boolean canDouble = money >= betAmount && playerCards.getNumCards() == 2;
                boolean canSplit = playerCards.getCard(0).face == playerCards.getCard(1).face && canDouble;

                // Create a panel to hold the card panels
                JPanel cardPanel = new JPanel();
                cardPanel.setLayout(new GridLayout(3, 1, 0, 0)); // 3 rows, 1 column, 0 horizontal gap, 5 vertical gap

                // Create dealer card panels
                JPanel dealerCardPanel = createCardPanelAtStart(dealerCards);
                JPanel dealerCardWrapper = new JPanel();

                dealerCardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                dealerCardWrapper.add(dealerCardPanel,BorderLayout.CENTER);

                // Create player card panels
                JPanel topCardPanel = createCardPanel(playerCards);
                topCardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                JPanel bottomCardPanel = new JPanel();
                bottomCardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                cardPanel.add(dealerCardWrapper);
                cardPanel.add(topCardPanel);
                cardPanel.add(bottomCardPanel);

                mainPanel.add(cardPanel, BorderLayout.CENTER);

                //checks for blackjacks
                if (!checkBlackJack(dealerCards) && checkBlackJack(playerCards)) {


                    int response = JOptionPane.showConfirmDialog(null, "BlACKJACK! Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        // User clicked "Yes", so start a new game
                        money += (betAmount * 6) / 4;
                        new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                        frame.dispose();
                    } else {
                        // User clicked "No" or closed the dialog, so exit the program
                        System.exit(0);
                    }
                } else if(checkBlackJack(dealerCards) && checkBlackJack(playerCards)) {

                    flipSecondDealerCard(dealerCards,dealerCardPanel);
                    int response = JOptionPane.showConfirmDialog(null, "Push. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        // User clicked "Yes", so start a new game
                        money += betAmount;
                        new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                        frame.dispose();
                    } else {
                        // User clicked "No" or closed the dialog, so exit the program
                        System.exit(0);
                    }
                }else if(checkBlackJack(dealerCards) && !checkBlackJack(playerCards)) {

                    flipSecondDealerCard(dealerCards,dealerCardPanel);
                    int response = JOptionPane.showConfirmDialog(null, "Dealer got Blackjack. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        // User clicked "Yes", so start a new game
                        money += betAmount;
                        new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                        frame.dispose();
                    } else {
                        // User clicked "No" or closed the dialog, so exit the program
                        System.exit(0);
                    }
                }
                // Create buttons for the game
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());

                JButton standButton = new JButton("Stand");
                standButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //if the hand hasn't been split
                       if(playingTopHand && !hasSplit) {

                           // standing normally
                           handsToEval.add(playerCards);
                           dealerTurn(deck, dealerCards, betAmount, dealerCardPanel, frame);

                       }else if(playingTopHand && hasSplit){

                           // standing on the first hand of the split
                           handsToEval.add(playerCards);
                           playingTopHand = false;

                       }else{

                           // standing on the second hand of a split
                           handsToEval.add(playerCards);
                           dealerTurn(deck, dealerCards, betAmount, dealerCardPanel, frame);

                       }


                    }
                });
                JButton doubleButton = new JButton("Double");
                doubleButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // logic for doubling, money decreases, label updated
                        money -= betAmount;
                        betAmount *= 2;
                        moneyLabel.setText(" $" + money);
                        hit(topCardPanel, playerCards);
                        handsToEval.add(playerCards);
                        dealerTurn(deck, dealerCards, betAmount, dealerCardPanel, frame);

                    }
                });

                JButton splitButton = new JButton("Split");
                splitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // logic for splitting,
                        money -= betAmount;
                        hasSplit= true;
                        splitButton.setVisible(false);
                        moneyLabel.setText(" $" + money);
                        // creates two new hands with each of the split cards

                        splitHand1.addCard(playerCards.getCard(0));
                        splitHand2.addCard(playerCards.getCard(1));

                        // repaint screen with split cards
                        topCardPanel.removeAll(); // Remove all components from cardPanel
                        // creates two panels for each side of the slip hand, displayed one under another
                        setPanel(topCardPanel,splitHand1);
                        setPanel(bottomCardPanel,splitHand2);

                        cardPanel.revalidate(); // Update cardPanel
                        cardPanel.repaint(); // Repaint cardPanel



                    }
                });
                JButton hitButton = new JButton("Hit");
                hitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        doubleButton.setVisible(false);
                        splitButton.setVisible(false);
                        //hitting with no split
                        if(!hasSplit) {

                            // Add logic for hitting
                            hit(topCardPanel, playerCards);
                            doubleButton.setVisible(false);
                            if (playerCards.getVal() > 21) {
                                bust(frame,hasSplit);
                            }

                        }else if( hasSplit && playingTopHand){

                            //hitting top hand on split
                            hit(topCardPanel, splitHand1);
                            if (splitHand1.getVal() > 21) {
                                bust(frame,hasSplit);
                            }
                            handsToEval.removeFirst();

                        }else{

                            // hitting bottom hand on split
                            hit(bottomCardPanel, splitHand2);
                            if (splitHand2.getVal() > 21) {
                                bust(frame,hasSplit);

                            }

                        }
                    }

                });

                buttonPanel.add(hitButton);
                buttonPanel.add(standButton);
                if (canDouble) {
                    buttonPanel.add(doubleButton);
                }
                if (canSplit) {
                    buttonPanel.add(splitButton);
                }

                mainPanel.add(buttonPanel, BorderLayout.SOUTH);

                // Repaint the panel
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

    }



    private void bust(JFrame frame, boolean hasSplit) {

        if(!hasSplit || (hasSplit && !playingTopHand && handsToEval.isEmpty())) {

            // bust result without a split and when both split hands have busted
            int response = JOptionPane.showConfirmDialog(null, "You bust. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                // User clicked "Yes", so start a new game
                new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                frame.dispose();

            } else {

                // User clicked "No" or closed the dialog, so exit the program
                System.exit(0);

            }
        }else if(hasSplit && playingTopHand){

            //bust result with split on top had
            JOptionPane.showMessageDialog(frame, "This hand went bust. Play the second hand.");
            playingTopHand = false;
            lostFirstHand = true;

        }

    }

    private JPanel createCardPanel(Hand hand) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout());


        for (int i = 0; i < hand.getNumCards(); i++) {

            JPanel newCardPanel = new JPanel();

            newCardPanel.setPreferredSize(new Dimension(50, 70));
            newCardPanel.setBackground(Color.WHITE);
            newCardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel newCardText = new JLabel(String.valueOf(hand.getCard(i).face), SwingConstants.CENTER);
            newCardText.setFont(new Font("Arial", Font.BOLD, 24));

            newCardPanel.add(newCardText);
            cardPanel.add(newCardPanel);
        }

        return cardPanel;
    }

    private JPanel createCardPanelAtStart(DealerHand hand) {

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout());

        JPanel card1Panel = new JPanel();
        card1Panel.setPreferredSize(new Dimension(50, 70));
        card1Panel.setBackground(Color.WHITE);
        card1Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel card1Text = new JLabel(String.valueOf(hand.getCard(0).face), SwingConstants.CENTER);
        card1Text.setFont(new Font("Arial", Font.BOLD, 24));
        card1Panel.add(card1Text);

        JPanel card2Panel = new JPanel();
        card2Panel.setPreferredSize(new Dimension(50, 70));
        card2Panel.setBackground(Color.WHITE);
        card2Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel card2Text = new JLabel(" ", SwingConstants.CENTER);
        card2Text.setFont(new Font("Arial", Font.BOLD, 24));
        card2Panel.add(card2Text);

        cardPanel.add(card1Panel);
        cardPanel.add(card2Panel);
        return cardPanel;
    }

    public boolean checkBlackJack(Hand hand) {
        return hand.getCard(0).value + hand.getCard(1).value == 21;
    }

    public void setPanel(JPanel panel,Hand cards){

        panel.removeAll();
        canDouble = false;
        canSplit = false;

        JPanel newCardPanel = createCardPanel(cards);
        panel.add(newCardPanel);

        panel.revalidate();
        panel.repaint();
    }
    public void hit(JPanel cardPanel, Hand cards) {
        cardPanel.removeAll();
        System.out.println("adding a card");
        cards.addCard(deck.getCard());
        canDouble = false;
        canSplit = false;

        JPanel newCardPanel = createCardPanel(cards);
        cardPanel.add(newCardPanel);

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private int dealerTurn(Deck deck, DealerHand dealerCards, int bet, JPanel dealerCardPanel, JFrame frame) {

        printForTestDeck(dealerCards);
        flipSecondDealerCard(dealerCards,dealerCardPanel);

        //runs when not hitting on soft 17
        while (dealerCards.getVal() < 17 && !hitOnSoft17) {
            hit(dealerCardPanel, dealerCards);
            printForTestDeck(dealerCards);
            if (dealerCards.getVal() > 21) {
                money += handsToEval.size() * bet * 2;
                int response = JOptionPane.showConfirmDialog(null, "Dealer busts, you Win! Play again?", "Play Again", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name,numDecks,money,hitOnSoft17,deck,deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
                //JOptionPane.showMessageDialog(frame, "Invalid bet amount. Please enter a valid number.");
                return 0;
            }

        }
        // this setup if for dealer to hit on soft 17, remove or to make the dealer stand on soft 17
        while (dealerCards.getVal() < 17 && hitOnSoft17 || ((dealerCards.getVal()  == 17 && dealerCards.numAces > 0 ) && hitOnSoft17) ){

            hit(dealerCardPanel, dealerCards);
            if (dealerCards.getVal() > 21) {
                money += handsToEval.size() * bet * 2;
                int response = JOptionPane.showConfirmDialog(null, "Dealer busts, you Win! Play again?", "Play Again", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name,numDecks,money,hitOnSoft17,deck,deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
                return 0;
            }
        }
        determineWinner(dealerCards,betAmount,frame);
        return 0;
    }
    private void determineWinner(DealerHand dealerCards, int bet,JFrame frame) {
    //determines winner without a split
    if(!hasSplit){
    while(!handsToEval.isEmpty()) {


        int playerVal = handsToEval.getFirst().getVal();
        int dealerVal = dealerCards.getVal();



        if (dealerVal > playerVal && dealerVal < 22) {
            //dealer wins case
            int response = JOptionPane.showConfirmDialog(null, "You lost. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // User clicked "Yes", so start a new game
                new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                frame.dispose();
            } else {
                // User clicked "No" or closed the dialog, so exit the program
                System.exit(0);
            }


        } else if (dealerVal == playerVal) {
            //push case
            int response = JOptionPane.showConfirmDialog(null, "You pushed. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // User clicked "Yes", so start a new game
                new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                frame.dispose();
            } else {
                // User clicked "No" or closed the dialog, so exit the program
                System.exit(0);
            }
            money += bet;

        } else {
            //player wins
            int response = JOptionPane.showConfirmDialog(null, "You won! Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // User clicked "Yes", so start a new game
                new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                frame.dispose();
            } else {
                // User clicked "No" or closed the dialog, so exit the program
                System.exit(0);
            }
            money += 2 * bet;

        }
        handsToEval.removeFirst();

    }
}else {


    // evaluating multiple hands

    while (!handsToEval.isEmpty()) {

        int playerVal = handsToEval.getFirst().getVal();
        int dealerVal = dealerCards.getVal();

        if (dealerVal > playerVal) {
            //dealer wins case

            if (money <= 0 && handsToEval.size() == 1) {
                JOptionPane.showMessageDialog(frame, "Game over. You're all out of money.");
                System.exit(0);
            } else if (handsToEval.size() == 2) {
                // dealer wins first hand

                lostFirstHand = true;
                handsToEval.removeFirst();

            } else if (handsToEval.size() == 1 && pushed) {
                // dealer wins second hand after first hand was a push

                int response = JOptionPane.showConfirmDialog(null, "You pushed, then lost. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
            } else if (handsToEval.size() == 1 && !lostFirstHand) {
                // dealer wins after user wins

                int response = JOptionPane.showConfirmDialog(null, "You won the first and lost the second. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
            } else if (handsToEval.size() == 1 && lostFirstHand) {
                // dealer wins with no more hands to evaluate

                int response = JOptionPane.showConfirmDialog(null, "You lost both hands. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
            } else {
                lostFirstHand = true;
                handsToEval.removeFirst();

            }

        } else if (dealerVal == playerVal) {
            //push case


            if (handsToEval.size() == 1 && lostFirstHand) {
                // push on last hand after a win

                money += bet;
                int response = JOptionPane.showConfirmDialog(null, "You lost, then pushed. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }

            } else if (handsToEval.size() == 1 && pushed) {
                // push on second hand after pushing the first
                money += bet;
                int response = JOptionPane.showConfirmDialog(null, "Both hands pushed. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
            } else if (handsToEval.size() == 1 && !lostFirstHand) {
                // dealer wins with no more hands to evaluate
                money += bet;
                int response = JOptionPane.showConfirmDialog(null, "You won, then pushed. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
            } else {
                handsToEval.removeFirst();
                money += bet;
                pushed = true;

            }


        } else {
            //player wins


            if (handsToEval.size() == 1 && !lostFirstHand) {
                // player wins both hands

                money += bet * 2;
                int response = JOptionPane.showConfirmDialog(null, "You won both hands! Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
            } else if (handsToEval.size() == 1 && pushed) {
                // player pushes then wins

                money += bet * 2;
                int response = JOptionPane.showConfirmDialog(null, "You pushed, then won. Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    // User clicked "Yes", so start a new game
                    new GUI(name, numDecks, money, hitOnSoft17, deck, deckSize);
                    frame.dispose();
                } else {
                    // User clicked "No" or closed the dialog, so exit the program
                    System.exit(0);
                }
            } else {
                lostFirstHand = false;
                handsToEval.removeFirst();
                money += bet * 2;

            }


        }
        handsToEval.removeFirst();


    }
}
    }
        private void flipSecondDealerCard(DealerHand dealerCards, JPanel dealerCardPanel){
        // two step process to gain access to and change the second dealer card
        Component[] components = dealerCardPanel.getComponents();
        JPanel[] subPanels = new JPanel[2];
        int subPanelIndex = 0;
        for (Component component : components) {
            if (component instanceof JPanel) {
                subPanels[subPanelIndex++] = (JPanel) component;
                if (subPanelIndex == 2) {
                    break;
                }
            }
        }

        // Get the label from the second sub panel
        if (subPanels[1] != null) {
            Component[] subComponents = subPanels[1].getComponents();
            for (Component subComponent : subComponents) {
                if (subComponent instanceof JLabel) {
                    JLabel label = (JLabel) subComponent;

                    label.setText(String.valueOf(dealerCards.getCard(1).face));
                }
            }
        }
    }

    public void printForTestDeck(Hand d) {

    for(int i =0; i < d.getSize();i++){
        System.out.print(d.getCard(i).face + " ");
    }
        System.out.println();
    }
    
}
