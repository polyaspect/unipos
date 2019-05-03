package unipos.data.components.sync;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.data.components.shared.GSonHolder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dominik on 01.11.15.
 */
public abstract class SyncController<T> {

    @Autowired
    MongoTemplate mongoTemplate;

    Gson gson = GSonHolder.serializeDateGson();

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void syncCreate(@RequestParam String logCreation) {


        Class<T> persistentClass = (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];

        T log = gson.fromJson(logCreation, persistentClass);

        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(log.getClass().getDeclaredFields()));
        if (fields.stream().anyMatch(x -> new ArrayList<>(Arrays.asList(x.getDeclaredAnnotations())).stream().anyMatch(y -> y instanceof Id || y instanceof javax.persistence.Id))) {
            Field idFiled = fields.stream().filter(x -> new ArrayList<>(Arrays.asList(x.getDeclaredAnnotations())).stream().anyMatch(y -> y instanceof Id || y instanceof javax.persistence.Id)).findFirst().get();
            try {
                idFiled.setAccessible(true);
                idFiled.set(log, null);
                idFiled.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        saveEntity(log);

        //That the Sequence number is always up to date, I need to increment it also after a synchronisation
        updateSequenceNumber(log);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void syncDelete(@RequestParam String logCreation) {


        Class<T> persistentClass = (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];

        T log = gson.fromJson(logCreation, persistentClass);

        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(log.getClass().getDeclaredFields()));
        if (fields.stream().anyMatch(x -> new ArrayList<>(Arrays.asList(x.getDeclaredAnnotations())).stream().anyMatch(y -> y instanceof Id || y instanceof javax.persistence.Id))) {
            Field idFiled = fields.stream().filter(x -> new ArrayList<>(Arrays.asList(x.getDeclaredAnnotations())).stream().anyMatch(y -> y instanceof Id || y instanceof javax.persistence.Id)).findFirst().get();
            try {
                idFiled.setAccessible(true);
                idFiled.set(log, null);
                idFiled.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        deleteEntity(log);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void syncUpdate(@RequestParam String logCreation) {


        Class<T> persistentClass = (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];

        T log = gson.fromJson(logCreation, persistentClass);

        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(log.getClass().getDeclaredFields()));
        if (fields.stream().anyMatch(x -> new ArrayList<>(Arrays.asList(x.getDeclaredAnnotations())).stream().anyMatch(y -> y instanceof Id || y instanceof javax.persistence.Id))) {
            Field idFiled = fields.stream().filter(x -> new ArrayList<>(Arrays.asList(x.getDeclaredAnnotations())).stream().anyMatch(y -> y instanceof Id || y instanceof javax.persistence.Id)).findFirst().get();
            try {
                idFiled.setAccessible(true);
                idFiled.set(log, null);
                idFiled.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        updateEntity(log);
    }

    /**
     * This method should do the operations to delete the Sync Entity from the Database
     * @param entity the entity we need to delete
     */
    protected abstract void deleteEntity(T entity);

    public abstract void saveEntity(T entity);

    protected abstract void updateEntity(T log);

    /**
     * This method updates the SequenceNumber, so that these number are also synchronized.
     * @param entity This entity contains the Information about the sequence Number we need to update
     */
    protected abstract void updateSequenceNumber(T entity);
}
