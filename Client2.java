package client2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
 import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
 import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Client2 {
    Long currentFrame; 
	Clip clip; 
	
	// current status of clip 
	String status; 
	
	AudioInputStream audioInputStream; 
	static String filePath; 
        public Client2() 
		throws UnsupportedAudioFileException, 
		IOException, LineUnavailableException 
	{
            audioInputStream = 
				AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile()); 
		
		// create clip reference 
		clip = AudioSystem.getClip(); 
		
		// open audioInputStream to the clip 
		clip.open(audioInputStream); 
                        
		
		clip.loop(Clip.LOOP_CONTINUOUSLY); 
        } 
   public static void main(String[] arg) throws SocketException, UnknownHostException, IOException, InterruptedException {
       System.out.println("==============================================");
       System.out.println("WELCOME TO RADIO STATION");
        System.out.println("==============================================");
       System.out.println("Radio stations in Nagpur:  {91.1,93.5,94.3,98.3,100.6,107.8}");
       System.out.println("Enter the station: ");
       DatagramSocket serverSocket = new DatagramSocket(9875);
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            
            
  try {

     Socket socketConnection = new Socket("127.0.0.1", 11111);


     //QUERY PASSING
     DataOutputStream outToServer = new DataOutputStream(socketConnection.getOutputStream());

     String SQL="I  am  CLIENT 2";
     outToServer.writeUTF(SQL);


  } catch (Exception e) {System.out.println(e); }
   BufferedReader inFromUser =
         new BufferedReader(new InputStreamReader(System.in));
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName("localhost");
      
      String sentence = inFromUser.readLine();
      sendData = sentence.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
      clientSocket.send(sendPacket);
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      clientSocket.receive(receivePacket);
      String modifiedSentence = new String(receivePacket.getData());
      System.out.println("FROM SERVER:" + modifiedSentence);
      clientSocket.close();
      //////////
      System.out.println("==============================================");
       System.out.println("==============================================");
       receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                   sentence = new String( receivePacket.getData());
                   String[] arrs = sentence.split(" ",8);
                  System.out.println("PLAYING: " + arrs[0]);
                  System.out.println("SINGER: "+arrs[1]);
                  System.out.println("MOVIE: "+arrs[2]);
                  System.out.println("CAST: "+arrs[3]);
                  System.out.println("YEAR: "+arrs[4]);
                  System.out.println("MUSIC: "+arrs[5]);
                  System.out.println("");
                  IPAddress = receivePacket.getAddress();
                  int port = receivePacket.getPort();
                  String capitalizedSentence = sentence.toUpperCase();
                  sendData = capitalizedSentence.getBytes();
                   sendPacket =
                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocket.send(sendPacket);
                  try
		{ 
			filePath =arrs[6]; 
			Client2 audioPlayer = new Client2();
			
			audioPlayer.play(); 
			Scanner sc = new Scanner(System.in); 
			 System.out.println("------------------------------");
			while (true) 
			{ 
				System.out.println("1. Pause"); 
				System.out.println("2. Resume"); 
				System.out.println("3. Restart"); 
				System.out.println("4. Stop"); 
				System.out.println("5. Jump to specific time"); 
				int c = sc.nextInt(); 
				audioPlayer.gotoChoice(c); 
				if (c == 4) 
				break; 
			} 
			sc.close(); 
		} 
		
		catch (Exception ex) 
		{ 
			System.out.println("Error with playing sound."); 
			ex.printStackTrace(); 
		
		} 
   
   }
   
   private void gotoChoice(int c) 
			throws IOException, LineUnavailableException, UnsupportedAudioFileException 
	{ 
		switch (c) 
		{ 
			case 1: 
				pause(); 
				break; 
			case 2: 
				resumeAudio(); 
				break; 
			case 3: 
				restart(); 
				break; 
			case 4: 
				stop(); 
				break; 
			case 5: 
				System.out.println("Enter time (" + 0 + 
				", " + clip.getMicrosecondLength() + ")"); 
				Scanner sc = new Scanner(System.in); 
				long c1 = sc.nextLong(); 
				jump(c1); 
				break; 
	
		} 
	
	} 
	
	// Method to play the audio 
	public void play() 
	{ 
		//start the clip 
		clip.start(); 
		
		status = "play"; 
	} 
	
	// Method to pause the audio 
	public void pause() 
	{ 
		if (status.equals("paused")) 
		{ 
			System.out.println("audio is already paused"); 
			return; 
		} 
		this.currentFrame = 
		this.clip.getMicrosecondPosition(); 
		clip.stop(); 
		status = "paused"; 
	} 
	
	// Method to resume the audio 
	public void resumeAudio() throws UnsupportedAudioFileException, 
								IOException, LineUnavailableException 
	{ 
		if (status.equals("play")) 
		{ 
			System.out.println("Audio is already "+ 
			"being played"); 
			return; 
		} 
		clip.close(); 
		resetAudioStream(); 
		clip.setMicrosecondPosition(currentFrame); 
		this.play(); 
	} 
	
	// Method to restart the audio 
	public void restart() throws IOException, LineUnavailableException, 
											UnsupportedAudioFileException 
	{ 
		clip.stop(); 
		clip.close(); 
		resetAudioStream(); 
		currentFrame = 0L; 
		clip.setMicrosecondPosition(0); 
		this.play(); 
	} 
	
	// Method to stop the audio 
	public void stop() throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException 
	{ 
		currentFrame = 0L; 
		clip.stop(); 
		clip.close(); 
	} 
	
	// Method to jump over a specific part 
	public void jump(long c) throws UnsupportedAudioFileException, IOException, 
														LineUnavailableException 
	{ 
		if (c > 0 && c < clip.getMicrosecondLength()) 
		{ 
			clip.stop(); 
			clip.close(); 
			resetAudioStream(); 
			currentFrame = c; 
			clip.setMicrosecondPosition(c); 
			this.play(); 
		} 
	} 
	
	// Method to reset audio stream 
	public void resetAudioStream() throws UnsupportedAudioFileException, IOException, 
											LineUnavailableException 
	{ 
		audioInputStream = AudioSystem.getAudioInputStream( 
		new File(filePath).getAbsoluteFile()); 
		clip.open(audioInputStream); 
		clip.loop(Clip.LOOP_CONTINUOUSLY); 
	} 
}
