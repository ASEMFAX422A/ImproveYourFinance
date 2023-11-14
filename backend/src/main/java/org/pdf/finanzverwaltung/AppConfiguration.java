package org.pdf.finanzverwaltung;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import javax.crypto.SecretKey;

import org.pdf.finanzverwaltung.dto.SettingsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.jsonwebtoken.Jwts;

/**
 * AppConfiguration
 */
@Component
@Configuration
public class AppConfiguration {

    @Autowired
    private Environment environment;

    private SecretKey jwtSecretKey = Jwts.SIG.HS256.key().build();

    public AppConfiguration() {
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        final String dateTimeFormat = environment.getProperty("spring.jackson.date-format", "yyyy-MM-dd");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);

        return builder -> {
            builder.dateFormat(simpleDateFormat);
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers(new DateSerializer(false, simpleDateFormat));
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }

    public SecretKey getJwtKey() {
        return jwtSecretKey;
    }

    public int getJwtExpirationMs() {
        return environment.getProperty("security.jwt.token-expiration-ms", Integer.class, 1800000);
    }

    public int getUsernameMinLength() {
        return environment.getProperty("security.username.min-length", Integer.class, 3);
    }

    public int getPasswordMinLength() {
        return environment.getProperty("security.password.min-length", Integer.class, 6);
    }

    public int getPasswordMinNumbers() {
        return environment.getProperty("security.password.min-numbers", Integer.class, 1);
    }

    public int getPasswordMinSpecialCharacters() {
        return environment.getProperty("security.password.min-special-characters", Integer.class, 1);
    }

    public File getAppFolder() {
        return new File(environment.getProperty("app.data-folder", "./"));
    }

    public SettingsDto getSettings() {
        return new SettingsDto(getPasswordMinLength(), getPasswordMinNumbers(), getPasswordMinSpecialCharacters(),
                getUsernameMinLength());
    }
}
