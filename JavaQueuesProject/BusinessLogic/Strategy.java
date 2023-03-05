package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.ArrayList;

public interface Strategy {
    void addTask(ArrayList<Server> servers, Task t);

}
