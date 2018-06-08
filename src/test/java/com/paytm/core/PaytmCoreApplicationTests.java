package com.paytm.core;

import com.paytm.core.domain.CustomAuthorityEnum;
import com.paytm.core.domain.UserModel;
import com.paytm.core.repository.UserModelRepository;
import com.paytm.core.service.TweetsService;
import com.paytm.core.service.client.TwitterClient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaytmCoreApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaytmCoreApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    UserModelRepository userModelRepository;

    @Autowired
    TwitterClient twitterClient;

    @Autowired
    TweetsService tweetsService;

    @Autowired
    PasswordEncoder encoder;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        userModelRepository.save(new UserModel(username, encoder.encode(password), Collections.singleton(CustomAuthorityEnum.ROLE_APPUSER)));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "acme");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("acme","acmesecret"))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    public void testTwitterClient() throws IOException {
        twitterClient.search("spring").stream().forEach(System.out::println);
    }

    @Test
    public void testTwitterController_searchTweets() throws Exception {
        String accessToken = obtainAccessToken("username","password");
        System.out.println("accessToken: " + accessToken);
        mockMvc.perform(get("/tweets/search/spring")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testTwitterController_IFeelLucky() throws Exception {
        String accessToken = obtainAccessToken("username","password");
        System.out.println("accessToken: " + accessToken);
        mockMvc.perform(get("/tweets/lucky/test")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoToken_whenAccessResource_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/HelloWorld"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenToken_whenAccessResource_thenOk() throws Exception {
        String accessToken = obtainAccessToken("username", "password");
        mockMvc.perform(get("/HelloWorld")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUniqueName_whenRegisterUser_thenCreated() throws Exception {
        mockMvc.perform(post("/users/register")
                .param("username", "newuser")
                .param("password", "password"))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenDuplicateName_whenRegisterUser_thenForbidden() throws Exception {
        mockMvc.perform(post("/users/register")
                .param("username", "username")
                .param("password", "password"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenCorrectOldPassword_whenChangePassword_thenAccepted() throws Exception {
        String accessToken = obtainAccessToken("username", "password");
        mockMvc.perform(post("/users/changePassword")
                .header("Authorization", "Bearer " + accessToken)
                .param("oldPassword","password")
                .param("newPassword","password"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void givenWrongOldPassword_whenChangePassword_thenUnauthorized() throws Exception {
        String accessToken = obtainAccessToken("username", "password");
        mockMvc.perform(post("/users/changePassword")
                .header("Authorization", "Bearer " + accessToken)
                .param("oldPassword","wrongpass")
                .param("newPassword","newpass"))
                .andExpect(status().isUnauthorized());
    }
}
