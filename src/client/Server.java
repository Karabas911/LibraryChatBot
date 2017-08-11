package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

//Server part of application
public class Server implements Runnable{
	public static final int PORT = 5981;
	@Override
	public void run() {
		 try {
		  //Make connection between server and client
	         ServerSocket ss = new ServerSocket(PORT);
	         System.out.println("Waiting for a client...");
	         Socket socket = ss.accept();
	         System.out.println("Got a client :)");
	         System.out.println();
	         
	      // Make Input and Output Stream to send and get massages 
	         InputStream sin = socket.getInputStream();
	         OutputStream sout = socket.getOutputStream();
	         
	      // Convert Stream to another type
	         DataInputStream in = new DataInputStream(sin);
	         DataOutputStream out = new DataOutputStream(sout);

	         String line = null;
	      // Infinite cycle which handles client messages
	         while(true) {
	           line = in.readUTF();
	           if(line.contains("add")) {
	        	   String fullName = line.substring(line.indexOf(" ")+1);
	        	   String author = fullName.substring(0,fullName.indexOf("\"")-1);
	        	   String bookName = fullName.substring(fullName.indexOf("\"")+1,fullName.lastIndexOf("\""));
	        	   Database.addBook(author,bookName);
	        	   out.writeUTF("book "+fullName+" was added");
		           out.flush();
	           }
	           else if(line.contains("remove")) {
	        	   String bookName = line.substring(line.indexOf(" ")+1);
	        	   ResultSet resSet = Database.findBookByName(bookName);
	        	   int rowCount = 0;
	        	   if(resSet.last()){
	        		    rowCount = resSet.getRow(); 
	        		    resSet.beforeFirst();
	        		}
	        	   if(rowCount ==0) {
	        		   out.writeUTF("the library does not have book with name \""+bookName+"\"");
				       out.flush();
	        	   }
	        	   if(rowCount ==1) {
	        		   resSet.next();
		        	   out.writeUTF("book "+resSet.getString("book_author")+" \""+resSet.getString("book_name")+"\" was removed");
				       out.flush();
	        		   Database.removeBook(bookName);	       		
	        	   }
	        	   
	        	   if(rowCount>1) {
		        	   System.out.println(rowCount+  " books with same name were found" );
	        		   String chooseBook =  "we have few books with such name please choose one by typing a number of book: ";
	        		   int position = 1;
	        		   while(resSet.next()) {
	        			   chooseBook+= "\n       "+position+ ". "+resSet.getString("book_author")+" "+resSet.getString("book_name");
	        			   position++;
	        		   }
	        		   
		        	   out.writeUTF(chooseBook);
		        	   out.flush();
		        	   position = Integer.parseInt(in.readUTF());
		        	   resSet.beforeFirst();
		        	   int count = 1;
		        	   while(resSet.next()) {
		        		   if(count ==position) break;
		        		   count++;
		        	   }
		        	   out.writeUTF("book "+resSet.getString("book_author")+" \""+resSet.getString("book_name")+"\" was removed");
				       out.flush();
		        	   Database.removeBook(resSet.getString("book_author"),resSet.getString("book_name"));
		        	   
	        	   }
	           }
	           else if (line.contains("all books")) {
	        	   ResultSet resSet = Database.getAllBooks();
	        	   String allBooks = "Our Books:";
	        	   while(resSet.next()) {
	        		allBooks+= "\n          "+resSet.getString("book_author")+" \""+resSet.getString("book_name")+"\"";
	        	   }
	        	   Database.closeStatement();
	        	   out.writeUTF(allBooks);
			       out.flush();
	           }
	           else if (line.contains("edit book")) {
	        	   String editBookName = line.substring(line.indexOf(" ")+1);
	        	   editBookName = editBookName.substring(editBookName.indexOf(" ")+1);  
	        	   ResultSet resSet = Database.findBookByName(editBookName);
	        	   int rowCount = 0;
	        	   if(resSet.last()){
	        		    rowCount = resSet.getRow(); 
	        		    resSet.beforeFirst();
	        		}
	        	   if(rowCount>=1) {
	        		   out.writeUTF("Please enter new name of the book:");
				       out.flush();
				       String newBookName = in.readUTF();
				       Database.updateBookName(editBookName,newBookName);
				       out.writeUTF("book name "+editBookName+" was changed to "+newBookName );
				       out.flush();
	        	   }
	        	   else {
	        		   out.writeUTF("the library does not have book with name \""+editBookName+"\"" );
				       out.flush();
	        	   }
	           }
	           else {
	        	   out.writeUTF("I am sorry, but I do not understand you" );
			       out.flush(); 
	           }
	           
	         }
	      } catch(Exception x) { x.printStackTrace(); }
	}

}
