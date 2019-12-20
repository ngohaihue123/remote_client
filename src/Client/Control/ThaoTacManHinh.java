/*
 * Author MINH SON
 */
package Client.Control;

import java.awt.Robot;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class ThaoTacManHinh extends Thread {

    Socket socket = null;
    // robot dùng để thao tác với các sự kiện chuột và phím
    Robot robot = null; 
    
    public ThaoTacManHinh(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
        start();
    }

    @Override
    public void run(){
        Scanner scanner;
        try {

            scanner = new Scanner(socket.getInputStream());
            while(true){
                // Chờ lệnh trả về từ server
                System.out.println("Dang cho thao tac");
                int command = scanner.nextInt();
                System.out.println("Thao tac: " + command);
                switch(command){
                    case -1:
                        robot.mousePress(scanner.nextInt());
                    break;
                    case -2:
                        robot.mouseRelease(scanner.nextInt());
                    break;
                    case -3:
                        robot.keyPress(scanner.nextInt());
                    break;
                    case -4:
                        robot.keyRelease(scanner.nextInt());
                    break;
                    case -5:
                        robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
