package forms;

import dao.connectionProvider;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import utility.BDUtility;
import java.sql.*;
import java.util.Objects;

public class DeleteUser extends javax.swing.JFrame{
	

public DeleteUser() {
	initComponents();
	BDUtility.setImage(this,"images/abc1.jpg",1087,491);
	this.getRootPane().setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.BLACK));
}




private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
	this.dispose();
}
private void formComponentShown(java.awt.event.ComponentEvent evt) {
	fetchUser(null);
}
private void txtSearchkeyReleased(java.awt.event.KeyEvent evt) {
	String searchText=txtSearch.getTExt().toString();
	if(Objects.isNull(searchText)|| searchText.isEmpty()) {
		fetchUser(null);
	}else {
		fetchUser(searchText);
	}
}
private void userTableMouseClicked(java.awt.event.MouseEvent evt) {
	try {
		int dialogResult=JOptionPane.showConfirmDialog(null,"*user details\n* Images\n* QR codes\n* Attendance\n\n associated with this user will be deleted.\n are you sure u want to proceed?","confirmation",JOptionPane.YES_NO_OPTION);
	    if(dialogResult==JOptionPane.YES_OPTION) {
	    	int index=userTable.getSelectedRow();
	    	TableModel model=userTable.getModel();
	    	String email=model.getValueAt(index,3).toString();
	    	String imagePath=BDUtility.getPath("/images"+File.separator+email+".jpg");
	    	deleteFile(imagePath);
	    	imagePath=BDUtility.getPath("/qrCodes"+File.separator+email+".jpg");
	    	deleteFile(imagePath);
	    	
	    	Connection connection=ConnectionProvider.getCon();
	    	String attendanceDeleteQuery="DELETE userattendance,userdetails FROM userdetails LEFT JOIN user attendance ON userattendance.userod=userdetails.id where userdetails.email=?";
	    	PreparedStatement preparedStatement=connection.prepareStatement(attendanceDeleteQuery);
	    	preparedStatement.setString(1, email);
	    	preparedStatement.executeUpdate();
	    	fetchUser(null);
	    	JOptionPane.showMessageDialog(null, "user deleted successfully.","Confirmation",JOptionPanee.INFORMATION_MESSAGE);
	    	
	    	
	    }else {
	    	JOptionPane.showMessageDialog(null,"Something went wrong","error",JOptionPane.INFORMATION_MESSAGE);
	    }
	}catch (Exception ex) {
		JOptionPane.showMessageDialog(null, "something went wrong.","Error",JOptionPane.ERROR_MESSAGE);
	}
}
private void fetchUser(String searchText) throws HeadlessException{
	DefaultTableModel model=(DefaultTableModel) userTable.getModel();
    model.setRowCount(0);
    try {
    	Connection con=ConnectionProvider.getCon();
    	Statement statement=con.createStatment();
    	String query=null;
    	if(Objects.isNull(searchText)) {
    		query="select * from userdetails";
    	}else {
    		query="select * from userdetails where name like'%"+searchText+"%' or email like+'%"+searchText+"%'"; 
    	}
    	ResultSet resultSet=statement.executeQuery(query);
    	while(resultSet.next()) {
    		model.addRow(new Object[] {
    				resultSet.getString("id"),
    				resultSet.getString("name"),
    				resultSet.getString("gender"),
    				resultSet.getString("email"),
    				resultSet.getString("contact"),
    				resultSet.getString("adress"),
    				resultSet.getString("state"),
    				resultSet.getString("country"),});

    			
    	}
    }catch(Exception ex) {
    	JOptionPane.showMessageDialog(null, ex);
    }
}
private void deleteFile(String filepath) {
	File fileToDelete=new File(filepath);
	if(fileToDelete.exists()) {
		if(fileToDelete.delete()) {
			System.out.println("File deleted successfully");
		}else {
			System.out.println("Failed to delete the file");
		}
	}
	
	
}

