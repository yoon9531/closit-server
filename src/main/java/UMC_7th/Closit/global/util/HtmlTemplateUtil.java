package UMC_7th.Closit.global.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlTemplateUtil {

    public static String loadHtml(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource("static/" + filename);
            Path path = resource.getFile().toPath();
            return Files.readString(path);
        } catch (IOException e) {
            return "<h1>페이지 로드 실패</h1>";
        }
    }
}
