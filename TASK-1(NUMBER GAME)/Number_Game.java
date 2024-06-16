import java.util.Random;
import java.util.Scanner;

public class Number_Game {
    private static final int MAX_ATTEMPTS = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        boolean playAgain = true;
        int totalScore = 0;
        int roundsPlayed = 0;

        System.out.println("Welcome to the Number Guessing Game!");

        while (playAgain) {
            int numberToGuess = random.nextInt(100) + 1;
            int attemptsLeft = MAX_ATTEMPTS;
            boolean hasGuessedCorrectly = false;

            System.out.println("\nA new number has been generated between 1 and 100. Try to guess it!");

            while (attemptsLeft > 0 && !hasGuessedCorrectly) {
                System.out.println("Attempts left: " + attemptsLeft);
                System.out.print("Enter your guess: ");
                int userGuess = scanner.nextInt();

                if (userGuess == numberToGuess) {
                    System.out.println("Congratulations! You guessed the correct number.");
                    hasGuessedCorrectly = true;
                } else if (userGuess < numberToGuess) {
                    System.out.println("Too low!");
                } else {
                    System.out.println("Too high!");
                }

                attemptsLeft--;
            }

            if (!hasGuessedCorrectly) {
                System.out.println("Sorry, you've run out of attempts. The correct number was " + numberToGuess);
            }

            int roundScore = attemptsLeft+1;
            totalScore += roundScore;
            roundsPlayed++;
            System.out.println("Your score for this round: " + roundScore);
            System.out.println("Total score: " + totalScore);
            
            System.out.print("Do you want to play another round? (yes/no): ");
            playAgain = scanner.next().equalsIgnoreCase("yes");
        }
        System.out.println("Thank you for playing! You played " + roundsPlayed + " round(s) with a total score of " + totalScore);
        scanner.close();
    }
}
