package com.ceg.xml;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Klasa reprezentująca typ zadania (polecenie).
 */
@XmlRootElement(name="tasks")
@XmlType( propOrder = { "name", "text", "taskData" })
public class TaskData {
    String name;
    String text;
    List<TaskData> taskData;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @XmlElement(name="task-data")
    public List<TaskData> getTaskData() {
        return taskData;
    }
    public void setTaskData(List<TaskData> taskData) {
        this.taskData = taskData;
    }
}
