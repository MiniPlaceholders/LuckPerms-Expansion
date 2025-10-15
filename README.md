# LuckPerms-Expansion
LuckPerms Expansion for MiniPlaceholders

## Placeholders

`<luckperms_prefix>`*

`<luckperms_suffix>`*

`<luckperms_has_permission:permission>` (true/false)

`<luckperms_check_permission:permission>` (true/false/undefined)

`<luckperms_primary_group_name>`*

`<luckperms_inherits_group:group>` (true/false)

`<luckperms_meta:meta-key>`*

*By default these resolve to an isolated component that doesn't leak its styles to the rest of the string.
If you want them inserted as strings prior to parsing so they affect the output add the `s` or `string` option to the end. Like this: `<luckperms_prefix:s>`

