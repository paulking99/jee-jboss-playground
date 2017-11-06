package com.jee.jboss.playground.transactions.log;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Dependent
public class Slf4jLoggerProducer {

    @Produces
    public Logger produceLogger(InjectionPoint ip){
        return LoggerFactory.getLogger(
                   ip.getMember().getDeclaringClass().getName());
    }
}