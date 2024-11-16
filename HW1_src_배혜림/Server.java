package QuizGame;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket welcomeSocket;
        String clientSentence;
        String capitalizedSentence;
        int nPort;
        
        nPort = 6789;
        welcomeSocket = new ServerSocket(nPort);
        System.out.println("Server start.. (port#=" + nPort + ")\n");
        
        // 문제 array 정의
        String[] questions = {
            "What is the delay corresponding to the time need for bits to physically propagate through the transmission medium from one end of the link to the other?",
            "What is the layer that transfer bit into and out of the transmission media?",
            "Suppose a packet is L = 25 bits, and link transmits at R = 100 bps. What is the transmission delay for this packet? ",
            "What communication method does SMTP use to deliver email from sender to recipient server?",
            "What is the time it takes for a packet to travel from client to server and back?"
        };
        // 답 array 정의 (여러개의 답 후보)
        String[][] answers = {
        		{"propagation delay", "propagation"},
        	    {"physical layer", "physical"},
        	    {"0.25", "0.4"},
        	    {"push", "push method"},
        	    {"RTT", "Round Trip Time"}
        };
        
        
        while (true) {
            // wait for a new connection on nPort#
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected!");
            
            BufferedReader inFromClient = new BufferedReader(
                new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(
                connectionSocket.getOutputStream());
            
            // 시작 메세지
            capitalizedSentence = "Let's start the quiz! (answer in english)\nTotal questions: " + questions.length + "\n";
            outToClient.writeBytes(capitalizedSentence);
            
            int score = 0;
            int[] cnt = new int[questions.length];  // 틀린 문제 번호 저장
            int wrongCount = 0;  // 틀린 문제 개수

            
            for (int i = 0; i < questions.length; i++) {
                // 문제 제시
                capitalizedSentence = "Question No" + (i + 1) + ". " + questions[i] + "\n";
                outToClient.writeBytes(capitalizedSentence);

                // 답 읽기
                clientSentence = inFromClient.readLine();
                if (clientSentence == null) break;

                // 답 확인
                boolean correct = false;
                String userAnswer = clientSentence.trim().toLowerCase();
                for(String answer : answers[i]) {
                    if(userAnswer.equalsIgnoreCase(answer)) {
                        correct = true;
                        break;
                    }
                }
                if(correct) {
                    score++;
                    capitalizedSentence = "Correct!\n";
                } else {
                    cnt[wrongCount++] = i + 1;  // 틀린 문제 번호 저장
                    capitalizedSentence = "Wrong. The answers could be: " + String.join(" or ", answers[i]) + "\n";
                }
                outToClient.writeBytes(capitalizedSentence);
            }
            
            // 점수 계산
            if (wrongCount > 0) {
                capitalizedSentence = "The quiz is over. Your score: " + (double)score / questions.length * 100 + "\n"
                                   + "Wrong question numbers: ";
                for(int i = 0; i < wrongCount; i++) {
                    capitalizedSentence += "No." + cnt[i];
                    if(i < wrongCount - 1) {
                        capitalizedSentence += ", ";
                    }
                }
                capitalizedSentence += "\n Close.";
            } else {
                capitalizedSentence = "The quiz is over. Your score: " + (double)score / questions.length * 100 + "\n Close.";
            }
            outToClient.writeBytes(capitalizedSentence);
        }
    }
}