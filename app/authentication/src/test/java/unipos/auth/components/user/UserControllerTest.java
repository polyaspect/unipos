package unipos.auth.components.user;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import unipos.auth.config.web.SecurityConfiguration;
import unipos.auth.shared.AbstractRestControllerTest;
import unipos.common.container.GSonHolder;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractRestControllerTest {

    @Autowired
    UserService userService;

    @Test
    public void testFindAll() throws Exception {
        when(userService.findAllUsers()).thenReturn(Arrays.asList(new User("dominik", "1234", false), new User("gradnigger", "4321", true)));

        ObjectMapper mapper = new ObjectMapper();

        MvcResult mvcResult = mockMvc.perform(get("/users")).andExpect(status().is2xxSuccessful()).andReturn();

        List<User> users = mapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<User>>() {}
        );

        assertThat(users.size(), is(2));
    }

    @Test
    public void testFindUserByUsername() throws Exception {
        when(userService.findUserByUsername(anyString())).thenReturn(new User("Dominik", "password", true));

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult mvcResult = mockMvc.perform(get("/users/username/Dominik"))
                .andExpect(status().isOk())
                .andReturn();

        User user = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<User>() {}
        );

        assertThat(user.getName(), is("Dominik"));
    }

    @Test
    public void testFindUserById() throws Exception {
        User dbUser = new User("Dominik", "password", true);
        dbUser.setId("556a0d0d0e1e6baf81aedf9d");

        when(userService.findUserById(anyString())).thenReturn(dbUser);

        MvcResult mvcResult = mockMvc.perform(get("/users/id/556a0d0d0e1e6baf81aedf9d"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        User assertUser = mapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<User>() {}
        );

        assertThat(assertUser.getName(), is("Dominik"));
        assertThat(assertUser.getId(), is("556a0d0d0e1e6baf81aedf9d"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User dbUser = new User("Dominik", "asdf", true);

        doNothing().when(userService).createUser(any(User.class));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(GSonHolder.serializeDateGson().toJson(new User("Dominik", "Schiener", true, null, "companyGuid"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveUserById() throws Exception {
        doNothing().when(userService).deleteUser(anyString());

        mockMvc.perform(delete("/users/id")
                .param("id", "3uijhgt67890op"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveUserByUsername() throws Exception {
        doNothing().when(userService).deleteUserByUsername(anyString());

        mockMvc.perform(delete("/users/username")
                .param("username", "Dominik"))
                .andExpect(status().isOk());
    }
}