package com.redhat.cleanbase.log.appender;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import com.redhat.cleanbase.log.field.CustomizedLogField;
import lombok.val;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OutputStreamAppenderCustomizer implements AppenderCustomizerCondition {

    public static final String WHITE_SPACE = " ";
    public static final Pattern COMPILE = Pattern.compile("\\[.+]");

    @Override
    public boolean isSupported(Appender<ILoggingEvent> loggingEventAppender) {
        return loggingEventAppender instanceof OutputStreamAppender<ILoggingEvent>;
    }

    @Override
    public void customize(Appender<ILoggingEvent> appender, List<? extends CustomizedLogField> customizedLogFields) {

        if (CollectionUtils.isEmpty(customizedLogFields)) {
            return;
        }

        if (appender instanceof OutputStreamAppender<ILoggingEvent> outputStreamAppender) {
            if (outputStreamAppender.getEncoder() instanceof PatternLayoutEncoder patternLayoutEncoder) {
                val encoderPattern = patternLayoutEncoder.getPattern();
                val newEncoderPattern = getNewEncoderPattern(encoderPattern, customizedLogFields);
                patternLayoutEncoder.setPattern(newEncoderPattern);
                patternLayoutEncoder.start();
            }
        }
    }

    protected int getPlaceIndex(String encoderPattern) {
        val matcher = COMPILE.matcher(encoderPattern);
        if (!matcher.find()) {
            return 0;
        }
        return matcher.end();
    }

    protected String getNewEncoderPattern(String encoderPattern, List<? extends CustomizedLogField> customizedLogFields) {
        val placeIndex = getPlaceIndex(encoderPattern);
        return encoderPattern.substring(0, placeIndex)
                + WHITE_SPACE
                + getCustomizedLogFormat(customizedLogFields)
                + encoderPattern.substring(placeIndex);
    }

    protected String getCustomizedLogFormat(List<? extends CustomizedLogField> customizedLogFields) {
        return customizedLogFields.stream()
                .map(CustomizedLogField::name)
                .map((name) -> "[%X{" + name + "}]")
                .collect(Collectors.joining(WHITE_SPACE));
    }

}
