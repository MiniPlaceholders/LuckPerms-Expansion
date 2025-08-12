package io.github.miniplaceholders.expansion.luckperms;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.provider.ExpansionProvider;
import io.github.miniplaceholders.api.provider.LoadRequirement;
import io.github.miniplaceholders.api.utils.Tags;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.util.Tristate;

import java.util.Optional;
import java.util.UUID;

import static io.github.miniplaceholders.api.utils.Components.FALSE_COMPONENT;
import static io.github.miniplaceholders.api.utils.Components.TRUE_COMPONENT;
import static io.github.miniplaceholders.api.utils.LegacyStrings.parsePossibleLegacy;

public final class LuckPermsExpansionProvider implements ExpansionProvider {
    private static final Component UNDEFINED_COMPONENT = Component.text("undefined", NamedTextColor.GRAY);

    private User user(final Audience audience, final LuckPerms luckPerms) {
        final UUID uuid = audience.get(Identity.UUID).orElse(null);
        if (uuid == null) {
            return null;
        }
        return luckPerms.getUserManager().getUser(uuid);
    }

    @Override
    public Expansion provideExpansion() {
        final LuckPerms luckPerms = LuckPermsProvider.get();
        return Expansion.builder("luckperms")
                .author("MiniPlaceholders Contributors")
                .version("2.0.0")
                .audiencePlaceholder("prefix", (aud, queue, ctx) -> {
                    final User user = user(aud, luckPerms);
                    if (user == null) {
                        return null;
                    }
                    final String prefix = user.getCachedData().getMetaData().getPrefix();
                    
                    if (parseString(queue)) {
                        return Tag.preProcessParsed(MiniMessage.miniMessage().serialize(parsePossibleLegacy(prefix, ctx)));
                    } else {
                        return Tag.inserting(parsePossibleLegacy(prefix, ctx));
                    }
                })
                .audiencePlaceholder("suffix", (aud, queue, ctx) -> {
                    final User user = user(aud, luckPerms);
                    if (user == null) {
                        return null;
                    }
                    final String suffix = user.getCachedData().getMetaData().getSuffix();
                    
                    if (parseString(queue)) {
                        return Tag.preProcessParsed(MiniMessage.miniMessage().serialize(parsePossibleLegacy(suffix, ctx)));
                    } else {
                        return Tag.inserting(parsePossibleLegacy(suffix, ctx));
                    }
                })
                .audiencePlaceholder("meta", (aud, queue, ctx) -> {
                    final User user = user(aud, luckPerms);
                    if (user == null) {
                        return null;
                    }
                    final String meta = queue.popOr(() -> "you need to introduce a meta key").value();
                    final Optional<QueryOptions> queryOptions = luckPerms.getContextManager().getQueryOptions(user);
                    if (queryOptions.isEmpty()) {
                        return null;
                    }
                    final String result = user.getCachedData().getMetaData(queryOptions.get()).getMetaValue(meta);
                    
                    if (parseString(queue)) {
                        return Tag.preProcessParsed(result != null ? result : "");
                    } else {
                        if (result == null) {
                            return Tags.EMPTY_TAG;
                        }
                        return Tag.preProcessParsed(result);
                    }
                })
                .audiencePlaceholder("has_permission", (aud, queue, ctx) -> {
                    final User user = user(aud, luckPerms);
                    if (user == null) {
                        return null;
                    }
                    final String permission = queue.popOr(() -> "you need to introduce an permission").value();
                    final Tristate result = user.getCachedData().getPermissionData().checkPermission(permission);
                    final boolean hasPermission = result.asBoolean();
                    
                    if (parseString(queue)) {
                        return Tag.preProcessParsed(hasPermission ? "true" : "false");
                    } else {
                        return Tag.selfClosingInserting(hasPermission
                                ? TRUE_COMPONENT
                                : FALSE_COMPONENT
                        );
                    }
                })
                .audiencePlaceholder("check_permission", (aud, queue, ctx) -> {
                    final User user = user(aud, luckPerms);
                    if (user == null) {
                        return null;
                    }
                    final String permission = queue.popOr(() -> "you need to introduce an permission").value();
                    final Tristate result = user.getCachedData().getPermissionData().checkPermission(permission);
                    
                    if (parseString(queue)) {
                        return Tag.preProcessParsed(switch (result) {
                            case TRUE -> "true";
                            case FALSE -> "false";
                            case UNDEFINED -> "undefined";
                        });
                    } else {
                        return Tag.selfClosingInserting(switch (result) {
                            case TRUE -> TRUE_COMPONENT;
                            case FALSE -> FALSE_COMPONENT;
                            case UNDEFINED -> UNDEFINED_COMPONENT;
                        });
                    }
                })
                .audiencePlaceholder("inherited_groups", (aud, queue, ctx) -> {
                    final User user = user(aud, luckPerms);
                    if (user == null) {
                        return null;
                    }
                    
                    if (parseString(queue)) {
                        final String groups = user.getInheritedGroups(user.getQueryOptions()).stream()
                                .map(group -> MiniMessage.miniMessage().serialize(parsePossibleLegacy(group.getDisplayName())))
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("");
                        return Tag.preProcessParsed(groups);
                    } else {
                        final Component groups = user.getInheritedGroups(user.getQueryOptions()).stream()
                                .map(group -> parsePossibleLegacy(group.getDisplayName(), ctx))
                                .collect(Component.toComponent(Component.text(", ")));
                        return Tag.selfClosingInserting(groups);
                    }
                })
                .audiencePlaceholder("primary_group_name", (aud, queue, ctx) -> {
                    final User user = user(aud, luckPerms);
                    if (user == null) {
                        return null;
                    }
                    final String primaryGroup = user.getCachedData().getMetaData().getPrimaryGroup();
                    
                    if (parseString(queue)) {
                        return Tag.preProcessParsed(primaryGroup != null ? primaryGroup : "");
                    } else {
                        if (primaryGroup == null) {
                            return Tags.EMPTY_TAG;
                        }
                        return Tag.preProcessParsed(primaryGroup);
                    }
                })
                .audiencePlaceholder("inherits_group", (aud, queue, ctx) -> {
                    final User user = user(aud, luckPerms);
                    if (user == null) {
                        return null;
                    }
                    final Group group = luckPerms.getGroupManager().getGroup(queue.popOr("you need to provide a group").value());
                    final boolean inheritsGroup = group != null && user.getInheritedGroups(user.getQueryOptions()).contains(group);
                    
                    if (parseString(queue)) {
                        return Tag.preProcessParsed(inheritsGroup ? "true" : "false");
                    } else {
                        return Tag.selfClosingInserting(inheritsGroup
                                ? TRUE_COMPONENT
                                : FALSE_COMPONENT
                        );
                    }
                }).build();
    }

    @Override
    public LoadRequirement loadRequirement() {
        return LoadRequirement.requiredComplement("luckperms", "LuckPerms");
    }

    private boolean parseString(ArgumentQueue queue) {
        return queue.hasNext() && queue.pop().lowerValue().equals("string");
    }
}
