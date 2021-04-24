package pl.trollcraft.crv.we.model;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldEditRestriction {

    private static final Logger LOG
            = Logger.getLogger(WorldEditRestriction.class.getSimpleName());

    private List<String> limitedCommands; // Limited WE commands.
    private Pattern forbiddenPattern;

    private long maxDefault;
    private long maxVip;
    private long maxSVip;
    private long maxMVip;

    private int start; // When user can start using WE.

    private int cooldownDefault;
    private int cooldownVip;
    private int cooldownSVip;
    private int cooldownMVip;

    public Optional<String> getLimitedCommand(String command) {
        return limitedCommands.stream()
                .filter(command::contains)
                .findFirst();
    }

    public void setLimitedCommands(List<String> limitedCommands) {
        this.limitedCommands = limitedCommands;
    }

    public void setForbiddenMaterials(List<String> forbiddenMaterials) {

        StringBuilder regex = new StringBuilder();
        String forbidden;

        for (int i = 0 ; i < forbiddenMaterials.size() ; i++) {
            forbidden = forbiddenMaterials.get(i);

            regex.append(String.format("((^%s)|(,%s,)|(,%s$))",
                    forbidden, forbidden, forbidden));

            if (i != forbiddenMaterials.size()-1)
                regex.append("|");
        }

        LOG.info("Created REGEX: " + regex.toString());

        forbiddenPattern = Pattern.compile(regex.toString(),
                Pattern.CASE_INSENSITIVE);

    }

    public long getMaxDefault() {
        return maxDefault;
    }

    public void setMaxDefault(long maxDefault) {
        this.maxDefault = maxDefault;
    }

    public long getMaxVip() {
        return maxVip;
    }

    public long getMaxMVip() {
        return maxMVip;
    }

    public void setMaxVip(long maxVip) {
        this.maxVip = maxVip;
    }

    public long getMaxSVip() {
        return maxSVip;
    }

    public void setMaxSVip(long maxSVip) {
        this.maxSVip = maxSVip;
    }

    public void setMaxMVip(long maxMVip) {
        this.maxMVip = maxMVip;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCooldownDefault() {
        return cooldownDefault;
    }

    public void setCooldownDefault(int cooldownDefault) {
        this.cooldownDefault = cooldownDefault;
    }

    public int getCooldownVip() {
        return cooldownVip;
    }

    public void setCooldownVip(int cooldownVip) {
        this.cooldownVip = cooldownVip;
    }

    public int getCooldownSVip() {
        return cooldownSVip;
    }

    public int getCooldownMVip() {
        return cooldownMVip;
    }

    public void setCooldownSVip(int cooldownSVip) {
        this.cooldownSVip = cooldownSVip;
    }

    public void setCooldownMVip(int cooldownMVip) {
        this.cooldownMVip = cooldownMVip;
    }

    public boolean containsForbidden(String materials) {
        Matcher matcher = forbiddenPattern.matcher(materials);
        return matcher.find();
    }

}
