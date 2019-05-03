package unipos.auth.components.right;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import unipos.auth.components.authentication.Token;
import unipos.auth.components.authentication.TokenManagement;
import unipos.auth.components.user.User;
import unipos.auth.components.user.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by Dominik on 28.05.2015.
 */

@Service
public class RightServiceImpl implements RightService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RightRepository rightRepository;
    TokenManagement tokenManagement = TokenManagement.getInstance();

    @Override
    public void create(Right right) {
        rightRepository.save(right);
    }

    @Override
    public List<Right> findAll() {
        return rightRepository.findAll();
    }

    @Override
    public List<RightDto> getRightsPerPartition() {
        List<Right> rights = rightRepository.findAll();

        return rights.stream().map(Right::getPartition).distinct().map(z -> RightDto.builder()
                        .partition(z)
                        .types(rights.stream().filter(asdf -> asdf.getPartition().equals(z)).map(asdf -> RightTypeDto.builder()
                                .name(asdf.getViewName())
                                .selected(false)
                                .guid(asdf.getGuid())
                                .build())
                                .collect(toList()))
                        .build())
                .collect(toList());
    }

    @Override
    public void addRightsToUser(List<String> rightGuids, Long userId) {
        User user = userRepository.findByUserId(userId);
        List<Right> rights = rightRepository.findByGuidIn(rightGuids);

        Assert.notNull(user, "No user found for the given userId!!!");
        Assert.notNull(rights, "No Rights found for the given GUIDs!!!");
        Assert.state(rightGuids.size() == rights.size(), "Not all Guids could be resolved with Rights!!!");

        user.setRights(rights);

        userRepository.save(user);

        List<Token> userToken = tokenManagement.getTokensByUserId(userId);
        if(userToken == null || userToken.isEmpty()) {
            return;
        }

        tokenManagement.updateTokenPermissions(userToken, rights);
    }

    @Override
    public List<String> getRightGuidsOfUser(Long userId) {
        User user = userRepository.findByUserId(userId);

        Assert.notNull(user, "No user found for the given UserId");
        return user.getRights().stream().map(Right::getGuid).collect(Collectors.toList());
    }

    @Override
    public void addRights(List<Right> rights) {
        if(rights.isEmpty())
            return;

        String module = rights.get(0).getName().split("\\.")[0];
        List<Right> dbRights =
                rightRepository.findAll().stream().filter(x -> x.getName().split("\\.")[0].equals(module)).collect(Collectors.toList());

        List<Right> newRights = rights.stream()
                .filter(x -> dbRights.stream().filter(y -> x.getName().equals(y.getName())).count() == 0)
                .map(x -> {
                    if(x.getGuid() == null || x.getGuid().isEmpty()) {
                        x.setGuid(UUID.randomUUID().toString());
                    }
                    return x;
                })
                .collect(toList());
        List<Right> deletedRights = new ArrayList<>(dbRights);
        deletedRights.removeIf(x -> rights.stream().map(Right::getName).anyMatch(z -> z.equalsIgnoreCase(x.getName())));

        newRights.forEach(rightRepository::save);
        deletedRights.forEach(rightRepository::delete);
    }

    @Override
    public void deleteAllFromUser(Long userId) {
        User user = userRepository.findByUserId(userId);

        user.setRights(new ArrayList<>());
        userRepository.save(user);

        List<Token> userToken = tokenManagement.getTokensByUserId(userId);
        if(userToken == null || userToken.isEmpty()) {
            return;
        }

        tokenManagement.updateTokenPermissions(userToken, new ArrayList<>());
    }
}
