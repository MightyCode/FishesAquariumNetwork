#ifndef _INIT_CLIENT_H_
#define _INIT_CLIENT_H_


#include "server_utils.h"


int init_client(int check_ls, int client_number, char * header, char * buffer, struct aquarium * aquarium, pthread_mutex_t * mutex, int client_id, struct view * client_view);

#endif