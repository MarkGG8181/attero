package fag.ware.client.event.data;

import fag.ware.client.Fagware;
import fag.ware.client.event.Event;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private final Map<Class<?>, List<RegisteredListener>> listeners = new ConcurrentHashMap<>();

    public void register(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) continue;

                method.setAccessible(true);

                Class<?> eventClass = params[0];
                Subscribe sub = method.getAnnotation(Subscribe.class);

                RegisteredListener listener = new RegisteredListener(obj, method, sub.priority());

                listeners.computeIfAbsent(eventClass, c -> new ArrayList<>()).add(listener);
                listeners.get(eventClass).sort(Comparator.comparingInt(l -> -l.priority));

                //Fagware.LOGGER.info("Registered listener: {} with priority {}", method, sub.priority());
            }
        }
    }

    public void unregister(Object obj) {
        for (List<RegisteredListener> list : listeners.values()) {
            list.removeIf(listener -> listener.owner == obj);
        }
        //Fagware.LOGGER.info("Unregistered listener: {}", obj.getClass().getName());
    }

    public void post(Event event) {
        List<RegisteredListener> list = listeners.get(event.getClass());
        if (list == null) return;

        for (RegisteredListener listener : list) {
            try {
                listener.method.invoke(listener.owner, event);
            } catch (Exception e) {
                Fagware.LOGGER.error("Error invoking event listener", e);
            }
        }
    }

    private record RegisteredListener(Object owner, Method method, int priority) {
    }
}