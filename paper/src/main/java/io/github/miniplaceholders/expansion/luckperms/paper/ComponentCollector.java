package io.github.miniplaceholders.expansion.luckperms.paper;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ComponentCollector implements Collector<Component, TextComponent.Builder, Component> {
    private final Supplier<Component> appender;
    private ComponentCollector(Supplier<Component> appender){
        this.appender = appender;
    }
    private ComponentCollector(){
        this(Component::space);
    }

    public static ComponentCollector append(Supplier<Component> appender){
        return new ComponentCollector(appender);
    }

    public static ComponentCollector append(Component appender){
        return new ComponentCollector(() -> appender);
    }

    public static ComponentCollector spacing(){
        return new ComponentCollector();
    }

    public static ComponentCollector parsing(String string){
        return new ComponentCollector(() -> MiniMessage.miniMessage().deserialize(string));
    }

    public static ComponentCollector withStyle(Style style){
        return new ComponentCollector(() -> Component.text("", style));
    }
    
    @Override
    public Supplier<Builder> supplier() {
        return Component::text;
    }

    @Override
    public BiConsumer<Builder, Component> accumulator() {
        return (builder, component) -> builder.append(component).append(appender.get());
    }

    @Override
    public BinaryOperator<Builder> combiner() {
        return (builder, otherBuilder) -> builder.append(otherBuilder.build());
    }

    @Override
    public Function<Builder, Component> finisher() {
        return Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
