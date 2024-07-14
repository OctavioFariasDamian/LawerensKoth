package net.lawerens.koth.utils;

import org.bukkit.command.CommandSender;

import static net.lawerens.koth.utils.Colorize.color;

public class CenterMessage {

    private final static int CENTER_PX = 154;

    public static void sendCenteredMessage(CommandSender s, String message){
        message = color(message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        s.sendMessage(sb + message);
    }

    public static void sendUnderline(CommandSender sender, char color) {
        sender.sendMessage(color("&"+color+"&m" + "                                                                      "));
    }
}
