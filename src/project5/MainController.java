package project5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class MainController implements Initializable {

    @FXML
    private Button btnInsert;
    @FXML
    private Button btnListAllGraduates;
    @FXML
    private Button btnListAllCourses;
    @FXML
    private Button btnCalculateGPA;
    @FXML
    private Button btnAvgOfCourse;
    @FXML
    private Button btnAvgOfSlot;

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ccr");
    static EntityManager em = emf.createEntityManager();
    static String fileName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnInsert.setOnAction(e -> {
            String dataFolderDest = "C:\\Users\\durgu\\OneDrive\\Masaüstü\\advanced java\\Project5\\Data\\";

            for (int i = 1; i <= 694; i++) {
                if (i == 694) {
                    fileName = dataFolderDest + "2126.txt.txt.mxt";
                } else {
                    fileName = dataFolderDest + i + ".txt.txt.mxt";
                }

                File file = new File(fileName);

                if (file.exists()) {
                    readAndInsertFromFile(fileName);
                } else {
                    System.out.println("Dosya bulunamadı: " + fileName);
                }
            }
            try {
                em.getTransaction().commit();
            } catch (Exception ex) {
                
            }
        });

        btnListAllGraduates.setOnAction(em -> {
            Stage currentStage = (Stage) btnListAllGraduates.getScene().getWindow();
            BorderPane borderPane = (BorderPane) currentStage.getScene().getRoot();
            borderPane.setCenter(null);
            HBox hbox = new HBox();
            TextField tfYear = new TextField();
            tfYear.setPromptText("Enter year");
            Button btnList = new Button("List");

            hbox.getChildren().addAll(tfYear, btnList);
            borderPane.setBottom(hbox);

            btnList.setOnAction(event -> {
                borderPane.setBottom(null);

                if (tfYear.getText().isEmpty()) {
                    showErrorAlert("Alert", "Error", "Please enter a year!");
                } else {
                    try {
                        int selectedYear = Integer.parseInt(tfYear.getText());

                        List<Student> graduates = this.em
                                .createQuery("SELECT s FROM Student s WHERE FUNCTION('YEAR', s.leavingDate) = :selectedYear", Student.class)
                                .setParameter("selectedYear", selectedYear)
                                .getResultList();

                        TableView<Student> twGraduates = createTableViewForGraduates();

                        ObservableList<Student> observableGraduates = FXCollections.observableArrayList(graduates);
                        twGraduates.setItems(observableGraduates);

                        borderPane.setCenter(twGraduates);

                    } catch (NumberFormatException e) {
                        showErrorAlert("Alert", "Error", "Invalid year format. Please enter a valid year!");
                    }
                }
            });
        });

        btnListAllCourses.setOnAction(a -> {
            Stage currentStage = (Stage) btnListAllCourses.getScene().getWindow();
            BorderPane borderPane = (BorderPane) currentStage.getScene().getRoot();
            borderPane.setCenter(null);

            HBox hbox = new HBox();
            borderPane.setBottom(hbox);

            TextField tfSlotCode = new TextField();
            tfSlotCode.setPromptText("Slot Code");

            TextField tfYear = new TextField();
            tfYear.setPromptText("Year");

            Button btnList = new Button("List Courses");
            hbox.getChildren().addAll(tfSlotCode, tfYear, btnList);

            btnList.setOnAction(c -> {
                borderPane.setCenter(null);

                if (tfSlotCode.getText().isEmpty()) {
                    showErrorAlert("Alert", "Error", "Please enter a valid input for slot code");
                } else {
                    String selectedSlotCode = tfSlotCode.getText();
                    String year = tfYear.getText();

                    em.getTransaction().begin();

                    String queryString = "SELECT s FROM SlotAndCourse s WHERE s.ck.slotCode = :slotCode";

                    if (!year.isEmpty()) {
                        queryString += " AND s.yearTaken = :yearTaken";
                    }

                    TypedQuery<SlotAndCourse> query = em.createQuery(queryString, SlotAndCourse.class)
                            .setParameter("slotCode", selectedSlotCode);

                    if (!year.isEmpty()) {
                        query.setParameter("yearTaken", Integer.parseInt(year));
                    }

                    List<SlotAndCourse> coursesForSlot = query.getResultList();

                    em.getTransaction().commit();

                    TableView<SlotAndCourse> courseTableView = createTableViewForCourses();

                    ObservableList<SlotAndCourse> observableCourses = FXCollections.observableArrayList(coursesForSlot);
                    courseTableView.setItems(observableCourses);

                    borderPane.setCenter(courseTableView);
                }
            });
        });

        btnCalculateGPA.setOnAction(e -> {
            Stage currentStage = (Stage) btnCalculateGPA.getScene().getWindow();
            BorderPane borderPane = (BorderPane) currentStage.getScene().getRoot();
            borderPane.setCenter(null);

            HBox hbox = new HBox();
            TextField tfStudentNumber = new TextField();
            tfStudentNumber.setPromptText("Enter Student Number");
            Button btnCalculate = new Button("Calculate GPA");

            hbox.getChildren().addAll(tfStudentNumber, btnCalculate);
            borderPane.setBottom(hbox);

            btnCalculate.setOnAction(event -> {
                borderPane.setBottom(null);

                if (tfStudentNumber.getText().isEmpty()) {
                    showErrorAlert("Alert", "Error", "Please enter a valid student number!");
                } else {
                    try {
                        String studentNumber = tfStudentNumber.getText();

                        double gpa = calculateGraduateGPA(studentNumber);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("GPA Calculation");
                        alert.setHeaderText(null);
                        alert.setContentText("GPA for Student " + studentNumber + ": " + gpa);
                        alert.showAndWait();
                    } catch (NumberFormatException ex) {
                        showErrorAlert("Alert", "Error", "Invalid student number format. Please enter a valid student number!");
                    }
                }
            });
        });

    }

    private static void readAndInsertFromFile(String filePath) {

        try (Scanner scanner = new Scanner(new File(filePath))) {

            Student student = new Student();
            Slot slot = new Slot();
            Course course = new Course();
            CCR ccr = new CCR();
            SlotAndCourse slotAndCourse = new SlotAndCourse();
            
            int currentSemester = 1;
            int countAttempts = 0;
            boolean inStudentSection = true;

            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split("\\s+");

                if (inStudentSection) {

                    switch (parts[0]) {
                        case "majorleavingdate":
                            if (student != null) {
                                Date leavingDate = new SimpleDateFormat("dd MM yyyy").parse(parts[1] + " " + parts[2] + " " + parts[3]);
                                student.setLeavingDate(leavingDate);
                            }
                            break;

                        case "studentnumber":
                            student.setStudentNumber(parts[1]);
                            break;

                        case "minor":
                            if (student != null) {
                                student.setMinorDegree(Boolean.parseBoolean(parts[1]));
                            }
                            break;
                        case "semester":
                            currentSemester = Integer.parseInt(parts[1]);
                            inStudentSection = false;
                        default:
                            break;
                    }
                } else {

                    if (parts[0].equals("numberofattempts")) {
                        countAttempts = Integer.parseInt(parts[1]);
                        continue;
                    }

                    if (parts[0].equals("semester")) {
                        currentSemester = Integer.parseInt(parts[1]);
                        continue;
                    }

                    if (countAttempts == 0) {
                        em.getTransaction().begin();
                        course = new Course(parts[0]);
                        slot = new Slot(parts[0], parts[1]);
                        slot.setCredit(Integer.parseInt(parts[2]));
                        slot.setSemester(currentSemester);
                        em.merge(slot);
                        em.merge(course);
                        em.getTransaction().commit();
                        continue;
                    }

                    if (countAttempts > 0) {
                        em.getTransaction().begin();
                        slotAndCourse.setYearTaken(Integer.parseInt(parts[0]));
                        slotAndCourse.setTermTaken(parts[1]);
                        slotAndCourse.setGrade(parts[2]);
                        slotAndCourse.setSlot(slot);
                        slotAndCourse.setCourse(course);

                        CCRId ccrid = new CCRId();
                        ccrid.setStudentNumber(student.getStudentNumber());
                        ccr.setStudent(student);
                        ccr.setCCRId(ccrid);

                        CKForSlotAndCourse ck = new CKForSlotAndCourse();
                        ck.setSlotCode(slot.getSlotCode());
                        ck.setCourseCode(course.getCourseCode());
                        ck.setStudentNumber(student.getStudentNumber());
                        slotAndCourse.setCk(ck);
                        slotAndCourse.setCcr(ccr);
                        slotAndCourse.getCcr().setStudent(student);

                        em.merge(slotAndCourse);
                        em.getTransaction().commit();
                        countAttempts--;
                        continue;
                    }
                }
            }

            em.getTransaction().begin();
            em.persist(student);
            em.persist(ccr);
            em.getTransaction().commit();

        } catch (Exception e) {
            
        }
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private TableView<Student> createTableViewForGraduates() {
        TableView<Student> twGraduates = new TableView<>();

        TableColumn<Student, String> studentNumberColumn = new TableColumn<>("Student Number");
        studentNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentNumber()));

        TableColumn<Student, String> leavingDateColumn = new TableColumn<>("Leaving Date");
        leavingDateColumn.setCellValueFactory(cellData -> {
            Date date = cellData.getValue().getLeavingDate();
            String formattedDate = (date != null) ? new SimpleDateFormat("yyyy-MM-dd").format(date) : "";
            return new SimpleStringProperty(formattedDate);
        });

        TableColumn<Student, String> minorDegreeColumn = new TableColumn<>("Minor Degree");
        minorDegreeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Boolean.toString(cellData.getValue().getMinorDegree())));

        twGraduates.getColumns().addAll(studentNumberColumn, leavingDateColumn, minorDegreeColumn);

        return twGraduates;
    }

    private TableView<SlotAndCourse> createTableViewForCourses() {
        TableView<SlotAndCourse> courseTableView = new TableView<>();

        TableColumn<SlotAndCourse, String> courseCodeColumn = new TableColumn<>("Course Code");
        courseCodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse().getCourseCode()));

        TableColumn<SlotAndCourse, String> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getYearTaken())));

        TableColumn<SlotAndCourse, String> termTakenColumn = new TableColumn<>("Term");
        termTakenColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTermTaken()));

        courseTableView.getColumns().addAll(courseCodeColumn, yearColumn, termTakenColumn);

        return courseTableView;
    }

    private double calculateGraduateGPA(String studentNumber) {

        double totalPoints = 0.0;
        int totalCredits = 0;

        List<SlotAndCourse> courses = em.createQuery("SELECT s FROM SlotAndCourse s WHERE s.ck.studentNumber = :studentNumber", SlotAndCourse.class)
                .setParameter("studentNumber", studentNumber)
                .getResultList();

        for (SlotAndCourse course : courses) {
            if (!course.getGrade().equals("F")) {
                totalPoints += convertGradeToPoints(course.getGrade()) * course.getSlot().getCredit();
                totalCredits += course.getSlot().getCredit();
            }
        }

        if (totalCredits == 0) {
            return 0.0;
        }

        return totalPoints / totalCredits;
    }

    private double convertGradeToPoints(String grade) {
        switch (grade) {
            case "aa":
                return 4.0;
            case "ba":
                return 3.5;
            case "bb":
                return 3.0;
            case "cb":
                return 2.5;
            case "cc":
                return 2.0;
            case "dc":
                return 1.5;
            case "dd":
                return 1.0;
            default:
                return 0.0;
        }
    }
}
