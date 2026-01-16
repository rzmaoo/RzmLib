package com.rzmao.rzmlib.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RzmLogger {
    // 全局 Debug 模式
    private static boolean debugEnabled = true;
    private final Logger logger;
    private final String prefix;

    private RzmLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
        this.prefix = "[Rzm/" + name + "] ";
    }

    public static RzmLogger get(String modId) {
        return new RzmLogger(modId);
    }

    public static void setDebug(boolean enabled) {
        debugEnabled = enabled;
    }

    public void info(String message) {
        logger.info("{}{}", prefix, message);
    }

    public void warn(String message) {
        logger.warn("{}{}", prefix, message);
    }

    public void error(String message) {
        logger.error("{}{}", prefix, message);
    }

    public void error(String message, Throwable t) {
        logger.error("{}{}", prefix, message, t);
    }

    public void debug(String message) {
        if (debugEnabled) {
            logger.info("{}[DEBUG] {}", prefix, message);
        }
    }
}