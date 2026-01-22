package UMC_7th.Closit.global.config;

import UMC_7th.Closit.global.interceptor.ViewCountInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ViewCountInterceptor viewCountInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(viewCountInterceptor)
                .addPathPatterns("/api/v1/communities/battle/**");
    }
}
