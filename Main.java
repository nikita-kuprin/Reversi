import java.util.Objects;
import java.util.Scanner;

class Board
{
    private String[][] gameboard = new String[8][8];
    private int[] tilesToFlipX = new int[64];
    private int[] tilesToFlipY = new int[64];
    private int countToFlip = 0;
    String[][] getCopyBoard()
    {
        String[][] gameboardCopy = new String[8][8];
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                gameboardCopy[x][y] = gameboard[x][y];
            }
        }
        return gameboardCopy;
    }
    void resetBoard()
    {
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                gameboard[x][y] = " ";
            }
        }
        gameboard[3][3] = "X";
        gameboard[3][4] = "O";
        gameboard[4][3] = "O";
        gameboard[4][4] = "X";
    }
    void drawBoard()
    {
        String horisontLine = "  +---+---+---+---+---+---+---+---+";
        String verticalLine = "  |   |   |   |   |   |   |   |   |";
        System.out.println("    1   2   3   4   5   6   7   8");
        System.out.println(horisontLine);
        for (int y = 1; y <= 8; y++)
        {
            System.out.print(y);
            System.out.print(" ");
            for (int x = 0; x < 8; x++)
            {
                System.out.print("| " + gameboard[x][y - 1] + " ");
            }
            System.out.println("|");
            System.out.println(horisontLine);
        }
    }
    int[] getScore()
    {
        int xScore = 0;
        int oScore = 0;
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (Objects.equals(gameboard[x][y], "X"))
                {
                    xScore += 1;
                }
                else if (Objects.equals(gameboard[x][y], "O"))
                {
                    oScore += 1;
                }
            }
        }
        int[] score = new int[2];
        score[0] = xScore;
        score[1] = oScore;
        return score;
    }
    boolean isValidMove(int x, int y, String current)
    {
        if (Objects.equals(gameboard[x][y], "X") || Objects.equals(gameboard[x][y], "O"))
        {
            return false;
        }
        if (!(x >= 0 && x <= 7 && y >= 0 && y <= 7))
        {
            return false;
        }

        gameboard[x][y] = current;
        String other = "";
        if (Objects.equals(current, "X"))
        {
            other = "O";
        }
        else
        {
            other = "X";
        }

        int[][] mas = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
        tilesToFlipX = new int[64];
        tilesToFlipY = new int[64];
        int counter = 0;
        countToFlip = 0;
        for (int[] ma : mas) {
            int xS = x;
            int yS = y;
            xS += ma[0];
            yS += ma[1];
            if (xS >= 0 && xS <= 7 && yS >= 0 && yS <= 7 && Objects.equals(gameboard[xS][yS], other)) {
                xS += ma[0];
                yS += ma[1];
                if (!(xS >= 0 && xS <= 7 && yS >= 0 && yS <= 7)) {
                    continue;
                }
                while (Objects.equals(gameboard[xS][yS], other)) {
                    xS += ma[0];
                    yS += ma[1];
                    if (!(xS >= 0 && xS <= 7 && yS >= 0 && yS <= 7)) {
                        break;
                    }
                }
                if (!(xS >= 0 && xS <= 7 && yS >= 0 && yS <= 7)) {
                    continue;
                }
                if (Objects.equals(gameboard[xS][yS], current))
                {
                    while (true)
                    {
                        xS -= ma[0];
                        yS -= ma[1];
                        if (Objects.equals(xS, x) && Objects.equals(yS, y))
                        {
                            break;
                        }
                        tilesToFlipX[counter] = xS;
                        tilesToFlipY[counter] = yS;
                        countToFlip ++;
                        counter++;
                    }
                }
            }
        }
        gameboard[x][y] = " ";
        return countToFlip != 0;
    }
    boolean areValidMoves(String Tile)
    {
        boolean flag = false;
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (isValidMove(x, y, Tile)){
                    flag = true;
                }
            }
        }
        return flag;
    }
    void makeMove(int x, int y, String tile)
    {
        gameboard[x][y] = tile;
        for (int i = 0; i < countToFlip; i++)
        {
            gameboard[tilesToFlipX[i]][tilesToFlipY[i]] = tile;
        }
    }
    void copyBoard(Board game)
    {
        gameboard = game.getCopyBoard();
    }
    void getPossibleMoves(Board game, String playerTile)
    {
        gameboard = game.getCopyBoard();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (game.isValidMove(x, y, playerTile)) {
                    gameboard[x][y] = ".";
                }
            }
        }
    }
}

