package ProjetPoisson.project.command;

import ProjetPoisson.project.command.commands.*;
import ProjetPoisson.project.fish.FishManager;
import ProjetPoisson.project.scenes.MenuScene;

import java.util.HashMap;

public class CommandAnalyser {
    public static final int EMPTY_COMMAND = 0;
    public static final int NO_OPTIONAL_ARGUMENT = 1;

    public static final int COMMAND_TYPE_ARGUMENT = 0;
    public static final int FIRST_OPTIONAL_ARGUMENT = 1;

    HashMap<String, ICommand<String>> relations;

    public CommandAnalyser(MenuScene.ConnectionStateContainer state, FishManager fishManager, MenuScene.TryConnectionContainer tryConnection) {
        relations = new HashMap<>();
        relations.put("help", new HelpCommand());
        relations.put("clear", new ClearCommand());
        relations.put("quit", new QuitCommand());
        relations.put("addFish", new AddFishCommand(state, fishManager));
        relations.put("delFish", new DelFishCommand(state, fishManager));
        relations.put("startFish", new StartFishCommand(state, fishManager));
        relations.put("startFishAll", new StartFishAll());
        relations.put("status", new StatusCommand(state));
        relations.put("name", new ShowFishNameCommand(state, fishManager));
        relations.put("log", new LogOutCommand());
        relations.put("tryConnection", new TryConnectionCommand(tryConnection));
        relations.put("populate", new PopulateCommand(state));

    }

    public ResultCommand<String> analyseCommand(String command){
        String[] args = command.replace("/", "").replace("\n", "").split(" ");

        if (args.length == EMPTY_COMMAND)
            return new PromptResultCommand("> NOK : Empty command !");

        if ( ! relations.containsKey(args[COMMAND_TYPE_ARGUMENT]))
            return new PromptResultCommand("-> NOK : command introuvable");

        if (args.length == NO_OPTIONAL_ARGUMENT)
            return relations.get(args[COMMAND_TYPE_ARGUMENT]).process(args);

        if (args[FIRST_OPTIONAL_ARGUMENT].equalsIgnoreCase("help") || args[FIRST_OPTIONAL_ARGUMENT].equalsIgnoreCase("-h") )
            return relations.get(args[COMMAND_TYPE_ARGUMENT]).returnHelp();


        return relations.get(args[COMMAND_TYPE_ARGUMENT]).process(args);
    }
}
