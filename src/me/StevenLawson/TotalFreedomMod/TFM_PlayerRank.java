package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.DEVELOPERS;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum TFM_PlayerRank
{
    DEVELOPER("a " + ChatColor.DARK_PURPLE + "Developer", ChatColor.DARK_PURPLE + "(Developer)"),
    IMPOSTOR("an " + ChatColor.GRAY + ChatColor.UNDERLINE + "Impostor", ChatColor.GRAY.toString() + ChatColor.UNDERLINE + "(Imp)"),
    NON_OP("a " + ChatColor.WHITE + "Non-OP", ChatColor.WHITE.toString()),
    OP("an " + ChatColor.RED + "OP", ChatColor.RED + "(Op)"),
    SUPER("an " + ChatColor.YELLOW + "Admin", ChatColor.YELLOW + "(Admin)"),
    TELNET("a " + ChatColor.AQUA + "Super Admin", ChatColor.AQUA + "(Super Admin)"),
    SENIOR("a " + ChatColor.LIGHT_PURPLE + "Senior Admin", ChatColor.LIGHT_PURPLE + "(Senior Admin)"),
    OWNER("the " + ChatColor.BLUE + "Owner", ChatColor.BLUE + "(Owner)"),
    EXEC("an " + ChatColor.GOLD + "Executive", ChatColor.GOLD + "(Executive)"),
    SYS("a " + ChatColor.DARK_PURPLE + "System Admin", ChatColor.DARK_PURPLE + "(Sys-Admin)"),
    SPEC("a " + ChatColor.GREEN + "Specialist", ChatColor.GREEN + "(Specialist)"),
    ADMINM("the " + ChatColor.DARK_RED + "Admin Manager", ChatColor.DARK_RED + "(Admin Manager)"),
    FOUNDER("the " + ChatColor.BLUE + "Founder", ChatColor.BLUE + "(Founder)"),
    COOWNER("the " + ChatColor.BLUE + "Co-Owner", ChatColor.BLUE + "(Co-Owner)"),
    CONSOLE("The " + ChatColor.DARK_PURPLE + "Console", ChatColor.DARK_PURPLE + "(CONSOLE)");
    private final String loginMessage;
    private final String prefix;

    private TFM_PlayerRank(String loginMessage, String prefix)
    {
        this.loginMessage = loginMessage;
        this.prefix = prefix;
    }

    public static String getLoginMessage(CommandSender sender)
    {
        // Handle console
        if (!(sender instanceof Player))
        {
            return fromSender(sender).getLoginMessage();
        }

        // Handle admins
        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);
        if (entry == null)
        {
            // Player is not an admin
            return fromSender(sender).getLoginMessage();
        }

        // Custom login message
        final String loginMessage = entry.getCustomLoginMessage();

        if (loginMessage == null || loginMessage.isEmpty())
        {
            return fromSender(sender).getLoginMessage();
        }

        return ChatColor.translateAlternateColorCodes('&', loginMessage);
    }

    public static TFM_PlayerRank fromSender(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return CONSOLE;
        }

        if (TFM_AdminList.isAdminImpostor((Player) sender))
        {
            return IMPOSTOR;
        }

        if (DEVELOPERS.contains(sender.getName()))
        {
            return DEVELOPER;
        }

        final TFM_Admin entry = TFM_AdminList.getEntryByIp(TFM_Util.getIp((Player) sender));

        final TFM_PlayerRank rank;

        if (entry != null && entry.isActivated())
        {
            if (TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName()))
            {
                return OWNER;
            }

            if (entry.isSeniorAdmin())
            {
                rank = SENIOR;
            }
            else if (entry.isTelnetAdmin())
            {
                rank = TELNET;
            }
            else
            {
                rank = SUPER;
            }
            if (sender.getName().equals("xYurippe"))
            {
                return EXEC;
            }
            if (sender.getName().equals("DarkHorse108"))
            {
                return ADMINM;
            }
            if (sender.getName().equals("Cyro1999"))
            {
                return COOWNER;
            }
            if (sender.getName().equals("NL_Fenix_NL"))
            {
                return FOUNDER;
            }
        }
        else
        {
            if (sender.isOp())
            {
                rank = OP;
            }
            else
            {
                rank = NON_OP;
            }

        }
        return rank;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getLoginMessage()
    {
        return loginMessage;
    }
}
