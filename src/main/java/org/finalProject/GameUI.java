package org.finalProject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//TODO: Сгенерирован с GPT. В будущем нужно исследовать и довести до ума
public class GameUI extends Application {

    private static final int WIDTH = LiveGame.WIDTH;   // Ширина острова
    private static final int HEIGHT = LiveGame.HEIGHT; // Высота острова

    private GridPane gridPane;  // Поле для сетки

    @Override
    public void start(Stage primaryStage) {
        gridPane = new GridPane();  // Создаем сетку

        // Инициализируем пустую сетку при запуске
        initializeGrid();

        // Создаем сцену и задаем окно
        Scene scene = new Scene(gridPane, 600, 600);
        primaryStage.setTitle("Island Ecosystem");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Метод для инициализации сетки (например, пустыми значениями)
    private void initializeGrid() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Label label = new Label("🌊");  // Пустая клетка
                label.setStyle("-fx-border-color: black; -fx-padding: 10;");
                gridPane.add(label, j, i);  // Размещаем клетки на GridPane
            }
        }
    }

    // Метод для обновления содержимого сетки в зависимости от состояния островов
    public void updateGrid(Island[][] islands) {
        gridPane.getChildren().clear();  // Очищаем текущие элементы перед обновлением

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Label label = new Label(getIslandContent(islands[i][j]));
                label.setStyle("-fx-border-color: black; -fx-padding: 10;");
                gridPane.add(label, j, i);  // Добавляем обновленный элемент на GridPane
            }
        }
    }

    // Метод для получения содержимого клетки (что находится на острове)
    private String getIslandContent(Island island) {
        if (island.getAliveObjectsList().isEmpty()) {
            return "🌊";  // Пустая клетка (вода)
        } else {
            return island.getAliveObjectsList().get(0).toString();  // Возвращаем содержимое клетки (например, животное или растение)
        }
    }

//    public static void main(String[] args) {
//        launch(args);  // Запуск JavaFX приложения
//    }
}
