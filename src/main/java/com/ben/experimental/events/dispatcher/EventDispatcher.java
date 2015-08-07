package com.ben.experimental.events.dispatcher;

import com.ben.experimental.events.AbstractEvent;
import com.ben.experimental.events.interfaces.ComponentEvent;
import com.ben.experimental.threads.AbstractThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 8/4/2015.
 */
public class EventDispatcher {
    private static final Logger LOG = LogManager.getLogger(EventDispatcher.class);

    private HashMap<String, List<AbstractThread>> registry;

    private static EventDispatcher instance;

    protected EventDispatcher() {
        registry = new HashMap<String, List<AbstractThread>>();
    }

    public static EventDispatcher getInstance() {
        if(instance == null) {
            instance = new EventDispatcher();
        }
        return instance;
    }

    public static void registerThread(AbstractThread thread) {
        EventDispatcher e = getInstance();
        Set<Class> events = e.findReleventEvents(thread.getComponentEventInterface());
        for(Class event : events) {
            String className = event.getName();
            if(e.registry.get(className) == null) {
                e.registry.put(className, new LinkedList<AbstractThread>());
            }
            e.registry.get(className).add(thread);
        }
    }

    private Set<Class> findReleventEvents(Class event) {
        Reflections reflections = new Reflections(AbstractEvent.class.getPackage().getName());
        Set<Class> subTypes = reflections.getSubTypesOf(event);
        return subTypes;
    }

    public static void dispatch(ComponentEvent componentEvent) {
        EventDispatcher e = getInstance();
        List<AbstractThread> threads = e.registry.get(componentEvent.getClass().getName());
        if(threads != null) {
            for(AbstractThread thread : threads) {
                thread.receiveEvent(componentEvent);
            }
        }
    }
}
