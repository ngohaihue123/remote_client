package Entity;

import UTILS.DataUtils;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ComputerTableModel extends AbstractTableModel {

    String columnNames[] = {"STT", "Tên máy", "IP máy", "Cổng kết nối"};
    private List<Socket> list;
    private List<ComputerModel> listComputer;

    private ComputerModel createComputerModel(Socket s) {
        ComputerModel com = new ComputerModel();
        com.setName(s.getInetAddress().getHostName());
        com.setIp(s.getInetAddress().getHostAddress());
        com.setPort(s.getPort());
        return com;
    }

    public ComputerTableModel(List<Socket> list) {
        this.list = list;
        this.listComputer = new ArrayList();
        for (Socket s : list) {
            listComputer.add(createComputerModel(s));
        }
    }

    public int getSize() {
        return this.list.size();
    }

    public List<Socket> getList() {
        return this.list;
    }

    public Socket getItem(int rowIndex) {
        return this.list.get(rowIndex);
    }

    public void removeAllElement() {
        this.list.clear();
        this.listComputer.clear();
        fireTableDataChanged();
    }

    public void updateAllElement() {
        int i = 0;
        try {
            for (Socket s : list) {
                if (DataUtils.checkConnectClosed(s)) {
                    list.remove(i);
                    listComputer.remove(i);
                     fireTableRowsDeleted(i, i);
                } else {
                    i++;
                }
            }
        } catch (Exception e) {
        }
    }

    public void addElement(Socket e) {
        // Adds the element in the last position in the list
        list.add(e);
        listComputer.add(createComputerModel(e));
        fireTableRowsInserted(list.size() - 1, list.size() - 1);
    }

    public void removeElement(int rowIndex) {
        list.remove(rowIndex);
        listComputer.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public int getRowCount() {
        return list.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return listComputer.get(rowIndex).getName();
                case 2:
                    return listComputer.get(rowIndex).getIp();
                case 3:
                    return listComputer.get(rowIndex).getPort();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
