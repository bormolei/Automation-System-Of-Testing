package ru.sbtqa.tag.pagefactory.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.qautils.properties.Props;

public class DesiredCapabilitiesParser {

    /**
     * Parses desired capabilities from config
     *
     * @return built capabilities
     */
    public DesiredCapabilities parse() {
        final DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);

        final String capsPrefix = "webdriver." + TagWebDriver.getBrowserName().toLowerCase() + ".capability.";
        Set<String> propKeys = Props.getProps().stringPropertyNames();
        List<String> capabilitiesFromProps = new ArrayList<>();

        for (String prop : propKeys) {
            if (prop.startsWith(capsPrefix)) {
                capabilitiesFromProps.add(prop);
            }
        }

        final Map<String, Object> options = new HashMap<>();

        for (String rawCapabilityKey : capabilitiesFromProps) {

            String capability = rawCapabilityKey.substring(capsPrefix.length());

            if (capability.startsWith("options") && "chrome".equals(TagWebDriver.getBrowserName())) {
                // For Chrome options must be parsed and specified as a data structure.
                // For non-chrome browsers options could be passed as string
                String optionsCapability = capability.substring("options.".length());
                switch (optionsCapability) {
                    case "args":
                    case "extensions":
                    case "excludeSwitches":
                    case "windowTypes":
                        String[] arrayOfStrings = Props.get(rawCapabilityKey).split(",");
                        final List<String> listOfStrings = new ArrayList<>();

                        for (String item : arrayOfStrings) {
                            listOfStrings.add(item.trim());
                        }

                        if (!listOfStrings.isEmpty()) {
                            options.put(optionsCapability, listOfStrings.toArray());
                        }
                        break;
                    case "prefs":
                    case "mobileEmulation":
                    case "perfLoggingPrefs":
                        final Map<String, Object> dictionary = new HashMap<>();
                        String[] dictRows = Props.get(rawCapabilityKey).split(",");

                        for (String row : dictRows) {
                            String[] keyVal = row.split("=>");
                            if ("true".equals(keyVal[1].toLowerCase().trim())
                                    || "false".equals(keyVal[1].toLowerCase().trim())) {
                                
                                dictionary.put(keyVal[0], Boolean.parseBoolean(keyVal[1].toLowerCase().trim()));
                            } else {
                                dictionary.put(keyVal[0], keyVal[1].trim());
                            }
                        }

                        if (!dictionary.isEmpty()) {
                            options.put(optionsCapability, dictionary);
                        }
                        break;
                    default:
                        options.put(optionsCapability, Props.get(rawCapabilityKey));
                        break;
                }
                if (!options.isEmpty()) {
                    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                }
            } else {
                if ("true".equals(Props.get(rawCapabilityKey).toLowerCase().trim())
                        || "false".equals(Props.get(rawCapabilityKey).toLowerCase().trim())) {
                    capabilities.setCapability(capability, Boolean.parseBoolean(Props.get(rawCapabilityKey)));
                } else {
                    capabilities.setCapability(capability, Props.get(rawCapabilityKey));
                }
            }
        }
        return capabilities;
    }
}
