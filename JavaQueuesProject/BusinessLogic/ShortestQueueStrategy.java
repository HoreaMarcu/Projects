package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.ArrayList;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(ArrayList<Server> servers, Task t) {
        Server bestServer = new Server();
        int wantedIndex = 0;
        int minWaitingQueue = 10000;
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).getTasks().length < minWaitingQueue) {
                wantedIndex = i;
                minWaitingQueue = servers.get(i).getTasks().length;
            }
        }
        bestServer = servers.get(wantedIndex);
        bestServer.addTask(t);
    }
}
