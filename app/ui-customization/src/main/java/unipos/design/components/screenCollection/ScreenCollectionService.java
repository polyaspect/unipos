package unipos.design.components.screenCollection;

import unipos.design.components.screenCollection.model.ScreenCollection;

import java.util.List;

/**
 * Created by jolly on 10.12.2016.
 */
public interface ScreenCollectionService {
    ScreenCollection findByNameAndPublished(String name, boolean published);

    List<ScreenCollection> findAllByPublished(boolean published);

    List<ScreenCollection> findAll();

    List<String> listnamesByPublished(boolean published);

    ScreenCollection save(ScreenCollection screenCollection);

    ScreenCollection publish(String name);

    ScreenCollection setDefault(String screenCollectionName);

    ScreenCollection findFirstByPublished();
}
