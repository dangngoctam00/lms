package lms.mailservice;

import lms.commonlib.AvroConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AvroConfig.class)
public class LmsMailServiceConfiguration {
}
