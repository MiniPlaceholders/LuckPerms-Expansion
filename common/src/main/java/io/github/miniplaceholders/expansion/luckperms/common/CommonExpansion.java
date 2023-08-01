package io.github.miniplaceholders.expansion.luckperms.common;

import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static io.github.miniplaceholders.api.utils.Components.*;
import static io.github.miniplaceholders.api.utils.LegacyUtils.LEGACY_HEX_SERIALIZER;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public record CommonExpansion(LuckPerms luckPerms) {
    private static final Component UNDEFINED_COMPONENT = Component.text("undefined", NamedTextColor.GRAY);

    public Expansion.Builder commonBuilder() {
        return Expansion.builder("luckperms")
                .audiencePlaceholder("prefix", (aud, queue, ctx) -> {
                    final User user = user(aud);
                    if (user == null) {
                        return null;
                    }
                    return Tag.inserting(parsePossibleLegacy(user.getCachedData().getMetaData().getPrefix()));
                })
                .audiencePlaceholder("suffix", (aud, queue, ctx) -> {
                    final User user = user(aud);
                    if (user == null) {
                        return null;
                    }
                    return Tag.inserting(parsePossibleLegacy(user.getCachedData().getMetaData().getSuffix()));
                })
                .audiencePlaceholder("get_meta", (aud, queue, ctx) -> {
                    final User user = user(aud);
                    if (user == null) {
                        return null;
                    }
                    String meta = queue.popOr(() -> "you need to introduce a meta key").value();
                    String result = user.getCachedData().getMetaData().getMetaValue(meta);
                    return Tag.inserting(parsePossibleLegacy(result));
                })
                .audiencePlaceholder("has_permission", (aud, queue, ctx) -> {
                    final User user = user(aud);
                    if (user == null) {
                        return null;
                    }
                    String permission = queue.popOr(() -> "you need to introduce an permission").value();
                    Tristate result = user.getCachedData().getPermissionData().checkPermission(permission);
                    return Tag.selfClosingInserting(result.asBoolean()
                            ? TRUE_COMPONENT
                            : FALSE_COMPONENT
                    );
                })
                .audiencePlaceholder("check_permission", (aud, queue, ctx) -> {
                    final User user = user(aud);
                    if (user == null) {
                        return null;
                    }
                    final String permission = queue.popOr(() -> "you need to introduce an permission").value();
                    final Tristate result = user.getCachedData().getPermissionData().checkPermission(permission);

                    return Tag.selfClosingInserting(switch(result) {
                        case TRUE -> TRUE_COMPONENT;
                        case FALSE -> FALSE_COMPONENT;
                        case UNDEFINED -> UNDEFINED_COMPONENT;
                    });
                })
                .audiencePlaceholder("inherited_groups", (aud, queue, ctx) -> {
                    final User user = user(aud);
                    if (user == null) {
                        return null;
                    }
                    final Component groups = user.getInheritedGroups(user.getQueryOptions()).stream()
                            .map(group -> parsePossibleLegacy(group.getDisplayName()))
                            .collect(Component.toComponent(Component.text(", ")));
                    return Tag.selfClosingInserting(groups);
                })
                .audiencePlaceholder("primary_group_name", (aud, queue, ctx) -> {
                    final User user = user(aud);
                    if (user == null) {
                        return null;
                    }
                    final String primaryGroup = user.getCachedData().getMetaData().getPrimaryGroup();
                    if (primaryGroup == null) {
                        return null;
                    }
                    return Tag.preProcessParsed(primaryGroup);
                })
                .audiencePlaceholder("inherits_group", (aud, queue, ctx) -> {
                    final User user = user(aud);
                    if (user == null) {
                        return null;
                    }
                    Group group = luckPerms.getGroupManager().getGroup(queue.popOr("you need to provide a group").value());
                    return Tag.selfClosingInserting(group != null && user.getInheritedGroups(user.getQueryOptions()).contains(group)
                            ? TRUE_COMPONENT
                            : FALSE_COMPONENT
                    );
                });
    }

    private User user(final Audience audience) {
        final UUID uuid = audience.get(Identity.UUID).orElse(null);
        if (uuid == null) {
            return null;
        }
        return luckPerms.getUserManager().getUser(uuid);
    }

    public static @NotNull Component parsePossibleLegacy(final @Nullable String string) {
        if (string == null || string.isBlank()) return Component.empty();
        if (string.indexOf('&') == -1) {
            return miniMessage().deserialize(string);
        }
        return miniMessage().deserialize(
                miniMessage().serialize(LEGACY_HEX_SERIALIZER.deserialize(string))
                        .replace("\\<", "<")
                        .replace("\\>", ">")
        );
    }
}
