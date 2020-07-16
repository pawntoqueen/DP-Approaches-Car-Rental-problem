
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class _2017510070_Gaye_Suner {


    public static int findMax(int[][] arr){
        int max = Integer.MIN_VALUE;
        for (int i = 0; i <arr.length ; i++) {
            for (int j = 0; j <arr[i].length ; j++) {
                if(arr[i][j]>max) max = arr[i][j];
            }
        }
        return max;
    }

    public static int findMax(int[] arr){
        int max = Integer.MIN_VALUE;
        for (int i = 0; i <arr.length ; i++) {
            if((arr[i] > max)){
                max = arr[i];
            }
        }
        return max;
    }

    public static int findIndex(int[] arr){
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i <arr.length ; i++) {
            if(arr[i] > max){
                max = arr[i];
                index = i;
            }
        }
        return index;
    }

    public static int[] findMonthlyIncome(int B, int[] monthly_demand ){
        int[] monthlyIncome = new int[monthly_demand.length + 1];
        int endOfTheMonthPrice = 0;
        for (int i = 0; i <monthly_demand.length ; i++) {
            monthlyIncome[i] = (int) (endOfTheMonthPrice + ((double)monthly_demand[i]*B/2));
            endOfTheMonthPrice = (monthly_demand[i]*B)/2;
        }
        monthlyIncome[monthlyIncome.length-1] = endOfTheMonthPrice;
        return monthlyIncome;
    }



    public static void main(String[] args) throws IOException {
        int p = 6, d=6, x= 5, t = 2, B=100, c = 6;
        //int p = 7, d=5, x= 20, t = 1, B=50, c = 4;
        //int p = 6, d=5, x= 45, t = 1, B=75,  c = 6;
        //int p = 5, d=5, x= 30, t = 3, B=150, c = 5;
        //int p = 2, d=2, x= 25, t = 2, B=100, c = 6;
        int limX=0 ;

        int[] monthly_demand = new int[x];
        int[][] monthly_offer = new int[x][c];
        int[] garage_costs = new int[310];


        FileReader demand_reader = new FileReader("month_demand.txt");
        String lineD;

        BufferedReader demand = new BufferedReader(demand_reader);
        demand.readLine();
        while ((lineD = demand.readLine()) != null) {
            String[] demands = lineD.split("\t");
            monthly_demand[limX++] = Integer.parseInt(demands[1]);
            if(limX == x) break;

        }
        demand.close();

        limX=0;
        FileReader fileReader = new FileReader("investment.txt");
        String lineI;
        BufferedReader invest = new BufferedReader(fileReader);
        invest.readLine();
        while ((lineI = invest.readLine()) != null) {

            String[] offers = lineI.split("\t");
            for (int i = 1; i <c+1 ; i++) {
                monthly_offer[limX][i-1]=Integer.parseInt(offers[i]);
            }
            limX ++;
            if(limX == x) break;
        }

        invest.close();


        limX=0;
        FileReader garageReader = new FileReader("garage_cost.txt");
        String lineG;
        BufferedReader garage = new BufferedReader(garageReader);
        garage.readLine();
        while ((lineG = garage.readLine()) != null) {
            String[] costs = lineG.split("\t");
            garage_costs[limX++] = Integer.parseInt(costs[1]);
        }
        garage.close();


        int DPprofit = DPProfit(monthly_demand,B,monthly_offer,t);
        int Greedy_profit = GreedyProfit(monthly_demand,B,monthly_offer,t);
        int DPCost = DpCost(monthly_demand,p,garage_costs,d,x);
        int Greedy_cost = GreedyCost(monthly_demand,p,garage_costs,d,x);
        System.out.println("DP Result: " + (DPprofit - DPCost));
        System.out.println("Greedy Result: " + (Greedy_profit - Greedy_cost));

    }

    private static int GreedyCost(int[] monthly_demand, int p, int[] garage_costs, int d, int x) {
        int size =0;
        for (int i = 0; i < x; i++) {
            size += monthly_demand[i];
        }
        int[][] processTable = new int[x][size];
        int[] cars_cost = new int[size];
        int vallet[]  = new int[x+1];
        Arrays.fill(vallet,0);

        for (int i = 1; i < cars_cost.length ; i++) {
            cars_cost[i] = garage_costs[i-1];
        }

        for (int i = 0; i < x; i++) {
            Arrays.fill(processTable[i],0);
        }
        int intern_cost = 0;
        int capacity = size;

        for (int i = 0; i < x; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j < size ; j++) {
               if(monthly_demand[i] >=  p){
                   if(capacity!=0){
                       for (int k = 0; k <= capacity; k++) {
                           intern_cost = vallet[i] + cars_cost[k] + (monthly_demand[i]-p-k) * d ;
                           if(intern_cost < min && intern_cost >= 0){
                               min = intern_cost;
                           }
                           if (monthly_demand[i]-p==k) break;
                       }
                       vallet[i+1]= min;
                       capacity = 0;
                       break;
                   }
                   else{
                       vallet[i+1] = vallet[i]+ (monthly_demand[i]-p) * d;
                   }
               }
               else {
                       vallet[i+1] = vallet[i];
                       capacity = p-monthly_demand[i];
               }
            }

        }

        return vallet[vallet.length-1];
    }


    private static int DpCost(int[] monthly_demand, int p, int[] garage_costs, int d,int x) {
        int size =0;
        for (int i = 0; i < x; i++) {
            size += monthly_demand[i];
        }
        int[][] processTable = new int[x+1][size];

        for (int i = 0; i < x+1; i++) {
            Arrays.fill(processTable[i],0);
        }
        int intern_cost = 0;
        for (int i = 0; i < x+1; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j < size ; j++) {
                if(i == 0){
                    processTable[i][j] = garage_costs[j-1];
                }
                else{
                    if (monthly_demand[i-1]-p>=0){
                        for (int k = 0; k <= monthly_demand[i-1]-p ; k++) {
                            if (k == 0){
                                intern_cost = processTable[i-1][0] + (monthly_demand[i-1]-p-k) * d ;
                            }
                            else if(processTable[i-1][k] != 0)
                            {
                                intern_cost =processTable[i-1][0]+ (monthly_demand[i-1]-p-k) * d + processTable[i-1][k];
                            }
                            if (intern_cost < min){
                                min = intern_cost;
                            }
                        }
                        processTable[i][0]=min;
                    }
                    else{
                        processTable[i][0]= processTable[i-1][0];
                        int capacity = Math.abs(monthly_demand[i-1]-p);
                        for (int k = 1; k <= capacity; k++) {
                            processTable[i][k] =  processTable[0][k];
                        }
                    }
                }

            }

        }

        return (processTable[processTable.length-1][0]);
    }



    public static int GreedyProfit(int[] monthly_demand,int B, int[][] monthly_offer, int t ){
        String[][] bestBanks = new String[monthly_offer.length][monthly_offer[0].length];
        int[][] bestIncome = new int[monthly_offer.length][monthly_offer[0].length];
        int[] monthlyIncome = findMonthlyIncome( B,  monthly_demand );


        for (int i = 0; i <bestBanks.length ; i++) {
            Arrays.fill(bestBanks[i],"");
        }

        for (int i = 0; i < bestIncome.length ; i++) {
            for (int j = 0; j < bestIncome[i].length ; j++) {
                if(i==0){
                    bestIncome[i][j]  = (int) (monthlyIncome[i]*((double)monthly_offer[i][j] / 100) + monthlyIncome[i]);
                }else{

                    double rawMoneyAtBeginningMonth, moneyTransferredFromPreviousMonth;
                    double MaxMoneyPreviousMonth = findMax(bestIncome[i-1]);

                    if(findIndex(bestIncome[i-1])==j){
                        moneyTransferredFromPreviousMonth = MaxMoneyPreviousMonth;
                    }
                    else{
                        moneyTransferredFromPreviousMonth = MaxMoneyPreviousMonth - MaxMoneyPreviousMonth* (double)t/100 ;
                    }

                    rawMoneyAtBeginningMonth = monthlyIncome[i] + moneyTransferredFromPreviousMonth ;

                    bestIncome[i][j] = (int) (rawMoneyAtBeginningMonth  + rawMoneyAtBeginningMonth *((double)monthly_offer[i][j] / 100));
                }

            }
        }


        return findMax(bestIncome[bestIncome.length-1])+monthlyIncome[monthlyIncome.length-1];
    }




    public static int DPProfit(int[] monthly_demand,int B, int[][] monthly_offer, int t ){


        String[][] bestBanks = new String[monthly_offer.length][monthly_offer[0].length];
        int[][] bestIncome = new int[monthly_offer.length][monthly_offer[0].length];
        int[] monthlyIncome = findMonthlyIncome( B,  monthly_demand );



        for (int i = 0; i <bestBanks.length ; i++) {
            Arrays.fill(bestBanks[i],"");
        }


        for (int i = 0; i < bestIncome.length ; i++) {
            for (int j = 0; j < bestIncome[i].length ; j++) {
                if(i==0){
                    bestIncome[i][j]  = (int) (monthlyIncome[i]*((double)monthly_offer[i][j] / 100) + monthlyIncome[i]);
                }else{
                    double max = Integer.MIN_VALUE;
                    double possibleIncomeAtEndOfTheMonth, rawMoneyAtBeginningMonth, moneyTransferredFromPreviousMonth;
                    for (int k = 0; k <bestIncome[i].length ; k++) {
                        if(k==j){
                            moneyTransferredFromPreviousMonth = bestIncome[i-1][j];
                        }
                        else{
                            moneyTransferredFromPreviousMonth = bestIncome[i-1][k] - bestIncome[i-1][k] * (double)t/100 ;
                        }

                        rawMoneyAtBeginningMonth = monthlyIncome[i] + moneyTransferredFromPreviousMonth ;
                        possibleIncomeAtEndOfTheMonth  =   rawMoneyAtBeginningMonth  + rawMoneyAtBeginningMonth *((double)monthly_offer[i][j] / 100) ;

                        if(possibleIncomeAtEndOfTheMonth >= max){
                            max = possibleIncomeAtEndOfTheMonth;
                        }
                    }
                    bestIncome[i][j] = (int) max;
                }
            }
        }

        return findMax(bestIncome)+monthlyIncome[monthlyIncome.length-1];
    }


}
