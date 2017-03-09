package com.ceg.gui;

import com.ceg.examContent.Exam;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.commons.lang.SystemUtils;

/**
 * Klasa reprezentująca rozszerzoną, możliwą do przesunięcia zakładkę.
 *
 * Original author:
 * Michael Berry
 *
 * Improvement's author:
 * Paweł Lal
 */
public class DraggableTab extends Tab {

    private Label nameLabel;
    private Text dragText;
    private Stage dragStage;
    private static Stage markerStage;
    private TabPane tabPane;
    private ContextMenu contextMenu;

    /**
     * Definiuje separator, który jest wyświetlany w momencie przeciągania zakładki (czarna pionowa kreska).
     */
    static {
        markerStage = new Stage();
        markerStage.initStyle(StageStyle.UNDECORATED);
        Rectangle marker = new Rectangle(3, 15, Color.web("#000000"));
        StackPane sp = new StackPane();
        sp.getChildren().add(marker);
        markerStage.setScene(new Scene(sp));
    }

    /**
     * Tworzy specjalną zakładkę, która ma możliwość zmiany swojej pozycji przy przeciągnięciu.
     * @param labelText Tekst który ma się pojawić na zakładce.
     */
    public DraggableTab(String labelText) {
        nameLabel = new Label(labelText);
        setGraphic(nameLabel);
        dragStage = new Stage();
        dragStage.initStyle(StageStyle.UNDECORATED);
        StackPane sp = new StackPane();
        sp.setStyle("-fx-background-color:#DDDDDD;");
        dragText = new Text(labelText);
        StackPane.setAlignment(dragText, Pos.CENTER);
        sp.getChildren().add(dragText);
        dragStage.setScene(new Scene(sp));

        contextMenu = createContextMenu();

        if(SystemUtils.IS_OS_WINDOWS) {
            /**
             * Definiuje sposób zachowania zakładki w trakcie jej przeciągania.
             * Ustala kiedy oraz gdzie ma zostać wyświetlona pionowa czarna linia oddzielająca zadania oraz wyświetla ją.
             */
            nameLabel.setOnMouseDragged(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    dragStage.setWidth(nameLabel.getWidth() + 10);
                    dragStage.setHeight(nameLabel.getHeight() + 10);
                    dragStage.setX(t.getScreenX());
                    dragStage.setY(t.getScreenY());
                    dragStage.show();
                    Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                    tabPane = getTabPane();
                    InsertData data = getInsertData(screenPoint);
                    if (data == null || data.getInsertPane().getTabs().isEmpty()) {
                        markerStage.hide();
                    } else {
                        int index = data.getIndex();
                        boolean end = false;
                        if (index == data.getInsertPane().getTabs().size()) {
                            end = true;
                            index--;
                        }
                        Rectangle2D rect = getAbsoluteRect(data.getInsertPane().getTabs().get(index));
                        if (end) {
                            markerStage.setX(rect.getMaxX() + 7);
                        } else {
                            markerStage.setX(rect.getMinX() - 7);
                        }
                        markerStage.setY(rect.getMaxY() - 15);
                        markerStage.show();
                    }
                }
            });

