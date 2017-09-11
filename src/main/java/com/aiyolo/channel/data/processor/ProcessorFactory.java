package com.aiyolo.channel.data.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessorFactory {

    private static Log errorLogger = LogFactory.getLog("errorLog");

    @SuppressWarnings("unchecked")
    public <T extends Processor> T getProcessor(String className) {
        T processor = null;
        try {
            String packageName = this.getClass().getPackage().getName();
            processor = (T) Class.forName(packageName + "." + className).newInstance();
        } catch (Exception e) {
            errorLogger.error("ProcessorFactory异常！Get processor:" + className, e);
        }
        return processor;
    }

}
