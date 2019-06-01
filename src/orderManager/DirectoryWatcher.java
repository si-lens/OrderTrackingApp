package orderManager;

import orderManager.gui.model.Model;

import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DirectoryWatcher {
Model model;

public void start() {
    model = Model.getInstance();
    ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
    s.scheduleAtFixedRate(() -> {
        try {
            observe();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }, 0, 5000, TimeUnit.MILLISECONDS);
}


        public void observe(){
            // get path object pointing to the directory we wish to monitor
            Path path = Paths.get("C:\\Users\\Szymon\\IdeaProjects\\CSe2018B_Exam_1sty_Grp2");
            try {
                // get watch service which will monitor the directory
                WatchService watcher = path.getFileSystem().newWatchService();
                // associate watch service with the directory to listen to the event
                // types
                path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
                System.out.println("Monitoring directory for changes...");
                // listen to events
                WatchKey watchKey = watcher.take();
                // get list of events as they occur
                List<WatchEvent<?>> events = watchKey.pollEvents();
                //iterate over events
                for (WatchEvent event : events) {
                    //check if the event refers to a new file created
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        //print file name which is newly created
                       // System.out.println("Created: " + event.context().toString());

                        String fullPath = event.context().toString();
                        int index = fullPath.lastIndexOf('.');
                        String finalPath = fullPath.substring(index + 1);
                        if (finalPath.equals("json")) {
                            System.out.println("New json file detected: "+ event.context().toString()+" - Loading...");
                            model.getManager().readFile(fullPath);
                        } else
                            System.out.println("Wrong file! Try again.");

                    }
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}