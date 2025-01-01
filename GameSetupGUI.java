import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/* this class is responsible for creating a GUI to get the users buy in, number of decks for the game,
weather to hit on soft 17 and their name.
**/

public class GameSetupGUI {

    private JDialog dialog;
    private JPanel panel;
    private JLabel messageLabel;
    private JLabel buyInLabel;
    private JTextField buyInField;
    private JLabel numDecksLabel;
    private JTextField numDecksField;
    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel hitOnSoft17Label;
    private JButton yesButton;
    private JButton noButton;
    private JButton enterButton;
    private boolean hitOnSoft17;
    private int buyIn;
    private int numDecks;
    private String name;

    public GameSetupGUI(JFrame parent) {
        createAndShowGUI(parent);
    }

    private void createAndShowGUI(JFrame parent) {
        // the GUI components and layout
        dialog = new JDialog(parent, "Blackjack Game", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false); // make the GUI not resizable

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        messageLabel = new JLabel("Welcome to the casino");
        panel.add(messageLabel);

        buyInLabel = new JLabel("Enter your buy in:");
        panel.add(buyInLabel);

        buyInField = new JTextField(10);
        panel.add(buyInField,BorderLayout.CENTER);

        numDecksLabel = new JLabel("Enter the number of decks:");
        panel.add(numDecksLabel);

        numDecksField = new JTextField(10);
        panel.add(numDecksField,BorderLayout.CENTER);

        nameLabel = new JLabel("Enter your name:");
        panel.add(nameLabel,BorderLayout.CENTER);

        nameField = new JTextField(10);
        panel.add(nameField);

        hitOnSoft17Label = new JLabel("Hit on soft 17?");
        panel.add(hitOnSoft17Label);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        enterButton = new JButton("Enter");

        yesButton = new JButton("Yes");
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitOnSoft17 = true;
                enterButton.setEnabled(true);
            }
        });
        buttonPanel.add(yesButton);

        noButton = new JButton("No");
        noButton.addActionListener(e -> {
            hitOnSoft17 = false;
            enterButton.setEnabled(true);
        });
        buttonPanel.add(noButton);

        panel.add(buttonPanel);

        enterButton.setEnabled(false);
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buyIn = Integer.parseInt(buyInField.getText());
                    numDecks = Integer.parseInt(numDecksField.getText());
                    name = nameField.getText();

                    // Close the dialog
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    messageLabel.setText("Invalid input. Please enter proper answers.");
                }
            }
        });
        panel.add(enterButton);

        dialog.getContentPane().add(panel);
        dialog.setSize(400, 300); // set the GUI size
        dialog.setVisible(true);
    }

    public int getBuyIn() {
        return buyIn;
    }

    public boolean isHitOnSoft17() {
        return hitOnSoft17;
    }

    public int getNumDecks() {
        return numDecks;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame parent = new JFrame();
                GameSetupGUI gui = new GameSetupGUI(parent);
                // Now you can use the values entered by the user
                int buyIn = gui.getBuyIn();
                boolean hitOnSoft17 = gui.isHitOnSoft17();
                int numDecks = gui.getNumDecks();
                String name = gui.getName();
                //...
            }
        });
    }
}