import java.io.*;
import java.util.ArrayList;
import  java.util.Scanner;
public class main {
    public static String orderDeterminer(String order){
        for (int i = 0; i < order.length(); i++) {
            if (order.charAt(i)==' ')return order.substring(0,i);
        }
        return order.trim();
    }
    public static String StringfirstInputOfOrder(String order){
        int spaceCounters = 0;
        int firstSpace = 0;
        for (int i = 0; i < order.length(); i++) {
            if (order.charAt(i)==' '&&spaceCounters==1){
                return order.substring(firstSpace+1,i);
            } else if (order.charAt(i)==' '&& spaceCounters == 0) {
                firstSpace = i;
                spaceCounters++;
            }
        }
        return order.substring(spaceCounters).trim();
    }
    public static String lastInput(String order){
        order = order.trim();
        for (int i = order.length()-1 ;i >=0 ; i--) {
            if (order.charAt(i)==' ')return order.substring(i+1);
        }
        return order;
    }

    public static void main(String[] args) {
        Scanner rev = new Scanner(System.in);
        boolean isItLoggedIn = false;
        account user = new account("temp" , "temp");
        String line;
        StringBuilder info = new StringBuilder();
        BufferedReader reader;
        try {
            File file = new File("DataOfUsers.txt");
            reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                info.append(line);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String userAndPass = info.toString();
        while (true){
            if (isItLoggedIn == false){
                System.out.println("**please login ir sign up**");
            }
            ArrayList<account> informationOfEachUser = jsonParser.jsonParsers(userAndPass);
            String input = rev.nextLine();
            String orderType = orderDeterminer(input);
            if (isItLoggedIn == false) {
                if (orderType.equals("login")){

                    String userName = StringfirstInputOfOrder(input);
                    String passWord = lastInput(input);
                    isItLoggedIn = account.login(userName,passWord,informationOfEachUser);
                    if (isItLoggedIn){
                        account temp = account.UserGiver(informationOfEachUser,userName);
                        user.userName = temp.userName;
                        user.balance = temp.balance;
                        user.passWord = temp.passWord;
                        user.ID = temp.ID;
                        user.history = temp.history;
                    }
                } else if (orderType.equals("signup")) {
                    String userName = StringfirstInputOfOrder(input);
                    String passWord = lastInput(input);
                    account.signUp(userName , passWord , informationOfEachUser);
                    user = informationOfEachUser.getLast();
                    isItLoggedIn = true;
                }else System.out.println("please enter sth valid");

            }else {
                user = informationOfEachUser.get(indexFinder(user , informationOfEachUser));
                if (orderType.equals("transfer")){
                    Transfer.transfer(user , StringfirstInputOfOrder(input).trim() , Double.parseDouble(lastInput(input)) , informationOfEachUser);
                } else if (orderType.equals("deposit")) {
                    deposit.deposit(user , Double.parseDouble(lastInput(input)) , informationOfEachUser);
                }else if (orderType.equals("withdraw")){
                    withdraw.withdraw(user , Double.parseDouble(lastInput(input)) , informationOfEachUser);
                }else if (orderType.equals("history")){
                    historyCommand.historyOfTheUser(user.history);
                }
                else if (orderType.equals("balance")) {
                    Balance.balanceGiver(user);
                }
                else if (orderType.equals("undo")) {
                   undo.undoGeneral(lastInput(input).trim() , informationOfEachUser , user);
                }
                else if (orderType.equals("return")) {
                    isItLoggedIn = false;
                } else {
                    System.out.println("please enter something valid");
                }

            }
            FileWriter DataWriter;
            userAndPass = jsonParser.Write(informationOfEachUser);
            try
            {
                DataWriter = new FileWriter("DataOfUsers.txt");
                BufferedWriter dataW = new BufferedWriter(DataWriter);

                dataW.write(userAndPass);
                dataW.flush();

                dataW.close();
            }
            catch (IOException except)
            {
                except.printStackTrace();
            }
        }
    }

    public static int indexFinder(account user , ArrayList<account> info){
        String name = user.userName;
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i).userName.equals(name))return i;
        }
        return 0;
    }
}
