package de.frozenbytes.kickermost.http;

import de.frozenbytes.kickermost.PropertiesLoader;
import de.frozenbytes.kickermost.dto.Match;
import de.frozenbytes.kickermost.dto.StoryPart;
import de.frozenbytes.kickermost.dto.type.Country;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Properties;

public class MattermostMessageBuilder {

    private static final String NEW_LINE = "\n";
    private static final String HEADLINE_BIG = "## ";
    private static final String HEADLINE_MEDIUM = "### ";
    private static final String HEADLINE_SMALL = "#### ";
    private static final String SPACE = " ";
    private static final String GOAL = "TOR!";
    private static final String OWN_GOAL = "EIGENTOR!";

    public static String createJsonMessage(final Match match, final StoryPart messageParameters) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Properties properties = PropertiesLoader.loadProperties();
        builder.add("username", properties.getProperty(PropertiesLoader.USERNAME));
        builder.add("channel", properties.getProperty(PropertiesLoader.CHANNEL));
        builder.add("icon_url", properties.getProperty(PropertiesLoader.ICON_URL));
        builder.add("text", buildMattermostMessage(match, messageParameters));
        return builder.build().toString();
    }

    private static String buildMattermostMessage(final Match match, final StoryPart messageParameters) {
        switch (messageParameters.getEvent()){
            case GOAL:
                return buildGoalMessage(match, messageParameters, GOAL);
            case GOAL_OWN:
                return buildGoalMessage(match, messageParameters, OWN_GOAL);
            case EXCHANGE:
                return buildExchangeMessage(match, messageParameters);
            case YELLOW_CARD:
            case RED_CARD:
            case YELLOW_RED_CARD:
                return buildCardMessage(match, messageParameters);
            case PENALTY:
            case PENALTY_FAILURE:
                return buildPenaltyMessage(match, messageParameters);
            case DEFAULT:
                return buildGenericMessage(messageParameters);
            default:
                throw new IllegalArgumentException("Unknown type!");

        }
    }

    private static String buildGoalMessage(final Match match, final StoryPart messageParameters, final String HeaderString){
        StringBuilder builder = new StringBuilder();

        builder.append(HEADLINE_BIG);
        builder.append(messageParameters.getEvent().getMattermostCode()).append(SPACE).append(HeaderString);

        builder.append(NEW_LINE);

        builder.append(HEADLINE_MEDIUM);
        builder.append(buildScoreString(match));

        builder.append(NEW_LINE);

        builder.append(HEADLINE_SMALL);
        builder.append(messageParameters.getGameMinute()).append(SPACE).append(messageParameters.getTitle());

        builder.append(NEW_LINE);

        builder.append(messageParameters.getDescription());

        return builder.toString();
    }

    private static String buildExchangeMessage(final Match match, final StoryPart messageParameters) {
        return buildGenericMessageWithTitle(match, messageParameters);
    }

    private static String buildCardMessage(final Match match, final StoryPart messageParameters) {
        return buildGenericMessageWithTitle(match, messageParameters);
    }

    private static String buildPenaltyMessage(final Match match, final StoryPart messageParameters) {
        return buildGenericMessageWithTitle(match, messageParameters);
    }

    private static String buildGenericMessageWithTitle(final Match match, final StoryPart messageParameters) {
        StringBuilder builder = new StringBuilder();

        builder.append(HEADLINE_SMALL);
        builder.append(messageParameters.getEvent().getMattermostCode()).append(SPACE);
        builder.append(messageParameters.getGameMinute()).append(SPACE).append(messageParameters.getTitle());

        builder.append(NEW_LINE);
        builder.append(buildScoreString(match));
        builder.append(NEW_LINE);

        builder.append(messageParameters.getDescription() != null ? messageParameters.getDescription() : "");

        return builder.toString();
    }

    private static String buildGenericMessage(final StoryPart messageParameters) {
        StringBuilder builder = new StringBuilder();

        builder.append(messageParameters.getGameMinute() != null ? messageParameters.getGameMinute() : "").append(SPACE).append(messageParameters.getDescription());

        return builder.toString();
    }

    private static String buildScoreString(final Match match) {
        StringBuilder builder = new StringBuilder();

        //TODO: Handle case that country could not be mapped -> possible NPE
        Country teamA = Country.getCountryByName(match.getTeamA().getName().getValue());
        Country teamB = Country.getCountryByName(match.getTeamB().getName().getValue());

        builder.append(teamA.getName()).append(SPACE).append(teamA.getMattermostCode()).append(SPACE).append(match.getTeamA().getScore());
        builder.append(" : ");
        builder.append(match.getTeamB().getScore()).append(SPACE).append(teamB.getMattermostCode()).append(SPACE).append(teamB.getName());

        return builder.toString();
    }
}
