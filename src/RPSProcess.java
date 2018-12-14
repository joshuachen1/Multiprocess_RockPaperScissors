import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * @author Joshua Chen
 *         Date: Dec 12, 2018
 */
public class RPSProcess {
    private static int gamePort = 1234;
    private static int[] points = new int[3];

    public RPSProcess(int numGames) throws Exception {

        // Every other process to arrive simply plays.
        try {
            System.out.println("Connected successfully");

            Socket clientSocket = new Socket("localhost", gamePort);

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            for (int i = 0; i < numGames; i++) {
                out.writeInt((int) (Math.random() * 3));
                out.flush();
            }

            out.close();
            clientSocket.close();

            System.out.println("Game over");
        }
        // First process to arrive becomes the host and player.
        catch(ConnectException i) {
            ServerSocket hostSocket = new ServerSocket(gamePort);

            System.out.println("Waiting for players...");


            Socket client1 = hostSocket.accept();
            Socket client2 = hostSocket.accept();

            DataInputStream c1Input = new DataInputStream(
                    new BufferedInputStream(client1.getInputStream()));
            DataInputStream c2Input = new DataInputStream(
                    new BufferedInputStream(client2.getInputStream()));

            for (int j = 0; j < numGames; j++) {
                int c0Choice = (int) (Math.random() * 3);
                int c1Choice = c1Input.read();
                int c2Choice = c2Input.read();

                displayResults(c0Choice, c1Choice, c2Choice, points, j + 1);
            }

            client1.close();
            client2.close();
            hostSocket.close();

            System.out.println("Total Points Won");
            System.out.println("------------------------------------");
            for (int j = 0; j < points.length; j++) {
                System.out.println("Client " + j + " won a total of " + points[j] + " points!");
            }
            System.out.println("------------------------------------");
        }
    }

    private void displayResults(int c0Choice, int c1Choice, int c2Choice, int[] points, int round) {
        String[] rps = {"rock", "paper", "scissors"};
        System.out.println("Game Round: " + round);
        System.out.println("------------------------------------");
        System.out.println("Client 0: " + rps[c0Choice]);
        System.out.println("Client 1: " + rps[c1Choice]);
        System.out.println("Client 2: " + rps[c2Choice]);

        // Client 0 Beats Both
        if ((c0Choice - c1Choice + 3) % 3 == 1 && (c0Choice - c2Choice + 3) % 3 == 1) {
            points[0] += 2;
            System.out.println("Client 0 won 2 points!");
        }
        // Client 1 Beats Both
        else if ((c1Choice - c0Choice + 3) % 3 == 1 && (c1Choice - c2Choice + 3) % 3 == 1) {
            points[1] += 2;
            System.out.println("Client 1 won 2 points!");

        }
        // Client 2 Beats Both
        else if ((c2Choice - c0Choice + 3) % 3 == 1 && (c2Choice - c1Choice + 3) % 3 == 1) {
            points[2] += 2;
            System.out.println("Client 2 won 2 points!");

        }
        // c0 and c1 Beat c2
        else if (c0Choice == c1Choice && (c0Choice - c2Choice + 3) % 3 == 1) {
            points[0] += 1;
            points[1] += 1;
            System.out.println("Client 0 won 1 point!");
            System.out.println("Client 1 won 1 point!");
        }
        // c0 and c2 Beat c1
        else if (c0Choice == c2Choice && (c0Choice - c1Choice + 3) % 3 == 1) {
            points[0] += 1;
            points[2] += 1;
            System.out.println("Client 0 won 1 point!");
            System.out.println("Client 2 won 1 point!");

        }
        // c1 and c2 Beat c0
        else if (c1Choice == c2Choice && (c1Choice - c0Choice + 3) % 3 == 1) {
            points[1] += 1;
            points[2] += 1;
            System.out.println("Client 1 won 1 point!");
            System.out.println("Client 2 won 1 point!");
        }
        else {
            System.out.println("No points awarded");
        }
        System.out.println("------------------------------------");
    }

    public static void main(String[] args) throws Exception {

        System.out.print("Number of Games to Play: ");
        int numGames = new Scanner(System.in).nextInt();

        new RPSProcess(numGames);
    }
}
