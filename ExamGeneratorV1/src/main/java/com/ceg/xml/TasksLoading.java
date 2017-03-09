package com.ceg.xml;

import com.ceg.utils.Alerts;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class TasksLoading {

    private static final String filename = "tasks.xml";
    /**
     * Laduje polecenia ze z góry zdefiniowanego pliku .xml i zapisuje w obiekcie TaskData.
     * @return Obiekt zawierający dane o typach zadań.
     */
    public static TaskData loadFromXml() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Tasks.class);
            File file = new File(filename);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            TaskData taskData = (TaskData) unmarshaller.unmarshal(file);

            return taskData;
        } catch (JAXBException e) {
            Alerts.wrongFileContentAlert();
        }
        return null;
    }

    /**
     * Zapisuje polecenia z obiektu TaskData w z góry zdefniowanym pliku .xml
     * @param taskData obiekt typu TaskData zawierający treści zadań
     */
    //todo przydałoby się zabezpieczyć przed błędami zapisu gdyż w momencie nieprawidłowego zapisu treść pliku jest tracona
    public static void saveToXml(TaskData taskData) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Tasks.class);
            File file = new File("tasks.xml");

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(taskData, file);
        } catch (JAXBException e) {
            Alerts.taskDataSavingErrorAlert();
            System.out.println("Cannot save exam. Error caused by: " + e.toString());
        }
    }
}
