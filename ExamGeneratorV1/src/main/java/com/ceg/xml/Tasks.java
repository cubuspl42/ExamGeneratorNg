package com.ceg.xml;

import javax.xml.bind.annotation.*;

/**
 * Klasa przechowująca dane o typach zadań.
 */
@XmlRootElement
public class Tasks {
    TaskData taskData;

    @XmlElement(name="task-data")
    public TaskData getTaskData() {
        return taskData;
    }
    public void setTaskdata(TaskData taskData) {
        this.taskData = taskData;
    }
}
