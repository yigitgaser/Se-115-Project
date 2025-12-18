
import javax.swing.*;
import java.io.*;
import java.util.*;
public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};


    static int[][][] allData = new int[MONTHS][DAYS][COMMS];

    public static void loadData() {
        for (int m = 0; m < MONTHS; m++) {
            String fileName = "Data_Files/" + months[m] + ".txt"; //Identifies files names
            try { //Because of java
                File file = new File(fileName); //Finds the file
                Scanner reader = new Scanner(file); //Reads the file
                while (reader.hasNext()) {
                    String line = reader.nextLine(); //Reads the lines
                    if (line.toLowerCase().startsWith("day") || line.trim().isEmpty()) {
                        continue;
                    } // Because of skipping data's first line
                    String[] data = line.split(",");//Divides the line by ,
                    int day = Integer.parseInt(data[0]) - 1;//Turns string to integer. Days stars from 1 but array starts from 0
                    int commIndex = -1;
                    switch (data[1]) {
                        case "Gold":
                            commIndex = 0;
                            break;
                        case "Oil":
                            commIndex = 1;
                            break;
                        case "Silver":
                            commIndex = 2;
                            break;
                        case "Wheat":
                            commIndex = 3;
                            break;
                        case "Copper":
                            commIndex = 4;
                            break;
                        default:
                            commIndex = -1; // If there is a mistake in data
                            break;
                    }
                    int profit = Integer.parseInt(data[2]); //Identifying the profit
                    if (day < DAYS && commIndex != -1) { //Assigning the profit to the array
                        allData[m][day][commIndex] = profit;
                    }
                }
                reader.close(); //Closing the file
            } catch (Exception e) {//Because of java
            }
        }

    }

    public static int getCommodityIndex(String name) { //Helper method.Because we will use commIndex in different methods
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(name)) {
                return i;
            }
        }
        return -1; //For invalid name
    }


    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >=MONTHS){ //If user gives wrong number
            return "INVALID_MONTH";
        }
        int maxProfit = -999999; // very small number for changing
        int bestComm = -1;
        for (int c=0; c<COMMS ; c++){  // Looping for each commoditie
            int currentProfit = 0;
            for(int d =0; d<DAYS; d++){  // Looping for each day for each commoditie
                currentProfit += allData[month][d][c];
            }
            if (currentProfit > maxProfit){ // Updating the variables
                maxProfit = currentProfit;
                bestComm = c;
            }
        }
        return commodities[bestComm] + " " + maxProfit; // Printing the wanting sentence
    }

    public static int totalProfitOnDay(int month, int day) {
        if(day<1 || day > DAYS || month<0 || month >= MONTHS){ //For invalid day or month
            return -99999;
        }
        int totalProfit = 0;
    for(int c=0 ; c<allData[month][day-1].length; c++){ //Looping the given month and day for commoditie
        totalProfit += allData[month][day-1][c]; //Changing the variable
    }
    return totalProfit;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        int commIndex = getCommodityIndex(commodity);//Using helper method for commIndex
        if(commIndex == -1 || from <1 || from >DAYS || to <1 || to>DAYS){ //For wrong input
            return -99999;
        }
        int totalProfit = 0;
        for(int m=0; m<MONTHS; m++){ //Looping the months for wanted commodity
            for( int d=from ;d <= to;d++) {//Looping the days in the months for wanted commodity
                totalProfit +=allData[m][d-1][commIndex];
            }
        }
        return totalProfit;
    }

    public static int bestDayOfMonth(int month) {
        if(month<0 || month>=MONTHS){ // For wrong input
            return -1;
        }
        int bestProfit = -999999; //Very wrong number for changing
        int bestDay = -1; // Invalid day for changing
        for (int d=0; d<DAYS ; d++){ //Looping days for wanted month
            int profit = 0; //Equaling 0 for each day
            for (int c=0; c<COMMS ; c++){ // Looping commodities for days
                profit += allData[month][d][c];
            }
            if (profit > bestProfit){
                bestProfit = profit; //Changing max profit
                bestDay = d+1; // Changing best day
            }
        }
        return bestDay;
    }
    public static String bestMonthForCommodity(String comm) {
        int commIndex = getCommodityIndex(comm); // Getting the commoditi index with helper.
        int bestProfit = -999999;// Very small number for changing
        String bestMonth ="";//Creating the month variable
        if(commIndex == -1){ //For invalid commoditie
            return "INVALID_COMMODITY";
        }
        for(int m=0; m< MONTHS ; m++){ //Looping for months
            int commProfit = 0; //Refreshing the sum
            for(int d=0;d<DAYS;d++){//Looping for days
                commProfit += allData[m][d][commIndex]; //Summing the wanting commodities profits
            }
            if (commProfit > bestProfit){ // Updating the variables
                bestProfit = commProfit ;
                bestMonth = months[m];
            }
        }
        return bestMonth+""+bestProfit;
    }
    public static int consecutiveLossDays(String comm) {
        int commIndex = getCommodityIndex(comm); // Getting the commoditi index with helper.
        int maxStreak = 0;     // For maximum loss streak
        int currentStreak = 0; // For refreshing the streak when it breaks
        if(commIndex == -1){ //For invalid commoditie
            return-1;
        }
        for (int m =0;m<MONTHS;m++){ //Looping for all months
            for(int d=0;d<DAYS;d++){ //Looping for all days
                if(allData[m][d][commIndex] < 0){
                    currentStreak++; //Counting the streak
                }
                else if (currentStreak>maxStreak) { //Updating the streak
                    maxStreak =currentStreak;
                }
                else{
                    currentStreak =0; //Refreshing the streak
                }

            }
        }
        return  maxStreak;
    }

   }