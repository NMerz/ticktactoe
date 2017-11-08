package ticktactoe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
	static ArrayList<Integer> log = new ArrayList();
	public static void main(String[] args) {
		int gameCount = 0;
		AI TTTAI = new AI();
		AI2 TTTAI2 = new AI2();
	    DecimalFormat fourDecimal = new DecimalFormat("0.0000");
		boolean again = true;
		Scanner test = new Scanner(System.in);
		int move;
		int moveY;
		int moveX;
		String nextLine = null;
		while (again == true){
			log.clear();
			TicTacToe game = new TicTacToe();
			//game.printBoard();
			double reweightValue = .05;
			boolean won = false;
			while (game.checkForWin()==false && game.isBoardFull() == false){
				//System.out.println(log);
				if(game.getCurrentPlayerMark() == 'x'){
					/*System.out.println("Next move:");
					nextLine = test.next();
					move = Integer.parseInt(nextLine);*/
					move = TTTAI.aiMove();
				} else{

					move = TTTAI2.aiMove();
				}
				moveY = (move-1)/3;
				moveX = (move-1)%3;
				if (game.placeMark(moveY, moveX) == true) {
					log.add(move);
					
					//game.printBoard();//search anchor
					//test.next();
					game.changePlayer();
				}
			}
			if (game.checkForWin()) {
				//test.next();
				//System.out.println("We have a winner! Congrats" + game.getWinnerMark() + "!");
				//if (game.getWinnerMark() == 'o'){
					System.out.println(game.getWinnerMark());
					reweightValue *= -1;
				//}
				won = true;
			}
			else if (game.isBoardFull()) {
				//System.out.println("Appears we have a draw!");
				//reweightValue = -.03 ;
				reweightValue = 0;
				won = true;
			}
			if (won==true){
				boolean logLengthDecreased = false;
				int logLength = log.size();
				if(logLength%2 == 0){
					logLengthDecreased = true;
					logLength--;
				}
				for(int n = 0; n<logLength/2; n++){
					TTTAI.aiRewright(n, (int) log.get(n*2), reweightValue);
					TTTAI2.aiRewright(n, (int) log.get(n*2+1), reweightValue*-1); // not working correctly start with redoing 1
				}
			//weightPrint(TTTAI, fourDecimal);
			//weightPrint(TTTAI2, fourDecimal);
				TTTAI.aiRewright(logLength/2, (int) log.get(((int) (logLength/2))*2), reweightValue);
				if (logLengthDecreased == true){
					TTTAI2.aiRewright(logLength/2, (int) log.get(((int) (logLength/2))*2+1), reweightValue*-1);
				}
				
				
				TTTAI.aiStore(logLength/2);//search anchor
				if (logLengthDecreased == true){
					logLength++;
				}
				TTTAI2.aiStore(logLength/2);
				
				
				//weightPrint(TTTAI, fourDecimal);//prints final weight
				//System.out.println("Play again?");
				//System.out.println("Playing again");
				/*nextLine = test.next();
				if (nextLine.contains("n")){
					again = false;
				}*/
			}
			gameCount++;
			if (gameCount%30000 == 0){
				System.out.println("continue?");
				test.next();
			}
		}
	}

	public static void weightPrint(AI aiToPrint, DecimalFormat decFormat) {
		for (int i = 0; i<aiToPrint.weight.length; i++){
			for (int n = 0; n<aiToPrint.weight[i].length; n++ ){
				System.out.print(n + "," + decFormat.format(aiToPrint.weight[i][n])+ " | ");
			}
			System.out.println();
		}
	}
	public static void weightPrint(AI2 aiToPrint, DecimalFormat decFormat) {
		for (int i = 0; i<aiToPrint.weight.length; i++){
			for (int n = 0; n<aiToPrint.weight[i].length; n++ ){
				System.out.print(n + "," + decFormat.format(aiToPrint.weight[i][n])+ " | ");
			}
			System.out.println();
		}
	}
}
