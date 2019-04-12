//package view;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.EventHandler;
//import javafx.application.Platform;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import model.Cook;
//
//public class MCookView {
//    private Scene MainScene;
//    private Stage stage;
//    private ResultSet resultSet;
//    private TableView<String> table = new TableView();
//    ObservableList<Cook> data ;
//
//    //private Label label;
//    private TableView<Cook> tableBook = new TableView() ;
//    private TableColumn<Cook, String> columnTitle = new TableColumn("FirstName");
//
//    Group group = new Group();
//    Scene scene = new Scene(group, 980, 630);
//    GridPane gridPane = new GridPane();
//
//    public MCookView( Stage primaryStage , Scene s , ResultSet x ) throws ClassNotFoundException, SQLException {
//        dbm = DBMaster.getDBMaster();
//        MainScene = s;
//        resultSet = x;
//
//        addFunctionality();
//        stage = primaryStage;
//        trial();
//        // ViewerPage();
//    }
//    public void trial() throws SQLException
//    {
//        Back.setText("< Back");
//        Back.setPrefSize(119, 35);
//        Forward.setText("Forward >");
//        Forward.setPrefSize(119, 35);
//        //gridPane.add(Back, 15, 23);
//
//
//        data = FXCollections.observableArrayList();
//        columnFirst.setCellValueFactory(
//                new PropertyValueFactory<Cook,String>("isbn"));
//        columnLast.setCellValueFactory(
//                new PropertyValueFactory<Cook,String>("Title"));
//
//        tableBook.autosize();
//
//        tableBook.getColumns().addAll( columnISBN,columnTitle,columnCategory, columnPublisherID,columnYear,columnPrice,
//                columnQuantity,columnThreshold);
//
//        final VBox vbox = new VBox();
//        vbox.getChildren().add(tableBook);
//        vbox.getChildren().add(Back);
//        vbox.getChildren().add(Forward);
//
//        ((Group) scene.getRoot()).getChildren().addAll(vbox);
//
//        show();
//
//
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                stage.setScene(scene);
//                stage.show();
//            }
//        });
//    }
//
//    private void show() throws SQLException{
//        pagesOffset=0;
//        if(pagesCounter < allData.size()){
//            tableBook.setItems(allData.get(pagesCounter));
//            pagesCounter++;
//        }
//        else{
//            data = FXCollections.observableArrayList();
//            boolean hasNext = resultSet.next();
//            while(hasNext){
//                Cook b = new Cook();
//                data.add(
//                        new Cook(
//                                resultSet.getString("FirstName"),
//                                resultSet.getString("LastName")
//
//                        ));
//                pagesOffset++;
//                if(pagesOffset==10)
//                    break;
//                hasNext = resultSet.next();
//            }
//            if(data.size()!=0){
//                allData.add(data);
//                pagesCounter++;
//            }
//
//
//            tableBook.setItems(null);
//
//            tableBook.setItems(data);
//        }
//
//
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                stage.setScene(scene);
//                stage.show();
//            }
//        });
//
//    }
//
//
//    private void showPrevious(){
//        pagesCounter--;
//        tableBook.setItems(null);
//        tableBook.setItems(allData.get(pagesCounter));
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                stage.setScene(scene);
//                stage.show();
//            }
//        });
//    }
//    void addFunctionality(){
//        Back.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent arg0) {
//                if(pagesCounter==0){
//                    stage.setScene(MainScene);
//                    stage.show();
//                }
//                else{
//                    showPrevious();
//                }
//
//            }
//        });
//    }
//
//}