package client;

import java.rmi.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String keyboard = "";
        Client c;
        try {
            while (true) {
                keyboard = scan.nextLine();
                if (!keyboard.equals("exit")){
                    c = new Client();
                    c.run(keyboard);
                } else {
                    System.out.println("> Client closing");
                    System.exit(1);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
