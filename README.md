# Blackjack Game Project

## Overview

This project is a graphical implementation of the popular card game **Blackjack**. The game features a GUI built with Java Swing and provides an engaging user experience for players. It supports essential Blackjack functionalities such as hitting, standing, splitting, doubling down, and evaluating winners, all while adhering to customizable game rules like the number of decks and whether the dealer hits on a soft 17.

---

## Features

- **User-Friendly GUI**: Intuitive graphical interface for seamless gameplay.
- **Customizable Rules**:
  - Choose the number of decks.
  - Decide whether the dealer hits on a soft 17.
- **Blackjack Mechanics**:
  - Support for splitting hands.
  - Doubling down on the initial bet.
  - Automatic deck shuffling when the deck size falls below one-third.
- **Real-Time Feedback**:
  - Displays player and dealer cards.
  - Updates player money and game results dynamically.
- **Replay Option**: Prompt to play again after each game.

---

## How to Play

1. **Setup**:
   - Enter your name, initial buy-in amount, number of decks, and rule preferences in the setup GUI.
2. **Place Your Bet**:
   - Enter a bet amount that doesn’t exceed your available funds.
3. **Gameplay**:
   - Choose actions like Hit, Stand, Split, or Double depending on the cards dealt.
4. **Game Evaluation**:
   - The winner is determined based on Blackjack rules, and your balance is updated accordingly.
5. **Replay or Exit**:
   - After each game, you can choose to play again or exit.

---

## Code Structure

### Main Components

1. **`Main.java`**:
   - Entry point of the application. Initializes the game setup GUI and starts the game.
   - [Source Code](./Main.java)

2. **`GameSetupGUI.java`**:
   - Handles initial game setup inputs like buy-in amount, number of decks, and dealer rules.
   - [Source Code](./GameSetupGUI.java)

3. **`GUI.java`**:
   - Implements the core game logic and user interface for playing Blackjack.
   - [Source Code](./GUI.java)

4. **`Deck.java`**:
   - Manages the deck of cards, including shuffling and dealing cards.
   - [Source Code](./Deck.java)

5. **`Card.java`**:
   - Represents individual cards with attributes like value, face, and suit.
   - [Source Code](./Card.java)

6. **`Hand.java`**:
   - Represents a player's or dealer's hand, with methods to calculate the hand's value and manage cards.
   - [Source Code](./Hand.java)

7. **`DealerHand.java`**:
   - Extends `Hand` with dealer-specific logic.
   - [Source Code](./DealerHand.java)

---

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher.
- A Java-compatible IDE (e.g., IntelliJ IDEA, Eclipse) or terminal for running the program.

### Steps
1. Clone the repository or download the source files.
2. Open the project in your preferred IDE.
3. Compile and run `Main.java' from the main class.

---

## Screenshots


### In-Game Screen
![In-Game Screen](|./Screen Cap 0145 2025-01-01.jpg)


---


---

## Author
This project was developed as a demonstration of Java Swing and object-oriented programming principles.

Feel free to fork and enhance this project!
