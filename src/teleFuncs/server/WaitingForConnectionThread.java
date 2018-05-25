// package server;

// import java.io.*;
// import java.net.*;

// public class WaitingForConnectionThread extends ManageableThread {
//     private Server _server;
//     private ServerSocket _serverSocket;
//     private ThreadManager _threadManager;
//     public void cancelConnection() {
//         this.isActive(false);
//     }

//     public WaitingForConnectionThread(Server server, ServerSocket serverSocket, ThreadManager threadManager) {
//         super();
//         this._server = server;
//         this._serverSocket = serverSocket;
//         this._threadManager = threadManager;
//     }

//     @Override
//     public void run() {
//         try {
//             this._serverSocket.setSoTimeout(100);
            
//         finally {
//             dispose();
//         }
//     }

//     @Override
//     public void dispose() {
//         System.out.println("disposed wfct...");    // 4debug
        
//         super.dispose();
//         try {
//             if(this._serverSocket != null) {
//                 this._serverSocket.close();
//                 this._serverSocket = null;
//             }
//         }
//         catch(IOException ioe) {
//             ioe.printStackTrace();
//         }
//         finally {
//         }
//         this._threadManager = null;
//     }
// }