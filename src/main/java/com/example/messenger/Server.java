package com.example.messenger;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        try{
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept(); //server object to return server. it's blocking agent

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }  catch(IOException e){
            System.out.println("Error creating server");
            e.printStackTrace();

         }
    }

    public void sendMessageToClient(String messageToClient){
        try {
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Error in sending message to the client");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    } {

    }

    public void receiveMessageFromClient(VBox vbox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    try {
                        String messageFromClient = bufferedReader.readLine();
                        HelloController.addLabel(messageFromClient, vbox);

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error in reading form the client");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        });

    }
    public  void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if(bufferedReader!= null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
