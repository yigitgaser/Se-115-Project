
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
                while (reader.hasNextLine()) {
                    String line = reader.nextLine(); //Reads the lines
                    if (line.toLowerCase().startsWith("day") || line.trim().isEmpty()) {
                        continue;
                    } // Because of skipping data's first line
                    String[] data = line.split(",");//Divides the line by ,
                    if (data.length < 3) continue; // If date is missing it continues
                    String dayStr = data[0].trim(); // If there is a space in data
                    String commStr = data[1].trim();
                    String profitStr = data[2].trim();
                   int commIndex = -1;
                   int day =  Integer.parseInt(dayStr) - 1;
                    switch (commStr) {
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
                    int profit = Integer.parseInt(profitStr); //Identifying the profit
                    if (day >= 0 && day < DAYS && commIndex != -1) {
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
        int maxProfit = Integer.MIN_VALUE;
        int bestComm = 0;
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
        if(commIndex == -1 || from <1 || from >DAYS || to <1 || to>DAYS || from >to){ //For wrong input
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
        int bestProfit = Integer.MIN_VALUE;
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
        int bestProfit = Integer.MIN_VALUE;
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
        return bestMonth;
    }
    public static int consecutiveLossDays(String comm) {
        int commIndex = getCommodityIndex(comm); // Getting the commoditi index with helper.
        int maxStreak = 0;     // For maximum loss streak
        int currentStreak = 0; // For refreshing the streak when it breaks
        if(commIndex == -1){ //For invalid commoditie
            return-1;
        }
        for (int m = 0; m < MONTHS; m++) { //Looping for every month
            for (int d = 0; d < DAYS; d++) { // Looping for all days
                if (allData[m][d][commIndex] < 0) { // Counting the lost days
                    currentStreak++;
                } else {
                    if (currentStreak > maxStreak) maxStreak = currentStreak; //If it is not lost days , update the max streak and refresh the streak
                    currentStreak = 0;
                }
            }
        }
        if (currentStreak > maxStreak) maxStreak = currentStreak; //Updating the max streak if there is no gain days

        return maxStreak;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        int commIndex = getCommodityIndex(comm); // Getting the commoditi index with helper.
        if(commIndex == -1){ //For invalid commoditie
            return-1;
        }
        int daysAbove =0;
        for(int m =0; m<MONTHS;m++){ //Looping for all months
            for(int d =0;d<DAYS;d++){  //Looping for all days
                if (allData[m][d][commIndex] > threshold){
                    daysAbove ++; //Counting the days
                }
            }
        }
        return daysAbove;
    }


    public static int biggestDailySwing(int month) {

        if (month < 0 || month >= MONTHS) { //For invalid month
            return -99999;
        }

        int maxSwing = 0;
        for (int d = 1; d < DAYS; d++) { //Looping the days
            int todayTotal = 0;
            int prevTotal = 0;
            for (int c = 0; c < COMMS; c++) {
                prevTotal += allData[month][d-1][c]; // Calculating the previous days profit
                todayTotal += allData[month][d][c]; //Calculating the current days profit
            }

            int diff = todayTotal - prevTotal;
            if (diff < 0) diff = -diff; // For absolute

            if (diff > maxSwing) maxSwing = diff; // Updating the max swing

        }

        return maxSwing;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        int commIndex1 = getCommodityIndex(c1); // Getting the indexes with helper
        int commIndex2 = getCommodityIndex(c2);
        if(commIndex1==-1 || commIndex2 ==-1){ // For invalid index
            return "INVALID_COMMODITY";
        }
        int profit1 =0;
        int profit2 = 0;
        for(int m=0; m<MONTHS; m++){ //looping all months
            for(int d =0; d<DAYS; d++){ //Looping all days
                profit1 += allData[m][d][commIndex1]; //Calculating profits
                profit2 += allData[m][d][commIndex2];
            }
        }
    if(profit1 > profit2){ // Comparing the profits
     int diff = profit1 -profit2;
     return c1 +" is better by "+diff;
    } else if ( profit1 < profit2) {
        int diff = profit2 -profit1;
        return c2 +" is better by " + diff;
    }
    else{
        return "Equal";
    }

    }

    public static String bestWeekOfMonth(int month) {
        if(month <0 || month>=12){ // For invalid month
            return "INVALID_MONTH";
        }
        int maxProfit = Integer.MIN_VALUE;
        int bestWeek = -1; //Invalid week for changing
        for(int w =1; w <5;w++){ //Looping the weeks
            int startDay =(w-1) *7; //Calculating the weeks starting days (0,7,14,21)
            int weakProfit =0;
            for(int d = startDay;d <startDay + 7;d++){ //Looping the  days that in the weeks
                for(int c =0; c<COMMS ; c++){ //Looping the commodities
                    weakProfit += allData[month][d][c]; //Calculating the weeks profit
                }
            }
            if(weakProfit > maxProfit){ //Updating the variables
                maxProfit = weakProfit;
                bestWeek = w;
            }
        }
        return "Week " + bestWeek;
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
   }