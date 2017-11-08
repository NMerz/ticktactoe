package ticktactoe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

public class AI2 {
    BufferedWriter writer = null;
    DecimalFormat fourDecimal = new DecimalFormat("0.0000");
	double[][] weight = {{.1, .1, .1, .1, .1, .1, .1, .1, .1},
	{.1, .1, .1, .1, .1, .1, .1, .1, .1},
	{.1, .1, .1, .1, .1, .1, .1, .1, .1},
	{.1, .1, .1, .1, .1, .1, .1, .1, .1},
	{.1, .1, .1, .1, .1, .1, .1, .1, .1}};
	public AI2(){
			
	}
	public int aiMove(){
		Random rand = new Random();
		float randNum = rand.nextFloat();
		if(Game.log.size() <8){
		if(Game.log.size() %2 == 1){
			try {
				String fileName = "TTT";
				/*for (int y=0; y<Game.log.size(); y++){
					fileName += Game.log.get(y).toString();
				}*/
				for (int y = 1; y<=9; y++){
					if (Game.log.contains(y)){
						if (Game.log.indexOf(y)%2==0){
							fileName += "x";
						}else{
							fileName+= "o";
						}
					} else{
						fileName += "-";
					}
				}
				fileName += ".txt";
				BufferedReader brMove = new BufferedReader(new FileReader(fileName));
			    String line = brMove.readLine();
			    int counterCol = 0;
			    while (line != null) {
			    	counterCol = 0;
			    	while(line.contains(",")){
			    		if(line.substring(0, line.indexOf(',')
			    				).contains("E")){
			    			weight[Game.log.size()/2][counterCol] = Double.parseDouble(line.substring(0, line.indexOf('E'))) /1000;
			    					//(Math.pow(10, Double.parseDouble(line.substring(line.indexOf('E')+2, line.indexOf(',')))));
			    		}
			    		else {
			    			weight[Game.log.size()/2][counterCol] = Double.parseDouble(line.substring(0, line.indexOf(',')));
			    		}
			    		line = line.substring(line.indexOf(',')+1);
			    		counterCol++;
			    	}
			        line = brMove.readLine();
			    }
		    	brMove.close();
			} catch(FileNotFoundException e){
				localWeightSet(Game.log.size()/2);
			} catch (IOException e) {
				e.printStackTrace();
				localWeightSet(Game.log.size()/2);
			}
			for(int x = 0; x<weight[Game.log.size()/2].length; x++){
				randNum -= weight[Game.log.size()/2][x];
				if (randNum<=.01){
					return x+1;
				}
			}
		}
		} 
		return rand.nextInt(9)+1;
	}
	public void aiRewright(int layer, int place, double value){
		/*if (layer == 0){
			value *= .15;
		} else if (layer == 1){
			value *= .5;
		}*/
		double balanceValue = value/8;
		weight[layer][place-1] += value + balanceValue;
		for(int n = 0; n < weight[0].length; n++){
			weight[layer][n] -= balanceValue;
			if (weight[layer][n]<0.001){
				//weight[layer][highestWeight(layer)] += (weight[layer][n]-0.0)*-1;
				weight[layer][n] = 0.001;
			}
			if ((Game.log.indexOf(n+1) != -1) && (Game.log.indexOf(n+1) <(layer*2+1))){
				weight[layer][n] = 0;
			}
		}
		aiBalance(layer);
	}
	
	private void aiBalance(int layer){
		double sum = 0;
		for(int n = 0; n < weight[layer].length; n++){
			sum+= weight[layer][n];
		}
		double change = 1/sum;
		if (sum>1){
		}
		for(int n = 0; n < weight[layer].length; n++){
			weight[layer][n]*= change;
		}
	}
	private int highestWeight(int layer){
		int highest = 0;
		for(int n = 0; n < weight[layer].length; n++){
			if (weight[layer][n] > weight[layer][highest]){
				highest = n;
			}
		}
		return highest;
	}
	
	public void aiStore(int length){
		for(int x = 0; x<length;x++){
			String fileName = "TTT";
			for (int y = 1; y<=9; y++){
				if (Game.log.contains(y) && (Game.log.indexOf(y)<(x*2+1))){
					if (Game.log.indexOf(y)%2==0){
						fileName += "x";
					}else{
						fileName+= "o";
					}
				} else{
					fileName += "-";
				}
			}
			fileName += ".txt";
			File logFile = new File(fileName);
			try {
				writer = new BufferedWriter(new FileWriter(logFile));
				for (int n = 0; n<weight[x].length-1; n++ ){
					writer.write(weight[x][n]+",");
				}
				writer.write(weight[x][weight[x].length-1]+",");
				writer.write(";\n");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void localWeightSet(int x){
			for(int y = 0; y < weight[x].length; y++){
				weight[x][y] = .112;
		}
	}
}
