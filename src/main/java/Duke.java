import CustomExceptions.DukeException;
import Enums.ExceptionType;
import Enums.Tasktype;
import Model_Classes.*;
import Operations.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * main class of the Duke program
 */
public class Duke {
    private Ui ui;
    private Storage storage;
    private TaskList taskList;
    private Parser parser;

    /**
     * Constructor of a Duke class. Creates all necessary objects and collections for Duke to run
     * Also loads the ArrayList of tasks from the data.txt file
     */
    public Duke() {
        ui = new Ui();
        ui.startUp();
        storage = new Storage();
        parser = new Parser();
        try {
            taskList = new TaskList(storage.loadFile());
        } catch (DukeException e) {
            ui.showLoadError();
            ArrayList<Task> emptyList = new ArrayList<>();
            taskList = new TaskList(emptyList);
        }
    }

    /**
     * Deals with the operation flow of Duke.
     */
    public void run() {
        boolean isExit = false;
        while (!isExit) {
            String command = parser.getCommand();
            Tasktype type = Tasktype.valueOf(command);
            switch (type) {
                case list :
                    ui.showList();
                    taskList.list();
                    break;

                case bye :
                    isExit = true;
                    try {
                        storage.writeFile(taskList.currentList());
                        ui.showBye();
                    } catch (DukeException e) {
                        ui.showWriteError();
                        ui.showBye();
                    }
                    break;

                case done :
                    try {
                        ui.showDone();
                        taskList.done(parser.getIndex());
                    } catch (DukeException e) {
                        ui.showIndexError();
                    }
                    break;

                case delete :
                    try {
                        int index = parser.getIndex();
                        taskList.delete(index);
                        ui.showDeleted(index);
                    } catch (DukeException e) {
                        ui.showIndexError();
                    }
                    break;

                case find :
                    ui.showFind();
                    taskList.find(parser.getKey());
                    break;

                case todo :
                    try {
                        ui.showAdd();
                        ToDo temp = new ToDo(parser.getDescription());
                        taskList.add(temp);
                    } catch (DukeException e) {
                        ui.showEmptyDescriptionError();
                    }
                    break;

                case deadline :
                    try {
                        ui.showAdd();
                        String[] deadlineArray = parser.getDescriptionWithDate();
                        Date by = parser.formatDate(deadlineArray[1]);
                        Deadline temp = new Deadline(deadlineArray[0], by);
                        taskList.add(temp);
                    } catch (DukeException e) {
                        ui.showDateError();
                    }
                    break;

                case event :
                    try {
                        ui.showAdd();
                        String[] eventArray = parser.getDescriptionWithDate();
                        System.out.println(eventArray[1]);
                        Date at = parser.formatDate(eventArray[1]);
                        System.out.println(at);
                        if(CheckAnomaly.checkTime(at, TaskList.currentList())){
                            Event temp = new Event(eventArray[0], at);
                            taskList.add(temp);
                        } else {
                            throw new DukeException(ExceptionType.timeClash);
                        }
                    } catch (DukeException e) {
                        ui.showDateError();
                    }
                    break;

                case time :
                    ui.showAdd();
                    String[] ti = parser.getDescriptionWithDuration();
                    String[] ar = parser.getDuration(ti);
                    int duration = Integer.parseInt(ar[1]);
                    FixedDuration fixedDuration = new FixedDuration(ar[0], ar[1]);
                    taskList.add(fixedDuration);
                    Timer timer = new Timer();
                    class RemindTask extends TimerTask {
                        public void run() {
                            System.out.println(ar[0] + "is completed");
                            timer.cancel();
                        }
                    }
                    RemindTask rt = new RemindTask();
                    timer.schedule(rt, duration * 1000);
                    break;

                default:
                    ui.showCommandError();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new Duke().run();
    }
}
