package com.mycompany.asm2;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Scanner;

public class Asm2 {
    public static String accountFile = "src\\main\\java\\account\\account.txt";
    public static HashMap<String, String> accounts = new HashMap<String, String>();   
    
    public static void main(String argv[]) throws IOException
    {
        readFromFile();
        connectToClient();
//        System.out.print(checkAccount("an"));
    }
    
    private static void connectToClient() throws IOException
    {
        String sentence_from_client;
        String sentence_to_client;
        String rightPassword;
        
        //Tạo socket server, chờ tại cổng '5555'
        ServerSocket welcomeSocket = new ServerSocket(5555);
        
        while(true) {
            //chờ yêu cầu từ client
            Socket connectionSocket = welcomeSocket.accept();
            
            //Tạo input stream, nối tới Socket
            BufferedReader inFromClient =
                new BufferedReader(new
                    InputStreamReader(connectionSocket.getInputStream())); 
            
            //Tạo outputStream, nối tới socket
            DataOutputStream outToClient =
                new DataOutputStream(connectionSocket.getOutputStream());
            
            //Đọc thông tin từ socket
            String account = inFromClient.readLine();
            rightPassword = checkAccount(account) +" (Server accepted!)" + '\n';

            //ghi dữ liệu ra socket
            outToClient.writeBytes(rightPassword); 
            
            //More
            String password = inFromClient.readLine();
            if(password == rightPassword)
            {
                sentence_to_client = password +" (Server accepted!)" + '\n';
            }
            else
            {
                sentence_to_client = password +" (Server not accepted!)" + '\n';
            }
            
            outToClient.writeBytes(sentence_to_client);
            
            return;
        }
    }

    private static String checkAccount(String accountInput)
    {
        return accounts.get(accountInput);
    }
    
    private static void readFromFile()
    {
        try {
          File myObj = new File(accountFile);
          Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] dataItems = data.split(" ");
            accounts.put(dataItems[0], dataItems[1]);
          }
          myReader.close();
        } catch (FileNotFoundException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
    }
}

