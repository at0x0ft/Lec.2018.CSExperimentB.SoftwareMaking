import java.util.function.Predicate;

@FunctionalInterface
public interface IPredicate<T> {
    boolean judge(T t);
}

public class Filter {
    public static String inputLoop(List<String>) {
        String check = null;
        while(true) {
            System.out.print("Is it Correct? Enter y/n. : ");
            
            try {
                check = LoveLetter.cInputLn();
                if(check.equals("y") || check.equals("n")) {
                    return check;
                }
            }
            catch (IOException ioe) {
                System.out.println("IOError Occured.");
                System.out.println("Please enter again...");
                continue;
            }
            System.out.println("Wrong character! Please enter the correct ones.");
        }
    }
}