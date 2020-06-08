 package servers;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
 import java.net.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

class Multi extends Thread{
private Socket s=null;
DataInputStream infromClient;
Multi() throws IOException{
}
Multi(Socket s) throws IOException{
    this.s=s;
    infromClient = new DataInputStream(s.getInputStream());
}
public void run(){  
    String SQL=new String();
    try {
        SQL = infromClient.readUTF();
    } catch (IOException ex) {
        Logger.getLogger(Multi.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println("Query: " + SQL); 
    try {
        System.out.println("Socket Closing");
        s.close();
    } catch (IOException ex) {
        Logger.getLogger(Multi.class.getName()).log(Level.SEVERE, null, ex);
       }
   }  
}
public class Servers {
public static void main(String args[]) throws IOException, 
InterruptedException{
    String capitalizedSentence;
    DatagramSocket serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            String[] songList = new String[100];
            int sl=0;
            
        while(true){
        ServerSocket ss=new ServerSocket(11111);
        System.out.println("==============================================");
       System.out.println("RADIO STATION");
        System.out.println("==============================================");
        System.out.println("Server is Awaiting");
        System.out.println("Radio stations in Nagpur:  {91.1,93.5,94.3,98.3,100.6,107.8}");
        Socket s=ss.accept();
        Multi t=new Multi(s);
        t.start();
        Thread.sleep(2000);
        ss.close();
        //udp
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String sentence = new String( receivePacket.getData());
                  System.out.println("RECEIVED: " + sentence);
                  File file;
                  if(sentence=="91.1"){
                   file = new File("D:\\songs.txt");}
                  else if(sentence=="93.5"){
                      file = new File("D:\\songs1.txt");} 
                  else if(sentence=="94.3"){
                      file = new File("D:\\songs2.txt");} 
                  else if(sentence=="98.3"){
                     file = new File("D:\\songs3.txt");} 
                  else if(sentence=="100.6"){
                     file = new File("D:\\songs4.txt");} 
                  else {
                     file = new File("D:\\songs5.txt");} 
               BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					file));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
                                songList[sl] = line;
                                sl++;
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
                Random r = new Random();
                  InetAddress IPAddress = receivePacket.getAddress();
                  int port = receivePacket.getPort();
                  
                      capitalizedSentence ="OK";
                  
                  sendData = capitalizedSentence.getBytes();
                  DatagramPacket sendPacket =
                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocket.send(sendPacket);
                  ////////server is sending
                  int randomNumber = r.nextInt(sl);
                 
                   BufferedReader inFromUser =
         new BufferedReader(new InputStreamReader(System.in));
      DatagramSocket clientSocket = new DatagramSocket();
       IPAddress = InetAddress.getByName("localhost");
       sendData = new byte[1024];
       receiveData = new byte[1024];
       sentence = songList[randomNumber];                                        
      sendData = sentence.getBytes();
       sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9875);
      clientSocket.send(sendPacket);
       receivePacket = new DatagramPacket(receiveData, receiveData.length);
      clientSocket.receive(receivePacket);
      
      String modifiedSentence =new String(receivePacket.getData());
      System.out.println("FROM SERVER:" + modifiedSentence);
      clientSocket.close();
         }
    }   
}
