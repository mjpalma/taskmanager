import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaskManager {
    public static final String TASKS_CSV = "tasks.csv";
    public static final String EMPTY_STRING = "";
    public static final String SPLIT_CHARACTER = ",";
    public static final String COLUMN_CHARACTER = " | ";
    public static final String COLUMN_HEADER = "ID | Title | Description | Due Date | Completed";
    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private static final String LIST = "list";
    private static final String DELETE = "delete";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Integer currentId = 0;

    /**
     * The main method that runs when the application starts.
     * It reads the tasks from the file and executes the command provided as an argument.
     *
     * @param args The command line arguments.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        getFile();

        String command = EMPTY_STRING;
        if (args.length > 0) {
            command = args[0];
        }

        switch (command) {
            case CREATE:
                createTask(args[1], args[2], args[3]);
                break;
            case UPDATE:
                updateTask(Integer.parseInt(args[1]), args[2], args[3], args[4], args[5]);
                break;
            case LIST:
                listTasks();
                break;
            case DELETE:
                deleteTask(Integer.parseInt(args[1]));
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }

    /**
     * Creates a new task and adds it to the task list.
     *
     * @param title       The title of the task.
     * @param description The description of the task.
     * @param dueDate     The due date of the task.
     */
    public static void createTask(String title, String description, String dueDate) {
        LocalDateTime dateTime = LocalDateTime.parse(dueDate, FORMATTER);
        Task task = new Task(title, description, dateTime);
        task.setId(getId());
        tasks.add(task);

        updateFile();
    }

    /**
     * Generates a new ID for a task.
     *
     * @return The generated ID.
     */
    private static Integer getId() {
        Integer nextId = currentId + 1;
        currentId = nextId;

        return nextId;
    }

    /**
     * Updates an existing task in the task list.
     *
     * @param id          The ID of the task to update.
     * @param title       The new title of the task.
     * @param description The new description of the task.
     * @param dueDate     The new due date of the task.
     * @param completed   The new completion status of the task.
     */
    public static void updateTask(Integer id, String title, String description, String dueDate, String completed) {
        LocalDateTime dateTime = LocalDateTime.parse(dueDate, FORMATTER);
        Boolean isCompleted = Boolean.parseBoolean(completed);

        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setTitle(title);
                task.setDescription(description);
                task.setDueDate(dateTime);
                task.setCompleted(isCompleted);
            }
        }

        updateFile();
    }

    /**
     * Prints all tasks in the task list.
     */
    public static void listTasks() {
        System.out.println(COLUMN_HEADER);
        for (Task task : tasks) {
            printTask(task);
        }
    }

    /**
     * Prints a task to the console.
     *
     * @param task The task to print.
     */
    private static void printTask(Task task) {
        StringBuilder taskBuilder = new StringBuilder();
        taskBuilder.append(task.getId())
                .append(COLUMN_CHARACTER)
                .append(task.getTitle())
                .append(COLUMN_CHARACTER)
                .append(task.getDescription())
                .append(COLUMN_CHARACTER)
                .append(task.getDueDate().format(FORMATTER))
                .append(COLUMN_CHARACTER)
                .append(task.getCompleted());
        System.out.println(taskBuilder);
    }

    /**
     * Deletes a task from the task list.
     *
     * @param id The ID of the task to delete.
     */
    public static void deleteTask(Integer id) {
        if (id != null) {
            tasks.removeIf(task -> task.getId().equals(id));
        }

        updateFile();
    }

    /**
     * Gets the task file and loads the tasks into the task list.
     *
     * @throws IOException If an I/O error occurs.
     */
    public static void getFile() throws IOException {
        File file = new File(TASKS_CSV);
        if (!file.exists()) {
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                System.out.println("File could not be created.");
            }
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = EMPTY_STRING;
                String lastLine = EMPTY_STRING;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(SPLIT_CHARACTER);
                    lastLine = line;
                    tasks.add(new Task(Integer.parseInt(values[0]), values[1], values[2], LocalDateTime.parse(values[3], FORMATTER), Boolean.parseBoolean(values[4])));
                }
                if (!lastLine.isEmpty()) currentId = Integer.parseInt(lastLine.split(SPLIT_CHARACTER)[0]);
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred while getting the file:" + e.getMessage());
            }
        }
    }


    /**
     * Updates the task file with the current tasks in the task list.
     */
    public static void updateFile() {
        File file = new File(TASKS_CSV);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(buildTask(task));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the file:" + e.getMessage());
        }
    }

    /**
     * Builds a string representation of a task for writing to the task file.
     *
     * @param task The task to build a string representation of.
     * @return The string representation of the task.
     */
    private static String buildTask(Task task) {
        StringBuilder taskBuilder = new StringBuilder();
        taskBuilder.append(task.getId())
                .append(SPLIT_CHARACTER)
                .append(task.getTitle())
                .append(SPLIT_CHARACTER)
                .append(task.getDescription())
                .append(SPLIT_CHARACTER)
                .append(task.getDueDate().format(FORMATTER))
                .append(SPLIT_CHARACTER)
                .append(task.getCompleted());
        return taskBuilder.toString();
    }


}