class Player
{
    static int curX = 0;
    static int curY = 0;
    int getCurX()
    {
        return curX;
    }
    int getCurY()
    {
        return curY;
    }
    void getMove(Board game, String playerTile)
    {
        int temp = 0;
        while (temp < 11 || temp > 88)
        {
            System.out.println("Enter your move in the format {1 <= digit <= 8}{1 <= digit <= 8}");
            System.out.println("You can get an updated field with possible moves by writing me \"helpme\"");
            Scanner in = new Scanner(System.in);
            String wish = in.nextLine();
            if (Objects.equals(wish, "quit"))
            {
                System.exit(0);
            }
            if (Objects.equals(wish, "helpme"))
            {
                Board copyBoard = new Board();
                copyBoard.getPossibleMoves(game, playerTile);
                copyBoard.drawBoard();
                continue;
            }
            temp = Integer.parseInt(wish);
        }
        curX = temp / 10 - 1;
        curY = temp % 10 - 1;
    }
}

class Computer
{
    int[] getBestMove(Board game, String computerTile)
    {
        int[][] possible = new int[64][2];
        int count = 0;
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (game.isValidMove(x, y, computerTile))
                {
                    int[] pair = new int[2];
                    pair[0] = x;
                    pair[1] = y;
                    possible[count] = pair;
                    count++;
                }
            }
        }
        for (int i = 0; i < count; i++)
        {
            if ((Objects.equals(possible[i][0], 0) && Objects.equals(possible[i][1], 0)) ||
                    (Objects.equals(possible[i][0], 7) && Objects.equals(possible[i][1], 7)) ||
                    (Objects.equals(possible[i][0], 7) && Objects.equals(possible[i][1], 0)) ||
                    (Objects.equals(possible[i][0], 0) && Objects.equals(possible[i][1], 7))) {
                return possible[i];
            }
        }
        int bestScore = -1;
        int[] bestMove = new int[2];
        for (int i = 0; i < count; i++)
        {
            Board secondBoard = new Board();
            secondBoard.copyBoard(game);
            secondBoard.makeMove(possible[i][0], possible[i][1], computerTile);
            int[] tempScore = new int[2];
            tempScore = secondBoard.getScore();
            if (Objects.equals(computerTile, "X"))
            {
                if (bestScore < tempScore[0])
                {
                    bestMove = possible[i];
                    bestScore = tempScore[0];
                }
            }
            else
            {
                if (bestScore < tempScore[0])
                {
                    bestMove = possible[i];
                    bestScore = tempScore[0];
                }
            }
        }
        return bestMove;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Reversi game");
        Board game = new Board();
        game.resetBoard();
        System.out.println("To exit the game, write \"quit\" during your turn");
        System.out.println("You can get an updated field with possible moves by writing me \"helpme\"");
        System.out.println("Playing with a computer(1) or with another player(2)?");
        Scanner in = new Scanner(System.in);
        String wish = in.nextLine();
        int gameMode;
        if (Objects.equals(wish, "1"))
        {
            gameMode = 1;
        }
        else
        {
            gameMode = 0;
        }
        if (Objects.equals(gameMode, 1))
        {
            Player player = new Player();
            Computer computer = new Computer();
            String playerTile, computerTile;
            wish = "";
            System.out.println("Player \"O\" goes first");
            while((!(Objects.equals(wish, "O")) && (!(Objects.equals(wish, "X")))))
            {
                System.out.println("Do you want to be X or O?");
                in = new Scanner(System.in);
                wish = in.nextLine();
            }
            playerTile = wish;
            String turn;
            if (Objects.equals(playerTile, "O"))
            {
                computerTile = "X";
                turn = "player";
            }
            else
            {
                computerTile = "O";
                turn = "computer";
            }
            while (true)
            {
                if (Objects.equals(turn, "player"))
                {
                    game.drawBoard();
                    int[] curScore = new int[2];
                    curScore = game.getScore();
                    System.out.println("Player1 have " + curScore[1] + " Score");
                    System.out.println("Player2 have " + curScore[0] + " Score");
                    player.getMove(game, playerTile);
                    while (!(game.isValidMove(player.getCurX(), player.getCurY(), playerTile)))
                    {
                        player.getMove(game, playerTile);
                    }
                    game.isValidMove(player.getCurX(), player.getCurY(), playerTile);
                    game.makeMove(player.getCurX(), player.getCurY(), playerTile);
                    if (game.areValidMoves(computerTile))
                    {
                        turn = "computer";
                    }
                    else
                    {
                        break;
                    }
                }
                else
                {
                    game.drawBoard();
                    int[] curScore = new int[2];
                    curScore = game.getScore();
                    System.out.println("Player1 have " + curScore[1] + " Score");
                    System.out.println("Player2 have " + curScore[0] + " Score");
                    int[] currCompMove = computer.getBestMove(game, computerTile);
                    game.isValidMove(currCompMove[0], currCompMove[1], computerTile);
                    game.makeMove(currCompMove[0], currCompMove[1], computerTile);
                    if (game.areValidMoves(playerTile))
                    {
                        turn = "player";
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            Player player1 = new Player();
            Player player2 = new Player();
            String player1Tile, player2Tile;
            wish = "";
            while((!(Objects.equals(wish, "O")) && (!(Objects.equals(wish, "X")))))
            {
                System.out.println("First player, do you want to be X or O?");
                in = new Scanner(System.in);
                wish = in.nextLine();
            }
            player1Tile = wish;
            String turn;
            if (Objects.equals(player1Tile, "O"))
            {
                player2Tile = "X";
                turn = "player1";
            }
            else
            {
                player2Tile = "O";
                turn = "player2";
            }
            while (true)
            {
                if (Objects.equals(turn, "player1"))
                {
                    game.drawBoard();
                    int[] curScore = new int[2];
                    curScore = game.getScore();
                    System.out.println("Player1 have " + curScore[1] + " Score");
                    System.out.println("Player2 have " + curScore[0] + " Score");
                    player1.getMove(game, player1Tile);
                    while (!(game.isValidMove(player1.getCurX(), player1.getCurY(), player1Tile)))
                    {
                        player1.getMove(game, player1Tile);
                    }
                    game.isValidMove(player1.getCurX(), player1.getCurY(), player1Tile);
                    game.makeMove(player1.getCurX(), player1.getCurY(), player1Tile);
                    if (game.areValidMoves(player2Tile))
                    {
                        turn = "player2";
                    }
                    else
                    {
                        break;
                    }
                }
                else
                {
                    game.drawBoard();
                    int[] curScore = new int[2];
                    curScore = game.getScore();
                    System.out.println("Player1 have " + curScore[1] + " Score");
                    System.out.println("Player2 have " + curScore[0] + " Score");
                    player2.getMove(game, player2Tile);
                    while (!(game.isValidMove(player2.getCurX(), player2.getCurY(), player2Tile)))
                    {
                        player2.getMove(game, player2Tile);
                    }
                    game.isValidMove(player2.getCurX(), player2.getCurY(), player2Tile);
                    game.makeMove(player2.getCurX(), player2.getCurY(), player2Tile);
                    if (game.areValidMoves(player1Tile))
                    {
                        turn = "player1";
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        System.out.println("Final BOARD:");
        game.drawBoard();
        int[] curScore = new int[2];
        curScore = game.getScore();
        System.out.println("Player1 have " + curScore[1] + " Score");
        System.out.println("Player2 have " + curScore[0] + " Score");
    }
}

