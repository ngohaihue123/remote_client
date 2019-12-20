/*
 * Author MINH SON
 */

package Client.Control;

import java.awt.Rectangle;
import java.awt.Robot;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class GoiManHinh extends Thread {

    Socket socket = null; 
    Robot robot = null; // Sử dụng để chụp màn hình
    Rectangle rectangle = null; // Sử dụng tính kích thước màn hình
    
    public GoiManHinh(Socket socket, Robot robot,Rectangle rect) {
        this.socket = socket;
        this.robot = robot;
        rectangle = rect;
        start();
    }
     public GoiManHinh(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
        start();
    }
    public void run(){
        ObjectOutputStream oos = null; 
        try{
            //Chuẩn bị gởi cho server
            oos = new ObjectOutputStream(socket.getOutputStream());
            if(rectangle != null){
                // Nếu sử dụng cho remote desktop thì truyền rectangle
                // để tính tọa độ chuột tương ứng
                oos.writeObject(rectangle);
            }
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
       ChupManHinh cScreenShot = new ChupManHinh(1.0f);
       while(true){
            // Gởi hình đã chụp cho server
            try {
                System.out.println("Truoc khi goi man hinh");
             
                oos.writeObject(cScreenShot.execute(robot));
                oos.flush();
                System.out.println("Man hinh da goi");
                // Dừng 200ms để dữ liệu kịp chuyển đến 
                // Trung bình 1/2 RTM = 170~200
                Thread.sleep(200);
            } catch (Exception ex) {
                break;
            }
        }

    }

}
