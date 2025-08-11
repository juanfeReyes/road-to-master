package helloworld;


import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.stream.SystemOut;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, SystemStubsExtension.class})
public class AppTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Context context;

    @SystemStub
    public EnvironmentVariables environmentVariables;

    @SystemStub
    public SystemOut systemOut;

    @Test
    public void shouldLoadEnvVariables() {
        environmentVariables.set("ENV_NAME", "Unit-test");
        App app = new App();
        app.handleRequest(null, null, context);
        assertThat(systemOut.getLinesNormalized()).contains("Staring image s3 service handling") ;
    }
}
