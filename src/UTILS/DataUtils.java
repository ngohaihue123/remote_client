package UTILS;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import Client.Control.ChupManHinh;

public class DataUtils {

    public static String nhanDuLieu(Socket sender) {
        try {
            BufferedReader inputServer =
                    new BufferedReader(new InputStreamReader(sender.getInputStream()));
            String msg = inputServer.readLine();
            System.out.println("Nhan may "
                    + sender.getInetAddress().getHostAddress()+":"+sender.getPort()
                    + " > "+ msg);
            return msg;
        } catch (IOException ex) {
            return "";
        }
    }

    public static boolean goiDuLieu(Socket receiver, String msg) {
        PrintWriter outputToClient;
        try {
            outputToClient = new PrintWriter(receiver.getOutputStream(), true);
            outputToClient.println(msg);
            System.out.println("Sended "
                    + receiver.getInetAddress().getHostAddress()+":"+receiver.getPort()
                    + " > "+msg);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public static String layTenMay(Socket socket) {
        return socket.getInetAddress().getHostName();
    }

    public static String layIPMay(Socket socket) {
        return socket.getInetAddress().getHostAddress();
    }
    public static boolean checkConnectClosed(Socket socket){
        PrintWriter out;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.print("");
            return out.checkError();
        } catch (IOException ex) {
        }
        return true;
    }
}
