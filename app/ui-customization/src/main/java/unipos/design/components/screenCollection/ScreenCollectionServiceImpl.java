package unipos.design.components.screenCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.container.GSonHolder;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.design.components.screenCollection.model.ScreenCollection;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jolly on 10.12.2016.
 */
@Service
public class ScreenCollectionServiceImpl implements ScreenCollectionService {
    @Autowired
    ScreenCollectionRepository screenCollectionRepository;

    @Autowired
    SocketRemoteInterface socketRemoteInterface;

    @Override
    public ScreenCollection findByNameAndPublished(String name, boolean published) {
        return screenCollectionRepository.findByNameAndPublished(name, published);
    }

    @Override
    public List<ScreenCollection> findAllByPublished(boolean published) {
        return screenCollectionRepository.findAllByPublished(published);
    }

    @Override
    public List<ScreenCollection> findAll() {
        return screenCollectionRepository.findAll();
    }

    @Override
    public List<String> listnamesByPublished(boolean published) {
        return screenCollectionRepository.findAllByPublished(published).stream().map(x -> x.getName()).collect(Collectors.toList());
    }

    @Override
    public ScreenCollection save(ScreenCollection screenCollection) {
        return screenCollectionRepository.save(screenCollection);
    }

    @Override
    public ScreenCollection publish(String name) {
        ScreenCollection collection = findByNameAndPublished(name, false);
        if (collection != null) {
            ScreenCollection collectionPublished = findByNameAndPublished(name, true);
            if (collectionPublished != null) {
                screenCollectionRepository.delete(collectionPublished);
            }
            collection.setId(null);
            collection.setPublished(true);

            collection = save(collection);

            socketRemoteInterface.sendToAll("/topic/updateScreenSets", GSonHolder.serializeDateGson().toJson(findFirstByPublished())) ;

            return collection;
        }
        return null;
    }

    @Override
    public ScreenCollection setDefault(String screenCollectionName) {
        ScreenCollection collection = findByNameAndPublished(screenCollectionName, false);
        if (collection != null) {
            findAllByPublished(false).forEach(x -> {
                x.setStandart(false);
                save(x);
            });
            ScreenCollection collectionSaved = findByNameAndPublished(screenCollectionName, true);
            screenCollectionRepository.delete(collectionSaved);
            collection.setStandart(true);
            return save(collection);
        }
        return null;
    }

    @Override
    public ScreenCollection findFirstByPublished() {
        return screenCollectionRepository.findFirstByPublished(true);
    }
}
