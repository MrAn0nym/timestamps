package com.aliucord.plugins;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.api.commands.CommandChoice;
import com.discord.models.commands.ApplicationCommandOption;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;

// This class is never used so your IDE will likely complain. Let's make it shut up!
@SuppressWarnings("unused")
public class Timestamps extends Plugin {
  @NonNull
  @Override
  // Plugin Manifest - Required
  public Manifest getManifest() {
    var manifest = new Manifest();
    manifest.authors =
        new Manifest.Author[] {new Manifest.Author("Namenlosxy", 339303461877186560L)};
    manifest.description = "Generates unix timestamps for discord and copies it to the clipboard";
    manifest.version = "1.0.1";
    manifest.updateUrl = "https://raw.githubusercontent.com/MrAn0nym/timestamps/builds/updater.json";
    return manifest;
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  // Called when your plugin is started. This is the place to register command, add patches, etc
  public void start(Context context) {
    var modes =
        Arrays.asList(
            new CommandChoice("Short Time", "t"),
            new CommandChoice("Long Time", "T"),
            new CommandChoice("Short Date", "d"),
            new CommandChoice("Long Date", "D"),
            new CommandChoice("Short Date/Time", "f"),
            new CommandChoice("Long Date/Time", "F"),
            new CommandChoice("Relative Time", "R"));

    var options =
        Arrays.asList(
            new ApplicationCommandOption(
                ApplicationCommandType.INTEGER, "yyyy", "The year", null, false, false, null, null),
            new ApplicationCommandOption(
                ApplicationCommandType.INTEGER, "MM", "The month", null, false, false, null, null),
            new ApplicationCommandOption(
                ApplicationCommandType.INTEGER, "dd", "The day", null, false, false, null, null),
            new ApplicationCommandOption(
                ApplicationCommandType.INTEGER, "HH", "The hour", null, false, false, null, null),
            new ApplicationCommandOption(
                ApplicationCommandType.INTEGER, "mm", "The minute", null, false, false, null, null),
            new ApplicationCommandOption(
                ApplicationCommandType.INTEGER, "ss", "The second", null, false, false, null, null),
            new ApplicationCommandOption(
                ApplicationCommandType.STRING, "z", "The timezone", null, false, false, null, null),
            new ApplicationCommandOption(
                ApplicationCommandType.STRING,
                "mode",
                "The mode in which discord will display the date",
                null,
                false,
                false,
                modes,
                null));

    commands.registerCommand(
        "timestamp",
        "Generates a unix timestamp and copies it to the clipboard",
        options,
        ctx -> {
          String result;
          ZoneId zone;
          ClipboardManager clipboard =
              (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

          var year = ctx.getIntOrDefault("yyyy", Calendar.getInstance().get(Calendar.YEAR));
          var month = ctx.getIntOrDefault("MM", Calendar.getInstance().get(Calendar.MONTH));
          var day = ctx.getIntOrDefault("dd", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
          var hour = ctx.getIntOrDefault("HH", Calendar.getInstance().get(Calendar.HOUR));
          var minute = ctx.getIntOrDefault("mm", Calendar.getInstance().get(Calendar.MINUTE));
          var second = ctx.getIntOrDefault("ss", Calendar.getInstance().get(Calendar.SECOND));
          var zoneString = ctx.getStringOrDefault("z", ZoneId.systemDefault().toString());
          var mode = ctx.getStringOrDefault("mode", "f");
          try {
            zone = ZoneId.of(zoneString);
          } catch (DateTimeException e) {
            return new CommandsAPI.CommandResult("Invalid timezone identifier", null, false);
          }

          try {
            // Create ZonedDateTime object
            ZonedDateTime time = ZonedDateTime.of(year, month, day, hour, minute, second, 0, zone);

            // Create message
            String message =
                "<t:" + time.toEpochSecond() + ":" + ctx.getStringOrDefault("mode", "f") + ">";

            // Create ClipData and push it
            ClipData clip = ClipData.newPlainText("Discord timestamp", message);
            clipboard.setPrimaryClip(clip);

            // Display confirmation
            result = "Copied " + message + " to your clipboard";
          } catch (DateTimeException e) {

            // Catch invalid dates/times and send the exception message to the user
            return new CommandsAPI.CommandResult(
                "Invalid date\n```java\n" + e.getMessage() + "\n```", null, false);
          }

          return new CommandsAPI.CommandResult(result, null, false);
        });
  }

  @Override
  // Called when your plugin is stopped
  public void stop(Context context) {
    // Unregisters all commands
    commands.unregisterAll();
  }
}
