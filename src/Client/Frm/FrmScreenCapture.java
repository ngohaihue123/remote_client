package Client.Frm;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Client.Control.ChupManHinh;

/**
 *
 * @author MINH SON
 */
public class FrmScreenCapture{
    
    Socket socket;
    ObjectOutputStream oos;
    ChupManHinh cScreenShot;
    public FrmScreenCapture(Socket socket) {
        this.socket = socket;
    }

    public void goiAnh() {
        try {
          cScreenShot = new ChupManHinh(1.0f);
          oos =  new ObjectOutputStream(socket.getOutputStream());
          // Lấy màn hình mặc định của hệ thống
          GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
          GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
          
          // Chuẩn bị robot thao tác màn hình
          Robot robot = new Robot(gDev);
          oos.writeObject(cScreenShot.execute(robot));
          oos.flush();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
}
