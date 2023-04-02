package school.hei.haapi.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import school.hei.haapi.SentryConf;
import school.hei.haapi.endpoint.rest.api.PayingApi;
import school.hei.haapi.endpoint.rest.client.ApiClient;
import school.hei.haapi.endpoint.rest.client.ApiException;
//import school.hei.haapi.endpoint.rest.model.CreateDelayPenaltyChange;
import school.hei.haapi.endpoint.rest.security.cognito.CognitoComponent;
import school.hei.haapi.integration.conf.AbstractContextInitializer;
import school.hei.haapi.integration.conf.TestUtils;
import school.hei.haapi.model.DelayPenalty;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static school.hei.haapi.endpoint.rest.model.DelayPenalty.InterestTimerateEnum.DAILY;
import static school.hei.haapi.integration.conf.TestUtils.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = DelayPenaltyIT.ContextInitializer.class)
@AutoConfigureMockMvc
class DelayPenaltyIT {
  @MockBean
  private SentryConf sentryConf;
  @MockBean
  private CognitoComponent cognitoComponentMock;

  private static ApiClient anApiClient(String token) {
    return TestUtils.anApiClient(token, DelayPenaltyIT.ContextInitializer.SERVER_PORT);
  }

  public static DelayPenalty delayPenalty1() {
    DelayPenalty delayPenalty = new DelayPenalty();
    delayPenalty.setId("delay_penalty_id1");
    delayPenalty.setCreationDatetime(Instant.parse("2021-11-01T09:36:37.00Z"));
    delayPenalty.setInterestPercent(2);
    delayPenalty.setInterestTimerate(DAILY);
    delayPenalty.setGraceDelay(0);
    delayPenalty.setApplicabilityDelayAfterGrace(0);
    return delayPenalty;
  }

  public static DelayPenalty delayPenalty2() {
    DelayPenalty delayPenalty = new DelayPenalty();
    delayPenalty.setId("delay_penalty_id2");
    delayPenalty.setCreationDatetime(Instant.parse("2022-11-01T09:36:37.00Z"));
    delayPenalty.setInterestPercent(5);
    delayPenalty.setInterestTimerate(DAILY);
    delayPenalty.setGraceDelay(0);
    delayPenalty.setApplicabilityDelayAfterGrace(0);
    return delayPenalty;
  }

  @BeforeEach
  void setUp() {
    setUpCognito(cognitoComponentMock);
  }

  @Test
  void student_read_ok() throws ApiException {
    ApiClient student1Client = anApiClient(TEACHER1_TOKEN);
    PayingApi api = new PayingApi(student1Client);

    school.hei.haapi.endpoint.rest.model.DelayPenalty actualDelayPenalty = api.getDelayPenalty();

    assertEquals(actualDelayPenalty, delayPenalty2());
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}
