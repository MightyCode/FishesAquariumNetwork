#ifndef _PROMPT_H_
#define _PROMPT_H_

#include "prompt_commands.h"

void prompt(int check, int * aquarium_flag, struct aquarium * aquarium, pthread_mutex_t * mutex_aquarium, pthread_mutex_t * mutex_aquarium_flag);

#endif
