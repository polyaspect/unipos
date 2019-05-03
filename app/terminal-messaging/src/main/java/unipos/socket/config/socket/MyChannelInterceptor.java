package unipos.socket.config.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import unipos.socket.components.workstation.WorkstationRepository;

import java.lang.reflect.Field;

/**
 * Created by Dominik on 13.08.2015.
 */
public class MyChannelInterceptor extends ChannelInterceptorAdapter {

    @Autowired
    WorkstationRepository workstationRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        if(message.getHeaders().containsValue(SimpMessageType.CONNECT_ACK)) {
            try {
                Class<?> clazz = message.getClass();
                Field field  = clazz.getDeclaredField("payload");
                field.setAccessible(true);
                field.set(message, workstationRepository.findLatestWorkstationRegistration().getDeviceId().getBytes());
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return super.preSend(message, channel);
    }
}
