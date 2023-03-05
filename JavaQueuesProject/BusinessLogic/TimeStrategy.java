package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.ArrayList;

public class TimeStrategy implements Strategy{
    @Override
    public void addTask(ArrayList<Server> servers, Task t) {
        Server bestServer = new Server();
        int wantedIndex = 0;
        int minWaitingPeriod = 10000;
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).getWaitingPeriod().get() < minWaitingPeriod) {
                wantedIndex = i;
                minWaitingPeriod = servers.get(i).getWaitingPeriod().get();
            }
        }
        bestServer = servers.get(wantedIndex);
        bestServer.addTask(t);
    }
}
