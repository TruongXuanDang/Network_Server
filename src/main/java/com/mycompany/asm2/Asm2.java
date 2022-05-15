package com.mycompany.asm2;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Scanner;

public class Asm2 {
    public static String accountFile = "src\\main\\java\\account\\account.txt";
    public static HashMap<String, String> accounts = new HashMap<String, String>();  
    public static String rightPassword = null;
    public static String loginErrorMessage = "Error Require login";
    public static String loginSuccessMessage = "Ok Logout successful. Client stopped!";
    
    public static void main(String argv[]) throws IOException
    {
        accounts = readFromFile(accountFile);
        connectToClient();
    }
    
    private static void connectToClient() throws IOException
    { 
        int isLogin = 0;
        //Socket server
        ServerSocket welcomeSocket = new ServerSocket(5555);
        
        //Wait from client
        Socket connectionSocket = welcomeSocket.accept();
        
        while(true)
        {
            //Input stream with Socket
            BufferedReader inFromClient =
                new BufferedReader(new
                    InputStreamReader(connectionSocket.getInputStream())); 

            //OutputStream with Socket
            DataOutputStream outToClient =
                new DataOutputStream(connectionSocket.getOutputStream());

            //Check account
            String value = inFromClient.readLine();
            String[] values = value.split(" ", 2);
            String header = values[0];
        
            if(header.equals("Account"))
            {
                outToClient.write(checkAccount(values[1]));
            }
            else if(header.equals("Password"))
            {
                isLogin = checkPassword(values[1]);
                outToClient.write(isLogin);
            }
            else if(header.equals("LOGOUT"))
            {
                if(isLogin == 0)
                {
                    outToClient.writeBytes(loginErrorMessage + '\n');
                }
                else
                {
                    isLogin = 0;
                    outToClient.writeBytes(loginSuccessMessage + '\n');
                }
                return;
            }
            else 
            {
                if(isLogin == 0)
                {
                    outToClient.writeBytes(loginErrorMessage);
                }
                else
                {
                    outToClient.writeBytes(values[1] + '\n');
                }
            }
        }
    }

    private static int checkAccount(String accountInput)
    {
        rightPassword = accounts.get(accountInput);
        if(rightPassword != null)
        {
            return 1;
        }
        return 0;
    };
    
    private static int checkPassword(String password)
    {
        if(password.equals(rightPassword))
        {
            return 1;
        }
        return 0;
    }
    
    private static HashMap<String, String> readFromFile(String accountFileName)
    {
        HashMap<String, String> accounts = new HashMap<String, String>(); 
        try {
          File myObj = new File(accountFileName);
          Scanner myReader = new Scanner(myObj);
          while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] dataItems = data.split(" ");
            accounts.put(dataItems[0], dataItems[1]);
          }
          myReader.close();
        } catch (FileNotFoundException e) {
          System.out.println("Cannot read from data");
          e.printStackTrace();
        }
        return accounts;
    }
}

