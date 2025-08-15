package server;

public class GUI{

    protected static final String RED = "\u001B[31m";
    protected static final String GREEN = "\u001B[32m";
    protected static final String YELLOW = "\u001B[33m";
    protected static final String RESET = "\u001B[0m";
    protected static final String BOLD = "\033[1m";
    protected static final String BG_RED = "\033[41m";
    protected static final String BG_YELLOW = "\033[43m";
    

    public static void start(){
    
    System.out.print("\033[H\033[2J");
    System.out.flush();

    System.out.println(YELLOW+"██████╗░░█████╗░██████╗░██╗░░██╗░██████╗████████╗██████╗░░"+RED+"█████"+YELLOW+"╗░██╗░░██╗███████╗");
    System.out.println("██╔══██╗██╔══██╗██╔══██╗██║░██╔╝██╔════╝╚══██╔══╝██╔══██╗"+RED+"██"+YELLOW+"╔══"+RED+"██"+YELLOW+"╗██║░██╔╝██╔════╝");
    System.out.println("██║░░██║███████║██████╔╝█████═╝░╚█████╗░░░░██║░░░██████╔╝"+RED+"██"+YELLOW+"║░░"+RED+"██"+YELLOW+"║█████═╝░█████╗░░");
    System.out.println("██║░░██║██╔══██║██╔══██╗██╔═██╗░░╚═══██╗░░░██║░░░██╔══██╗"+RED+"██"+YELLOW+"║░░"+RED+"██"+YELLOW+"║██╔═██╗░██╔══╝░░");
    System.out.println("██████╔╝██║░░██║██║░░██║██║░╚██╗██████╔╝░░░██║░░░██║░░██║╚"+RED+"█████"+YELLOW+"╔╝"+YELLOW+"██║░╚██╗███████╗");
    System.out.println("╚═════╝░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝╚═════╝░░░░╚═╝░░░╚═╝░░╚═╝░╚════╝░╚═╝░░╚═╝╚══════╝\n");
    System.out.println(BG_RED+"                                    "+BG_YELLOW+"                "+RESET+YELLOW+BOLD+"   A SILENT KEYLOGGER "+RED+"["+YELLOW+" v1.0 "+RED+"]"+RESET);
    
    System.out.println(RED+"\n["+YELLOW+"+"+RED+"]"+YELLOW+"                  MUHAMMAD SAAD"+RED+"                                        ::::::::");
    System.out.println("                                                                        ::::::::::");
    System.out.println(RED+"["+YELLOW+"+"+RED+"]"+YELLOW+"                      JAVA"+RED+"                                         ::::::::::::");
    System.out.println("                                                                    ::::::::::::::");
    System.out.println(RED+"["+YELLOW+"+"+RED+"]"+YELLOW+"                 DARKSTROKE V1.0"+RED+"                               ::::::::::::::::");
    System.out.println("                                                                ::::::::::::::::::");
    System.out.println(RED+"["+YELLOW+"+"+RED+"]"+YELLOW+"                 REMOTE KEYLOGGER"+RED+"                          ::::::::::::::::::::");
    System.out.println("                                                            ::::::::::::::::::::::");
    System.out.println(RED+"["+YELLOW+"+"+RED+"]"+YELLOW+"                    08-15-2025"+RED+"                         ::::::::::::::::::::::::");
    System.out.println("                                                        ::::::::::::::::::::::::::");
    System.out.println(RED+"["+YELLOW+"+"+RED+"]"+YELLOW+"               INITIALIZING SERVER"+RED+"                 ::::::::::::::::::::::::::::\n");
    System.out.println(BG_YELLOW+"                                                    "+BG_RED+"                              "+RESET+"\n\n");
    
    //System.out.println("..................................................................................");
    //System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
    
}

}
