// package server;

// import java.io.*;
// import java.net.*;
// import java.util.*;
// import java.lang.InterruptedException;
// import interfaces.IDisposable;

// public class ThreadManager extends Thread implements IDisposable {
//     private Server _server;

//     private ArrayList<ClientThread> _candidates;
//     public void addCandidate(ClientThread ct) {
//         ct.start();
//         this._candidates.add(ct);
//     }

//     private int _registeredPlayerNum;
//     public int registeredPlayerNum() {
//         return this._registeredPlayerNum;
//     }

//     private ClientThread[] _playerThreads;
//     private void addPlayer(ClientThread ct) {
//         this._playerThreads[this._registeredPlayerNum] = this._candidates.remove(this._candidates.indexOf(ct));
//         this._registeredPlayerNum++;
//         System.err.println("registered player num : " + this._registeredPlayerNum); // 4debug
//         if(this._registeredPlayerNum == this._server.playerNum() - 1) {
//             this._server.isNowPlaying(true);
//             this._server.finishInvitaion();
//             System.out.println("Completed.");   // 4debug
//         }
//     }


//     public ThreadManager(Server server) {
//         this._server = server;
//         this._candidates = new ArrayList<ClientThread>();
//         this._registeredPlayerNum = 0;
//         this._playerThreads = new ClientThread[this._server.playerNum() - 1];   // master player is not in this array.
//     }
    
//     @Override
//     public void run() {
//         // Connection Part Observing
//         while(true) {
//             ClientThread target = null;
//             if(this._candidates.size() > 0) {
//                 for (ClientThread ct : this._candidates) {
//                     // System.err.println("Candiplayer's name : " + ct.playerName());   // 4debug
//                     // System.err.println("isActive : " + ct.isActive() + "isInProcess : " + ct.isInProcess());   // 4debug
//                     if(ct.isActive() && !ct.isInProcess()) {
//                         target = ct;
//                         break;
//                     }
//                 }

//                 // System.err.println(target != null);  // 4debug
                
//                 if(target != null) {
//                     // System.out.println("add player.");  // 4debug
//                     addPlayer(target);
//                 }
//             }

//             // System.err.println("Test");  // 4debug

//             if(this._server.isNowPlaying()) {
//                 // System.err.println("Finished Connection Part Observation");   // 4debug
//                 break;
//             }
//         }

//         // Playing Part Observing
//     }

//     public void dispose() {
//         System.out.println("disposed tm...");    // 4debug

//         if(this._candidates != null) {
//             for (ClientThread ct : this._candidates) {
//                 ct.dispose();
//             }
//         }

//         if(this._playerThreads != null) {
//             for(int i = 0; i < this._playerThreads.length; i++) {
//                 if(this._playerThreads[i] != null) {
//                     this._playerThreads[i].dispose();
//                 }
//             }
//         }
//     }
// }