package QuizGame;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String argv[]) throws Exception {
        String sentence;
        String modifiedSentence;
        String serverIP = "127.0.0.1";
        int nPort = 6789;
        
        BufferedReader inFromUser 
            = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket(serverIP, nPort);
        DataOutputStream outToServer
            = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer 
            = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
        // 서버에서 시작 메세지 받기
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER >> " + modifiedSentence);
        
        // 퀴즈 loop
        while ((modifiedSentence = inFromServer.readLine()) != null) {
            System.out.println("FROM SERVER >> " + modifiedSentence);
            
            // 물음표 있을 경우 Question
            if (modifiedSentence.contains("?")) {
                System.out.print("Enter your answer: ");
                sentence = inFromUser.readLine();
                outToServer.writeBytes(sentence + '\n');
            }
            
            // 퀴즈 종료 구문 -> 종료
            if (modifiedSentence.contains("Close")) {
                break;
            }
        }
        
        clientSocket.close();
    }
}