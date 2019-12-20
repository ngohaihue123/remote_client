/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Frm;

import Entity.*;
import UTILS.DataUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import Client.Control.GoiManHinh;
import Client.Control.ThaoTacManHinh;

public class FrmClientGUI extends JFrame implements Runnable {

    Socket socketFromServer;
    FrmChatVoiServer dialogChatServer;
    boolean continueThread = true;
    String ipServer;
    final int mainPortServer = 999;
    Socket socketNhanFile;
    FrmScreenCapture screenCapture;
    
    public FrmClientGUI() {
        try {
            initComponents();
            frmRemoteDesktop.setVisible(true);
            ipServer = txtIP.getText();
            lblClientName.setText(InetAddress.getLocalHost().getHostName()
                    + " (" + InetAddress.getLocalHost().getHostAddress() + ")");
            lblStatus.setText("Kết nối để truy cập vào hệ thống...");
        } catch (Exception ex) {
        }
    }

    public void run() {
        while (continueThread) {
            try {
                String msg = DataUtils.nhanDuLieu(socketFromServer);
                if (msg != null && !msg.isEmpty()) {
                    xuLyDuLieuTrungTam(msg);
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }
    //<editor-fold defaultstate="collapsed" desc="Xử lý dữ liệu trung tâm">

    private void xuLyDuLieuTrungTam(String msg) throws UnknownHostException, IOException {
        PacketTin pkTin = new PacketTin();
        pkTin.phanTichMessage(msg);
        // Thực hiện các câu lệnh từ Server
        if (pkTin.isId(PacketChat.ID)) {
            if (dialogChatServer == null) {
                // Khởi tạo
            	String name = textField_1.getText();
                dialogChatServer = new FrmChatVoiServer(socketFromServer,name);
            }
            // Gởi dữ liệu đã phân tích
            dialogChatServer.nhanDuLieu(pkTin.getCmd(), pkTin.getMessage().toString());
            if (!dialogChatServer.isVisible()) {
                dialogChatServer.setVisible(true);
            }
        } else if (pkTin.isId(PacketTruyenFile.ID)) {
            int port = Integer.parseInt(pkTin.getMessage().toString());
            // Tạo socket nhận file với port đã gởi qua
            socketNhanFile = new Socket(ipServer, port);
            xuLyNhanFile();
        } else if (pkTin.isId(PacketRemoteDesktop.ID)) {
            xuLyRemoteDesktop(pkTin);
        }
        System.err.println(pkTin.toString());
    }
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="Remote desktop">
    private void xuLyRemoteDesktop(PacketTin pkTin) {
        int port = Integer.parseInt(pkTin.getMessage().toString());
        // Dùng để xử lý màn hình
        Robot robot;
        // Dùng dể tính độ phân giải và kích thước màn hình cho client
        Rectangle rectangle;
        try {
            // Tạo socket nhận remote với port đã gởi qua
            final Socket socketRemote =
                    new Socket(ipServer, port);
            try {
                // Lấy màn hình mặc định của hệ thống
                GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gDev = gEnv.getDefaultScreenDevice();

                // Lấy dimension màn hình
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                rectangle = new Rectangle(dim);

                // Chuẩn bị robot thao tác màn hình
                robot = new Robot(gDev);

                // Gởi màn hình
                new GoiManHinh(socketRemote, robot, rectangle);
                // Gởi event chuột/phím thao tác màn hình
                new ThaoTacManHinh(socketRemote, robot);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="Xử lý nhận file">
    private void xuLyNhanFile() throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showSaveDialog(null) != JOptionPane.OK_OPTION) {
            return;
        }
        int bytesRead;
        InputStream in = socketNhanFile.getInputStream();
        DataInputStream clientData = new DataInputStream(in);
        System.err.println("C[Nhận File]: bắt đầu chờ nhận file....");
        String fileName = clientData.readUTF();
        System.err.println("C[Nhận File]: đã thấy tên file: " + fileName);
        String fullPath = chooser.getSelectedFile().getPath() + "\\" + fileName;
         try {
            OutputStream output = new FileOutputStream(fullPath);
     
            System.err.println("C[Nhận File]: bắt đầu nhận file: " + fileName);
            long size = clientData.readLong();
            byte[] buffer = new byte[3024];
            while (size > 0 && (bytesRead =
                    clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            output.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.err.println("C[Nhận File]: đã nhận xong: " + fileName);
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	
    	lblClientName = new JLabel();
    	textField_1 = new JTextField();
    	lblStatus4 = new JLabel();
        lblClientName = new JLabel();
        lblStatus1 = new JLabel();
        lblIPAddress = new JLabel();
        lblStatus2 = new JLabel();
        lblStatus = new JLabel();
        btnThoat = new JToggleButton();
        jLabel1 = new JLabel();
        txtIP = new JTextField();
        jButtonConnect = new javax.swing.JButton();
        
		frmRemoteDesktop = new JFrame();
		frmRemoteDesktop.getContentPane().setBackground(SystemColor.menu);
		frmRemoteDesktop.setTitle("Remote Desktop Client");
		frmRemoteDesktop.setIconImage(Toolkit.getDefaultToolkit().getImage(FrmClientGUI.class.getResource("/RES/chat-client.png")));
		frmRemoteDesktop.setBounds(100, 100, 494, 315);
		frmRemoteDesktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRemoteDesktop.getContentPane().setLayout(null);

        
        //
		JEditorPane dtrpnSsadsaddsaD = new JEditorPane();
		dtrpnSsadsaddsaD.setForeground(Color.BLACK);
		dtrpnSsadsaddsaD.setEditable(false);
		dtrpnSsadsaddsaD.setBackground(SystemColor.menu);
		dtrpnSsadsaddsaD.setFont(new Font("Tahoma", Font.PLAIN, 13));
		dtrpnSsadsaddsaD.setToolTipText("");
		dtrpnSsadsaddsaD.setText("\u0110\u1EC1 t\u00E0i\t:\tRemote Desktop\r\nGVHD\t:\tThs. Lê Minh Tuấn\r\nSVTH\t:\tNgô Hải Huế- 15T13");
		dtrpnSsadsaddsaD.setBounds(10, 46, 458, 54);
		frmRemoteDesktop.getContentPane().add(dtrpnSsadsaddsaD);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 111, 478, 2);
		frmRemoteDesktop.getContentPane().add(separator);

		//
        lblStatus4.setText("Name");
        lblStatus4.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblStatus4.setBounds(70, 131, 63, 14);
		frmRemoteDesktop.getContentPane().add(lblStatus4);

		
		textField_1.setColumns(10);
		textField_1.setBounds(157, 125, 140, 21);
		frmRemoteDesktop.getContentPane().add(textField_1);

        lblStatus1.setText("My IP");
		lblStatus1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblStatus1.setBounds(70, 196, 63, 14);
		frmRemoteDesktop.getContentPane().add(lblStatus1);

		
		lblClientName.setForeground(new Color(0, 0, 255));
		lblClientName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblClientName.setBounds(157, 196, 254, 14);
		frmRemoteDesktop.getContentPane().add(lblClientName);

        lblStatus2.setText("Trạng thái");
        lblStatus2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblStatus2.setBounds(70, 221, 63, 14);
		frmRemoteDesktop.getContentPane().add(lblStatus2);
		
		// ?
		lblStatus.setToolTipText("");
		lblStatus.setForeground(new Color(0, 0, 255));
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblStatus.setBounds(157, 221, 254, 14);
        lblStatus.setText("Status");
        frmRemoteDesktop.getContentPane().add(lblStatus);
        
        JLabel lblNewLabel_2 = new JLabel("\u0110\u1ED2 \u00C1N L\u1EACP TR\u00CCNH M\u1EA0NG");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 11, 478, 24);
		frmRemoteDesktop.getContentPane().add(lblNewLabel_2);

        //
        jLabel1.setText("IP Server:");
        jLabel1.setFont(new Font("Tahoma", Font.BOLD, 11));
		jLabel1.setBounds(70, 160, 63, 14);
		frmRemoteDesktop.getContentPane().add(jLabel1);
		//

        txtIP.setText("127.0.0.1");
        txtIP.setBounds(157, 157, 140, 21);
		frmRemoteDesktop.getContentPane().add(txtIP);
		txtIP.setColumns(10);
        //
        jButtonConnect.setText("Kết nối");
        jButtonConnect.setBounds(322, 125, 89, 54);
        jButtonConnect.addActionListener(new ActionListener() {
			
			@Override
            public void actionPerformed(ActionEvent evt) {
                jButtonConnectActionPerformed(evt);
            }
        });
        frmRemoteDesktop.getContentPane().add(jButtonConnect);
		//
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonConnectActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonConnectActionPerformed
        ipServer = txtIP.getText();
        System.out.println(ipServer);
        try {
            // Khởi tạo kết nối từ Client đến Server
            lblStatus.setText("Đang chờ kết nối đến server...");
            socketFromServer = new Socket(ipServer, mainPortServer);
            DataOutputStream dos = new DataOutputStream(socketFromServer.getOutputStream());
            dos.writeUTF(textField_1.getText());
            lblStatus.setText("Đã kết nối server thành công.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối với server!");
            lblStatus.setText("Kết nối để truy cập vào hệ thống...");
        }
    }//GEN-LAST:event_jButtonConnectActionPerformed

    private void formWindowClosed(WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        continueThread = false;
    }//GEN-LAST:event_formWindowClosed

    private void btnThoatActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        dispose();
    }//GEN-LAST:event_btnThoatActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JFrame frmRemoteDesktop;
	private JTextField textField;
	private JTextField textField_1;
    
    private JToggleButton btnThoat;
    private JButton jButtonConnect;
    private JLabel jLabel1;
    private JLabel lblClientName;
    private JLabel lblIPAddress;
    private JLabel lblStatus;
    private JLabel lblStatus1;
    private JLabel lblStatus2;
    private JLabel lblStatus4;
    private JTextField txtIP;
    // End of variables declaration//GEN-END:variables
}