            /**
             * Definiuje sposób zachowania zakładki w trakcie jej 'upuszczenia' po przeciągnięciu.
             * Zmienia kolejność zadań w egzaminie zgodnie z nowym układem, poprawia indeksy zakładek.
             */
            nameLabel.setOnMouseReleased(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    markerStage.hide();
                    dragStage.hide();
                    if (!t.isStillSincePress()) {
                        Point2D screenPoint = new Point2D(t.getScreenX(), t.getScreenY());
                        TabPane oldTabPane = getTabPane();
                        int oldIndex = oldTabPane.getTabs().indexOf(DraggableTab.this);
                        tabPane = oldTabPane;
                        InsertData insertData = getInsertData(screenPoint);
                        if (insertData != null) {
                            int addIndex = insertData.getIndex();
                            if (oldTabPane == insertData.getInsertPane() && oldTabPane.getTabs().size() == 1) {
                                return;
                            }
                            GUIMainController mainInstance = GUIMainController.getInstance();
                            mainInstance.setStatus(GUIMainController.Status.DRAG);
                            oldTabPane.getTabs().remove(DraggableTab.this);
                            if (oldIndex < addIndex && oldTabPane == insertData.getInsertPane()) {
                                addIndex--;
                            }
                            if (addIndex > insertData.getInsertPane().getTabs().size()) {
                                addIndex = insertData.getInsertPane().getTabs().size();
                            }
                            Exam.getInstance().changeTasksOrder(oldIndex, addIndex);
                            insertData.getInsertPane().getTabs().add(addIndex, DraggableTab.this);
                            insertData.getInsertPane().selectionModelProperty().get().select(addIndex);
                            mainInstance.updateTabPaneTabIndexes();
                            mainInstance.setStatus(GUIMainController.Status.SWITCH);
                            Exam.getInstance().idx = addIndex;
                            return;
                        }
                    }
                }

            });
        }
    }


    private InsertData getInsertData(Point2D screenPoint) {
            Rectangle2D tabAbsolute = getAbsoluteRect(tabPane);
            if(tabAbsolute.contains(screenPoint)) {
                int tabInsertIndex = 0;
                if(!tabPane.getTabs().isEmpty()) {
                    Rectangle2D firstTabRect = getAbsoluteRect(tabPane.getTabs().get(0));
                    if(firstTabRect.getMaxY()+60 < screenPoint.getY() || firstTabRect.getMinY() > screenPoint.getY()) {
                        return null;
                    }
                    Rectangle2D lastTabRect = getAbsoluteRect(tabPane.getTabs().get(tabPane.getTabs().size() - 1));
                    if(screenPoint.getX() < (firstTabRect.getMinX() + firstTabRect.getWidth() / 2)) {
                        tabInsertIndex = 0;
                    }
                    else if(screenPoint.getX() > (lastTabRect.getMaxX() - lastTabRect.getWidth() / 2)) {
                        tabInsertIndex = tabPane.getTabs().size();
                    }
                    else {
                        for(int i = 0; i < tabPane.getTabs().size() - 1; i++) {
                            Tab leftTab = tabPane.getTabs().get(i);
                            Tab rightTab = tabPane.getTabs().get(i + 1);
                            if(leftTab instanceof DraggableTab && rightTab instanceof DraggableTab) {
                                Rectangle2D leftTabRect = getAbsoluteRect(leftTab);
                                Rectangle2D rightTabRect = getAbsoluteRect(rightTab);
                                if(betweenX(leftTabRect, rightTabRect, screenPoint.getX())) {
                                    tabInsertIndex = i + 1;
                                    break;
                                }
                            }
                        }
                    }
                }
                return new InsertData(tabInsertIndex, tabPane);
            }
        return null;
    }

    private Rectangle2D getAbsoluteRect(Control node) {
        return new Rectangle2D(node.localToScreen(node.getBoundsInLocal()).getMinX(),
                node.localToScreen(node.getBoundsInLocal()).getMinY(),
                node.getWidth(),
                node.getHeight());
    }

    private Rectangle2D getAbsoluteRect(Tab tab) {
        Control node = ((DraggableTab) tab).getLabel();
        return getAbsoluteRect(node);
    }

    private Label getLabel() {
        return nameLabel;
    }

    private boolean betweenX(Rectangle2D r1, Rectangle2D r2, double xPoint) {
        double lowerBound = r1.getMinX() + r1.getWidth() / 2;
        double upperBound = r2.getMaxX() - r2.getWidth() / 2;
        return xPoint >= lowerBound && xPoint <= upperBound;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(Label nameLabel) {
        this.nameLabel = nameLabel;
    }

    public Text getDragText() {
        return dragText;
    }

    public void setDragText(Text dragText) {
        this.dragText = dragText;
    }

    /**
     *
     */
    private static class InsertData {

        private final int index;
        private final TabPane insertPane;

        public InsertData(int index, TabPane insertPane) {
            this.index = index;
            this.insertPane = insertPane;
        }

        public int getIndex() {
            return index;
        }

        public TabPane getInsertPane() {
            return insertPane;
        }

    }

    public ContextMenu createContextMenu() {
        MenuItem changeItem = new MenuItem("Zmień nazwę");
        MenuItem moveForwards = new MenuItem("Przesuń w prawo");
        MenuItem moveBackwards = new MenuItem("Przesuń w lewo");

        GUIMainController instance = GUIMainController.getInstance();

        changeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    instance.changeTaskName(event);
                } catch (Exception e) {

                }
            }
        });
        moveForwards.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    instance.setStatus(GUIMainController.Status.DRAG);
                    int idx = instance.tabPane.getSelectionModel().getSelectedIndex();
                    if(idx+1 < Exam.getInstance().getTasks().size()) {
                        instance.tabPane.getTabs().remove(DraggableTab.this);

                        Exam.getInstance().changeTasksOrder(idx, idx + 1);
                        instance.tabPane.getTabs().add(idx + 1, DraggableTab.this);
                        instance.tabPane.getSelectionModel().select(DraggableTab.this);
                        instance.updateTabPaneTabIndexes();
                    }
                    instance.setStatus(GUIMainController.Status.SWITCH);
                } catch (Exception e) {

                }
            }
        });
        moveBackwards.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    instance.setStatus(GUIMainController.Status.DRAG);
                    int idx = instance.tabPane.getSelectionModel().getSelectedIndex();
                    if(idx-1 >= 0) {
                        instance.tabPane.getTabs().remove(DraggableTab.this);

                        Exam.getInstance().changeTasksOrder(idx, idx - 1);
                        instance.tabPane.getTabs().add(idx - 1, DraggableTab.this);
                        instance.tabPane.getSelectionModel().select(DraggableTab.this);
                        instance.updateTabPaneTabIndexes();
                    }
                    instance.setStatus(GUIMainController.Status.SWITCH);
                } catch (Exception e) {

                }
            }
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(changeItem, moveForwards, moveBackwards);
        return menu;
    }

    void enableContextMenu() {
        this.setContextMenu(contextMenu);
    }

    void disableContextMenu() {
        this.setContextMenu(null);
    }

}