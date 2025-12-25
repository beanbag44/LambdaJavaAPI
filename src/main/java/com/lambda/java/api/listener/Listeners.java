package com.lambda.java.api.listener;

import com.lambda.context.SafeContext;
import com.lambda.event.Event;
import com.lambda.event.EventFlow;
import com.lambda.event.listener.SafeListener;
import com.lambda.event.listener.UnsafeListener;
import com.lambda.threading.ThreadingKt;
import kotlin.Unit;
import kotlin.jvm.JvmClassMappingKt;
import kotlinx.coroutines.CoroutineDispatcher;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @see SafeListener.Companion
 * @see UnsafeListener.Companion
 */
@SuppressWarnings("unused")
public class Listeners {
    public static <T extends Event> SafeListener<T> listen(
            @NotNull Object owner,
            @NotNull Class<T> eventClass,
            @NotNull BiConsumer<SafeContext, T> handler
    ) {
        return listen(owner, eventClass, 0, false, handler);
    }

    public static <T extends Event> SafeListener<T> listen(
            @NotNull Object owner,
            @NotNull Class<T> eventClass,
            int priority,
            @NotNull BiConsumer<SafeContext, T> handler
    ) {
        return listen(owner, eventClass, priority, false, handler);
    }

    public static <T extends Event> SafeListener<T> listen(
            @NotNull Object owner,
            @NotNull Class<T> eventClass,
            int priority,
            boolean alwaysListen,
            @NotNull BiConsumer<SafeContext, T> handler
    ) {
        return SafeListener.Companion.listen(
                owner,
                JvmClassMappingKt.getKotlinClass(eventClass),
                priority,
                alwaysListen,
                (sc, event) -> {
                    handler.accept(sc, event);
                    return Unit.INSTANCE;
                }
        );
    }

    public static <T extends Event> AtomicReference<T> listenOnce(
            @NotNull Object owner,
            @NotNull Class<T> eventClass,
            int priority,
            boolean alwaysListen,
            @NotNull BiPredicate<SafeContext, T> predicate
    ) {
        AtomicReference<T> pointer = new AtomicReference<>(null);

        AtomicReference<SafeListener<T>> listenerHolder = new AtomicReference<>();

        SafeListener<T> listener = listen(
                owner,
                eventClass,
                priority,
                alwaysListen,
                (sc, event) -> {
                    pointer.set(event);

                    if (predicate.test(sc, event)) {
                        EventFlow.INSTANCE.getSyncListeners().unsubscribe(listenerHolder.get());
                    }
                }
        );

        listenerHolder.set(listener);
        EventFlow.INSTANCE.getSyncListeners().subscribe(JvmClassMappingKt.getKotlinClass(eventClass), listener);

        return pointer;
    }

    public static <T extends Event> SafeListener<T> listenConcurrently(
            @NotNull Object owner,
            @NotNull Class<T> eventClass,
            int priority,
            boolean alwaysListen,
            @NotNull CoroutineDispatcher scheduler,
            @NotNull BiConsumer<SafeContext, T> handler
    ) {
        SafeListener<T> listener = new SafeListener<>(
                priority,
                owner,
                alwaysListen,
                (sc, event) -> {
                    ThreadingKt.runConcurrent(scheduler, (scope, continuation) -> {
                        handler.accept(sc, event);
                        return Unit.INSTANCE;
                    });
                    return Unit.INSTANCE;
                }
        );

        EventFlow.INSTANCE.getConcurrentListeners().subscribe(
                JvmClassMappingKt.getKotlinClass(eventClass),
                listener
        );

        return listener;
    }

    public static <T extends Event> UnsafeListener<T> listenUnsafe(
            @NotNull Object owner,
            @NotNull Class<T> eventClass,
            int priority,
            boolean alwaysListen,
            @NotNull Consumer<T> handler
    ) {
        UnsafeListener<T> listener = new UnsafeListener<>(
                priority,
                owner,
                alwaysListen,
                event -> {
                    handler.accept(event);
                    return Unit.INSTANCE;
                }
        );

        EventFlow.INSTANCE.getSyncListeners().subscribe(
                JvmClassMappingKt.getKotlinClass(eventClass),
                listener
        );

        return listener;
    }

    public static <T extends Event> AtomicReference<T> listenOnceUnsafe(
            @NotNull Object owner,
            @NotNull Class<T> eventClass,
            int priority,
            boolean alwaysListen,
            @NotNull Predicate<T> predicate
    ) {
        AtomicReference<T> pointer = new AtomicReference<>(null);
        AtomicReference<UnsafeListener<T>> listenerHolder = new AtomicReference<>();

        UnsafeListener<T> listener = listenUnsafe(
                owner,
                eventClass,
                priority,
                alwaysListen,
                event -> {
                    pointer.set(event);

                    if (predicate.test(event)) {
                        EventFlow.INSTANCE.getSyncListeners().unsubscribe(listenerHolder.get());
                    }
                }
        );

        listenerHolder.set(listener);
        EventFlow.INSTANCE.getSyncListeners().subscribe(
                JvmClassMappingKt.getKotlinClass(eventClass),
                listener
        );

        return pointer;
    }

    public static <T extends Event> UnsafeListener<T> listenConcurrentlyUnsafe(
            @NotNull Object owner,
            @NotNull Class<T> eventClass,
            int priority,
            boolean alwaysListen,
            @NotNull CoroutineDispatcher scheduler,
            @NotNull Consumer<T> handler
    ) {
        UnsafeListener<T> listener = listenUnsafe(
                owner,
                eventClass,
                priority,
                alwaysListen,
                event -> ThreadingKt.runConcurrent(scheduler, (scope, continuation) -> {
                    handler.accept(event);
                    return Unit.INSTANCE;
                })
        );

        EventFlow.INSTANCE.getConcurrentListeners().subscribe(
                JvmClassMappingKt.getKotlinClass(eventClass),
                listener
        );

        return listener;
    }
}
