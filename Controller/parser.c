#include "parser.h"

#include <string.h>
#include <stdio.h>
#include <stdlib.h>

void parse_command(struct command * command, char** argv, int argc){
    if(strcmp(argv[1],"load") == 0){
        if(argc-2 != 1){
            printf("Uncorrect use of \"load\", there should be 1 argument (received %d) which is the aquarium to load.\n", argc-2);
        }
        else{
            command->command_name="load";
            command->nb_params = 1;
            int malloc_size = command->nb_params * sizeof(char*);
            command->params=malloc(malloc_size);
            command->params[0]=argv[2];
            command->_total_param_len = malloc_size;
        }
    }
    else if(strcmp(argv[1],"show") == 0){
        if(argc-2 != 1){
            printf("Uncorrect use of \"show\", there should be 1 argument (received %d) which is the aquarium to show.\n", argc-2);
        }
        else{
            command->command_name="show";
            command->nb_params = 1;
            int malloc_size = command->nb_params * sizeof(char*);
            command->params=malloc(malloc_size);
            command->params[0]=argv[2];
            command->_total_param_len = malloc_size;
        }
    }
    else if(strcmp(argv[1],"add") == 0 && strcmp(argv[2],"view") == 0){
        if(argc-3 != 2){
            printf("Uncorrect use of \"add view\", there should be 2 arguments (received %d) which are the name of the view and its size\n", argc-3);
        }
        else{
            command->command_name="add view";
            command->nb_params = 2;
            int malloc_size = command->nb_params * sizeof(char*);
            command->params=malloc(malloc_size);
            command->params[0]=argv[3];
            if(!check_add_wiew_format(argv[4])){
                 printf("Uncorrect use of \"add view\", the second argument should respect the following format : VIEW_X x VIEW_Y + VIEW_WITDH + VIEW_HEIGHT without spaces (received %s)\n",argv[4]);
            };
            command->params[1]=argv[4];
            command->_total_param_len = malloc_size;
        }
    }
    else if(strcmp(argv[1],"del") == 0){
        if(argc-3 != 1){
            printf("Uncorrect use of \"del view\", there should be 1 argument 1 (received %d) which is the name of the view to remove.\n", argc-3);
        }
        else{
            command->command_name="del view";
            command->nb_params = 1;
            int malloc_size = command->nb_params * sizeof(char*);
            command->params=malloc(malloc_size);
            command->params[0]=argv[3];
            command->_total_param_len = malloc_size;
        }
    }
    else if(strcmp(argv[1],"save") == 0){
        if(argc-3 != 1){
            printf("Uncorrect use of \"save\", there should be 1 argument (received %d) which is the aquarium to save.\n", argc-2);
        }
        else{
            command->command_name="save";
            command->nb_params = 1;
            int malloc_size = command->nb_params * sizeof(char*);
            command->params=malloc(malloc_size);
            command->params[0]=argv[2];
            command->_total_param_len = malloc_size;
        }
    }
}

void free_command(struct command * command){
    free(command->params);
    free(command);
}

void print_command(struct command * command){
    printf("============ Debug ============\n");
    printf("command name : %s\n", command->command_name);
    printf("number of arguments : %d\n", command->nb_params);
    for(int i=0 ; i < command->nb_params ; i++){
        printf("- argument %d : %s\n", i, command->params[i]); //bug valgrind
    }
    printf("===============================\n");
}

void init_command(struct command * command){
    command->command_name = NULL;
    command->nb_params = 0;
    command->params = NULL;
    command->_total_param_len = 0;
}

int check_add_wiew_format(char * input){
    size_t i = 0;
    int times_founded = 0;
    int plus_founded = 0;
    while(i <  strlen(input)){
        if(input[i] == 'x'){times_founded+=1;}
        else if(input[i] == '+'){plus_founded+=1;}
        else{
            if(input[i] != '1' 
            && input[i] != '2' 
            && input[i] != '3' 
            && input[i] != '4' 
            && input[i] != '5' 
            && input[i] != '6' 
            && input[i] != '7' 
            && input[i] != '8' 
            && input[i] != '9' 
            && input[i] != '0')
            {return 0;}
        }

        if(input[i] == 'x' && plus_founded != 0){
            return 0;
        }
        i++;
    }
    if(times_founded == 1 && plus_founded == 2){return 1;}
    else{return 0;}
}

int main(int argc, char** argv){
    struct command * command = malloc(sizeof(struct command));
    
    init_command(command);

    if (argc < 3) {
        printf("failure\n");
        free_command(command);
        return EXIT_FAILURE;
    }

    parse_command(command, argv, argc);
    
    //print
    print_command(command);
    
    //free
    free_command(command);
    return EXIT_SUCCESS;
}