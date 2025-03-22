import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Transfer extends TransActions{
    public static ArrayList<account> transfer(account Sender , String getter , double transferableMoney , ArrayList<account> info){
        if (getter.equals(Sender.userName)){
            System.out.println("the destination is invalid");
            return info;
        }
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i).userName.equals(Sender.userName)){
                info.remove(i);
                break;
            }
        }
        boolean isThereGetter = false;
        account personWhoGet = Sender;
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i).userName.equals(getter)){
                personWhoGet = info.get(i);
                info.remove(i);
                isThereGetter = true;
                break;
            }
        }
        if (!isThereGetter){
            info.add(Sender);
            System.out.println( getter + " dose not exist");
            return info;
        }
        if (transferableMoney > Sender.balance){
            info.add(Sender);
            info.add(personWhoGet);
            System.out.println("you don't have enough money.");
        }else {
            String id = withdraw.givingId(info);
            Sender.balance -= transferableMoney;
            Sender.history.add(TransferHistory(-transferableMoney,getter , id));
            personWhoGet.balance += transferableMoney;
            personWhoGet.history.add(TransferHistory(transferableMoney , "your account" , id));
            info.add(Sender);
            info.add(personWhoGet);
            System.out.println(String.valueOf(transferableMoney) + " transfered to " + getter);
        }
        return info;
    }
    static history TransferHistory(double mountOfMoney , String getter , String id){
        history h  = new history();
        LocalDateTime DateAndTime = LocalDateTime.now();
        DateTimeFormatter patternOfDate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String time = DateAndTime.format(patternOfDate);
        h.date = time;
        h.mountOfMoneyInvolved = mountOfMoney;
        h.toWho = getter;
        h.typeOfTransAction = "transfer";
        h.IdOt = id;

        return h;
    }

    public static int indexFinder(account user , ArrayList<account> info){
        String name = user.userName;
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i).userName.equals(name))return i;
        }
        return 0;
    }

    public static int indexFinderHistory(history History , ArrayList<history> info){
        String IDoT = History.IdOt;
        for (int i = 0; i < info.size(); i++) {
            if (info.get(i).IdOt.equals(IDoT))return i;
        }
        return 0;
    }
    public static ArrayList<account> undo(account user , account getter , double mountOfMoney , history Transaction ,ArrayList<account> info){

        info.remove(indexFinder(getter , info));
        info.remove(indexFinder(user , info));
        getter.balance -= mountOfMoney;
        user.balance+=mountOfMoney;
        getter.history.remove(indexFinderHistory(Transaction , getter.history));
        user.history.remove(indexFinderHistory(Transaction , user.history));
        info.add(user);
        info.add(getter);
        System.out.println("you have canceled the transaction");
        return info;
    }
}
