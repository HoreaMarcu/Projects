package GraphicalUserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class Controller implements java.awt.event.ActionListener {

    private View view;
    public Controller(View v){
        this.view = v;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(Objects.equals(command, "COMPUTE")){

            String language = String.valueOf(view.getLanguage().getSelectedItem());
            String operation = String.valueOf(view.getOperation().getSelectedItem());
            String result = "";
            ImageIcon imgIcon = view.getIcon();
            switch (language){
                case "C++":
                    switch (operation){
                        case "Sort an array":
                            result = "<html>Static array sorted in 5965 steps and 0.0000001 seconds. <br> Dynamic array sorted in 5551 steps and 0.0000001 seconds. </html>";
                            imgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Carray.png")));
                            imgIcon.getImage().flush();
                            break;
                        case "Create threads":
                            result = "Number of steps for creating 100 threads: 100.";
                            break;
                        case "Thread migration":
                            result = "<html> Number of steps for adding 100 numbers using 100 threads: 600 <br> This process of thread migration was executed in: 1.614 seconds. </html>";
                            imgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Cth.png")));
                            imgIcon.getImage().flush();
                            break;
                    }
                    break;
                case "Java":
                    switch (operation){
                        case "Sort an array":
                            result = "Static array sorted in 5482 steps and 0.0 seconds.";
                            imgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Jarray.png")));
                            imgIcon.getImage().flush();
                            break;
                        case "Create threads":
                            result = "Number of steps for creating 100 threads: 201.";
                            break;
                        case "Thread migration":
                            result = "<html> Number of steps for adding 100 numbers using 100 threads: 301 <br> This process of thread migration was executed in: 0.211 seconds. </html>";
                            imgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Jth.png")));
                            imgIcon.getImage().flush();
                            break;
                    }
                    break;
                case "Python":
                    switch (operation){
                        case "Sort an array":
                            result = "Number of steps needed to sort a randomly generated array: 5266, in 0.0 seconds.";
                            imgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Parray.png")));
                            imgIcon.getImage().flush();
                            break;
                        case "Create threads":
                            result = "Number of steps needed to create 100 threads: 500.";
                            break;
                        case "Thread migration":
                            result = "<html> Number of steps needed to add 100 numbers using 100 threads: 509 <br> This process of thread migration has taken 0.01 seconds. </html>";
                            imgIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("Pth.png")));
                            imgIcon.getImage().flush();
                            break;
                    }
                    break;
            }
            view.getResultValueLabel().setText(result);
            view.getImage().setIcon(imgIcon);
        }
    }
}
