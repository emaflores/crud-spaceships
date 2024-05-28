package com.emaflores.spaceships.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.emaflores.spaceships.controller.SpaceshipController.getSpaceshipById(..)) && args(id,..)")
    public void logBefore(JoinPoint joinPoint, String id) {
        try {
            Long spaceshipId = Long.parseLong(id);
            if (spaceshipId < 0) {
                logger.warn("Attempted to find spaceship with negative ID: {}", spaceshipId);
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid ID format: {}", id);
        }
    }
}