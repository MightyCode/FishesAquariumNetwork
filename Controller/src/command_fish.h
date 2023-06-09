#ifndef _COMMAND_FISH_H_
#define _COMMAND_FISH_H_

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <errno.h>
#include <time.h>
#include <unistd.h>
#include <pthread.h>
#include <assert.h>

#include "thread.h"
#include "server_utils.h"

int add_fish_server(int check_ls, int client_number, char * header, char * buffer, struct aquarium * aquarium, pthread_mutex_t * mutex, int sock, struct view * client_view);
int del_fish_server(int check_ls, int client_number, char * header, char * buffer, struct aquarium * aquarium, pthread_mutex_t * mutex, int sock);
int start_fish_server(int check_ls, int client_number, char * header, char * buffer, struct aquarium * aquarium, pthread_mutex_t * mutex, int sock);
int start_fish_all_server(int check_ls, int client_number, char * header, char * buffer, struct aquarium * aquarium, pthread_mutex_t * mutex, int sock);

#endif